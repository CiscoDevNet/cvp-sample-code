package com.callstudio.cva;


import com.audium.server.contextManager.Intent;
import com.audium.server.contextManager.QueryIntentResponse;
import com.audium.server.contextManager.SessionContext;
import com.audium.server.contextManager.SessionContextAbstract;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.Map;

public class MMSessionContext extends SessionContextAbstract implements SessionContext {


    @Getter
    @Setter
    private String transcribedText;

    @Getter
    @Setter
    private String agentId;

    @Getter
    @Setter
    private String allowedIntents;

    @Getter
    private String botLiteUrl;

    public MMSessionContext(String projectId,String botLiteUrl) {
        this.agentId = projectId;
        this.botLiteUrl = botLiteUrl;
    }


    @Override
    public void init() throws Exception {
        this.setIntentMap(new LinkedMap<String, Intent>());
    }


    public void clearContext() {
        if(null!=getIntentMap()) {
            for (Map.Entry<String, Intent> entry : getIntentMap().entrySet()) {
                Intent intent = entry.getValue();
                if (null != intent) {
                    intent.clearParameters();
                }
            }
            getIntentMap().clear();
            this.setIntentMap( null);
        }
    }

    @Override
    public Intent getIntent(String intentName) {
        if (intentName.contains(".")) intentName = intentName.split("\\.")[1];
        return super.getIntent(intentName);
    }

    public QueryIntentResponse detectIntentTextWithParam(String text, String intentName, String paramName, String language) throws Exception {

        return null;
        //return null;
    }

    /**
     * adds intent to map
     * if there are in complete intent it flushes from map and inserts new intent.
     *
     * @param intentName
     * @param intent
     */
    public void addIntent(String intentName, Intent intent) {
        if (!this.getIntentMap().containsKey(intentName)) {
            this.getIntentMap().put(intentName, intent);
        } else {
            Intent mapIntent = this.getIntentMap().get(intentName);
            if (mapIntent.isCompleted()) {
                mapIntent.clearParameters();
                this.getIntentMap().remove(mapIntent);
                this.getIntentMap().put(intentName, intent);
            }

        }
    }

     }