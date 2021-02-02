package com.callstudio.cva;


import com.audium.server.contextManager.*;
import com.audium.server.session.ElementDataTypes;
import com.audium.server.session.VoiceElementData;
import com.callstudio.cva.request.DetectIntentRequest;
import com.callstudio.cva.response.DetectIntentResponse;
import com.callstudio.cva.response.Entity;
import com.cisco.cvp.callserver.agentassist.AgentAssistToken;
import com.cisco.cvp.ivr.WxmRestClient;
import com.cisco.cvp.serviceability.CVPLogMessages;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import com.audium.core.serviceability.SvcMgrUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MMService  {


   private  MMSessionContext mmSessionContext;
   private VoiceElementData md;
   private boolean turnOnLogging;

   public MMService(VoiceElementData md,boolean turnOnLogging){
       this.md = md;
       this.turnOnLogging = turnOnLogging;
   }

    public  void  createSessionContext(String sessionId, String projectId,String botLiteUrl,String subflowname) throws Exception {
        MMSessionContext mmSessionContext;
       // mmSessionContext =(MMSessionContext) ContextManager.getSessionContext(sessionId);
        mmSessionContext = getSessionContext(sessionId,subflowname);
        if(null==mmSessionContext){
            mmSessionContext = new MMSessionContext(projectId,botLiteUrl);
            mmSessionContext.init();
            setSessionContext(sessionId,subflowname,mmSessionContext);
            //ContextManager.addSessionContext(sessionId,mmSessionContext);


        }
      this.mmSessionContext = mmSessionContext;
    }

    private MMSessionContext getSessionContext(String sessionId,String subflowName){
       if(StringUtils.isBlank(subflowName)){
          return (MMSessionContext) ContextManager.getSessionContext(sessionId);
       }else{
           return (MMSessionContext)md.getControllerData().getSubFlowCVASessionContext(subflowName);
       }
    }

    private void setSessionContext(String sessionId,String subflowName,SessionContext sessionContext){
       if(StringUtils.isBlank(subflowName)){
           ContextManager.addSessionContext(sessionId,sessionContext);
       }else{
           md.getControllerData().setSubFlowCVASessionContext(subflowName,sessionContext);
       }
    }



    public  QueryIntentResponse detectIntentTextWithParam(String text, String intentName, String paramName, String language) throws Exception{

        DetectIntentRequest detectIntentRequest = new DetectIntentRequest();
        detectIntentRequest.setAgentId(mmSessionContext.getAgentId());
        detectIntentRequest.setText(text);

        Intent intent = null;
        QueryIntentResponse queryIntentResponse =null;
        Set<String> allowedIntents =getAllowedIntents(intentName);
        if(null!=allowedIntents) {
            detectIntentRequest.setAllowedIntents(allowedIntents);
        }
        //param node
        if(StringUtils.isNotBlank(intentName) ){
            if (intentName.contains(".")) intentName = intentName.split("\\.")[1];
            if(mmSessionContext.getIntentMap().containsKey(intentName)){
                intent = mmSessionContext.getIntentMap().get(intentName);
                return  extractParameters(intent,text,paramName,language,detectIntentRequest);
            }else{
                intent = Intent.builder().intentName(intentName).build();
                mmSessionContext.getIntentMap().put(intentName,intent);
                return  extractParameters(intent,text,paramName,language,detectIntentRequest);
            }
        }


        DetectIntentResponse detectIntentResponse=detectIntent(detectIntentRequest);
        queryIntentResponse = getQueryIntentResponse(detectIntentResponse);
        return queryIntentResponse;
        //return null;
    }

    public void setAllowedIntents(String allowedIntents){
        mmSessionContext.setAllowedIntents(allowedIntents);
    }

    public String getChangedIntent(){
        return mmSessionContext.getChangedIntent();
    }

    public Intent getIntent(String intentName){
        return mmSessionContext.getIntent(intentName);
    }



    private QueryIntentResponse getQueryIntentResponse(DetectIntentResponse detectIntentResponse) throws Exception{


        QueryIntentResponse queryIntentResponse =QueryIntentResponse.builder().build();
        if(null!=detectIntentResponse){
            if(null!=detectIntentResponse.getResult().getIntent()){
                Intent intent = Intent.builder().intentName(detectIntentResponse.getResult().getIntent()).intentText(detectIntentResponse.getResult().getText()).build();
                String intentDisplayName;

                intentDisplayName = detectIntentResponse.getResult().getIntent();
                processParameters1(intent,detectIntentResponse,null);
                if(null!=detectIntentResponse.getResult() && null!=detectIntentResponse.getResult().getEntities() &&  detectIntentResponse.getResult().getEntities().size()>0){
                    intent.setCompleted(false);
                }else{
                    intent.setCompleted(true);
                }

                intent.setQueryResult(getJson(detectIntentResponse));
                mmSessionContext.addIntent(intentDisplayName,intent);
                queryIntentResponse.setIntent(intentDisplayName);
                queryIntentResponse.setStatusCode(StatusCode.INTENT_MATCH);
                mmSessionContext.setTranscribedText(null);
            }

        }

        return queryIntentResponse;
    }

    public String getJson(Object object){
        if(null!=object){
            Gson gson = new Gson();
            return gson.toJson(object);
        }

        return null;
    }

    public Parameter processParameters1(Intent intent, DetectIntentResponse detectIntentResponse, String paramKey) throws Exception{
        if(null==intent.getParameters()){
            intent.setParameters(new HashMap<String, Parameter>());
        }
        if(null!=detectIntentResponse && null!=detectIntentResponse.getResult().getEntities()){
            List<Entity> entities = detectIntentResponse.getResult().getEntities();
            String originalValue = null,jsonValue = null;
            for(Entity entity:entities){
                Parameter param;
                if(intent.getParameters().containsKey(entity.getType()) && null!=intent.getParameters().get(entity.getType())){
                    param = intent.getParameters().get(entity.getType());
                }else if (intent.getParameters().containsKey(entity.getRole()) &&  null!=intent.getParameters().get(entity.getRole())){
                    param = intent.getParameters().get(entity.getRole());
                } else{
                    if(StringUtils.isNotBlank(entity.getRole())){
                        param = Parameter.builder().paramName(entity.getRole()).build();
                    }else{
                        param = Parameter.builder().paramName(entity.getType()).build();
                    }

                }
                originalValue  = entity.getText();

                if(StringUtils.isNotBlank(originalValue)) {
                    param.setParamValue(originalValue);
                    param.setParamObject(originalValue);
                    if(StringUtils.isNotBlank(entity.getRole())){
                        intent.getParameters().put(entity.getRole(), param);
                    }else{
                        intent.getParameters().put(entity.getType(), param);
                    }

                }

            }

            if(StringUtils.isNotBlank(paramKey) && !intent.getParameters().containsKey(paramKey)){
                Parameter param = Parameter.builder().paramName(paramKey).build();
                param.setParamValue(paramKey);
                param.setParamValue(null);
                param.setParamObject(null);
                intent.getParameters().put(paramKey, param);
            }
        }

        if(null!=paramKey) {
            return intent.getParameters().get(paramKey);
        }else{
            return null;
        }
    }

    public  QueryIntentResponse extractParameters(Intent intent, String text, String paramName,String language,DetectIntentRequest detectIntentRequest) throws Exception{
        QueryIntentResponse queryIntentResponse = QueryIntentResponse.builder().build();
        DetectIntentResponse detectIntentResponse= detectIntent(detectIntentRequest);


        if(null!=detectIntentResponse && null!= detectIntentResponse.getResult()){
            //queryIntentResponse.setQueryResult(JsonFormat.printer().print(result));
            if(intent.getIntentName().equals(detectIntentResponse.getResult().getIntent())){
                intent.setQueryResult( getJson(detectIntentResponse));
                Parameter parameter = processParameters1(intent,detectIntentResponse,paramName);
                queryIntentResponse.setIntent(intent.getIntentName());
                if( null!=parameter && StringUtils.isNotBlank(parameter.getParamValue())){
                    queryIntentResponse.setStatusCode(StatusCode.VARIABLE_FILLED);
                    queryIntentResponse.setOriginalValue(parameter.getParamValue());
                    queryIntentResponse.setValue(parameter.getParamObject());
                   setChangedIntent(null);
                }else{
                    queryIntentResponse.setStatusCode(StatusCode.NO_MATCH);
                    setChangedIntent(null);
                }
                mmSessionContext.updateElementData(intent);
            }else{
                queryIntentResponse.setStatusCode(StatusCode.INTENT_CHANGED);
                //push new intent to stack
                if(StringUtils.isNotBlank(detectIntentResponse.getResult().getIntent())){
                    queryIntentResponse.setIntent(detectIntentResponse.getResult().getIntent());
                   setChangedIntent(detectIntentResponse.getResult().getIntent());
                    Intent newIntent = null;
                    Intent changedIntent = mmSessionContext.getIntent(detectIntentResponse.getResult().getIntent());
                    newIntent =  Intent.builder().intentName(detectIntentResponse.getResult().getIntent()).intentText(detectIntentResponse.getResult().getText())
                            .build();
                    if(null==changedIntent ||  changedIntent.isCompleted()){
                        newIntent.setQueryResult(getJson(detectIntentResponse));
                        mmSessionContext.addIntent(detectIntentResponse.getResult().getIntent(),newIntent);
                    }else{
                        //update parameters here if there are any: partial update on intent change
                        processParameters1(newIntent,detectIntentResponse,null);
                        newIntent.updateParameters(newIntent.getParameters(),changedIntent.getParameters());
                        newIntent.clearParameters();
                    }

                }
                if(StringUtils.isNotBlank(text)){
                    mmSessionContext.setTranscribedText(text);
                }
            }
        }else{
            queryIntentResponse.setStatusCode(StatusCode.UNKNOWN_INTENT);
        }

        return queryIntentResponse;
    }

    private  Set<String> getAllowedIntents(String intent){

        Set<String> allowedIntentList = null;
        if(StringUtils.isBlank(mmSessionContext.getAllowedIntents())){
            allowedIntentList = new HashSet<>();
            if(StringUtils.isNotBlank(intent))  allowedIntentList.add(intent);
            return allowedIntentList;
        }else{
            if(mmSessionContext.getAllowedIntents().contains(",")){
                allowedIntentList = new HashSet(Arrays.asList(mmSessionContext.getAllowedIntents().split(",")));
            }else{
                allowedIntentList = new HashSet<>();
                allowedIntentList.add(mmSessionContext.getAllowedIntents());
            }

            if(StringUtils.isNotBlank(intent)){
                allowedIntentList.add(intent);
            }
        }

        return allowedIntentList;
    }

    public  void setChangedIntent(String intent) {
        mmSessionContext.setChangedIntent(intent);
    }


    //https://mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
    private  DetectIntentResponse detectIntent(DetectIntentRequest detectIntentRequest) throws Exception{
        DetectIntentResponse detectIntentResponse = null;

        try {
            if(turnOnLogging) {
                md.setElementData("API Request", MMUtil.toJson(detectIntentRequest), ElementDataTypes.PD_STRING, true);
            }
            WxmRestClient wxmRestClient = WxmRestClient.getInstance();
            AgentAssistToken cloudConnectAuthToken = wxmRestClient.getCloudConnectAuthToken();

            URL url = new URL(mmSessionContext.getBotLiteUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent","CVP-VXML");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization","Bearer " + cloudConnectAuthToken.getAuthToken());

            String input = new Gson().toJson(detectIntentRequest);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                sb.append(output+"\n");
            }
            if(turnOnLogging) {
                md.setElementData("API Response", sb.toString(), ElementDataTypes.PD_STRING, true);
            }
            detectIntentResponse = new Gson().fromJson(sb.toString(), DetectIntentResponse.class);
            conn.disconnect();

        } catch (MalformedURLException e) {

            SvcMgrUtil.getSvcMgr().info(CVPLogMessages.VXML_SERVER_ERROR, "Malformed URL Exception",e);

        } catch (IOException e) {

            SvcMgrUtil.getSvcMgr().info(CVPLogMessages.VXML_SERVER_ERROR, "IO Exception",e);

        }

        return detectIntentResponse;
    }
}