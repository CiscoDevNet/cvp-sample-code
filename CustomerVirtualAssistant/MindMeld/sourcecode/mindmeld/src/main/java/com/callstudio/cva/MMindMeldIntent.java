package com.callstudio.cva;

import com.audium.core.vfc.VException;
import com.audium.core.vfc.VPreference;
import com.audium.core.vfc.form.VBuiltInField;
import com.audium.core.vfc.form.VForm;
import com.audium.core.vfc.util.VAction;
import com.audium.core.vfc.util.VGrammar;
import com.audium.core.vfc.util.VMain;
import com.audium.core.vfc.util.VProperty;
import com.audium.core.voicebrowser.VGrammarDialogFlowV21;
import com.audium.server.contextManager.*;
import com.audium.server.session.VoiceElementData;
import com.audium.server.voiceElement.*;
import com.audium.server.voiceElement.util.NoInputPromptWithCounters;
import com.audium.server.voiceElement.util.NoMatchPrompt;
import com.audium.server.voiceElement.util.VoiceElementUtil;
import com.audium.server.xml.VoiceElementConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Hashtable;

import static com.callstudio.cva.Constants.*;

public class MMindMeldIntent  extends VoiceElementBase implements AudiumElement {

    // FORM NAME
    protected static final String FORM_NAME = "formMainForm";

    private static final String SUBMIT_MAIN_FIELD_NAME_SCRATCH = "the_main_field_name";

    private static final String ELEMENT_DATA_CREATED = "element_data_created";


    /**
     * Get The displayFolder in callstudio
     */
    public String getDisplayFolderName() {
        return AudiumConstants.CVA_ELEMENT_FOLDER;
    }

    public String getDescription() {
        return "The voice element collects the natural language user input and stream it to Speech to Text engine and get the user input " +
                "text and send it to Botlite and finds user intent from it.";

    }


    public String getElementName() {

        return "MindMeld";
    }


    public ElementData[] getElementData() throws ElementException {

        ElementData[] elementDataArray = new ElementData[4];
        elementDataArray[0] = new ElementData(ED_INTENT, "Action on the Caller Intent.");
        elementDataArray[1] = new ElementData(ED_INPUT_TYPE, "type of the input DTMF or voice");
        elementDataArray[2] = new ElementData(ED_JSON, "contains raw json response from Botlite.");
        elementDataArray[3] = new ElementData(ED_VALUE, "intent from Botlite.");

        return elementDataArray;
    }


    /**
     * GetVXMLBody
     */
    @SuppressWarnings("all")
    public String addXmlBody(VMain vxml, Hashtable reqParameters, VoiceElementData md) throws VException, ElementException {


        VoiceElementConfig config = md.getVoiceElementConfig();
        VPreference pref = md.getPreference();
        md.getControllerData().countDfLicense();
        md.removeAllElementData();
        md.removeAllScratchData();

        String dfResponseFiledName = md.getCurrentVoiceElement().concat("_fld");
        String completeNBestStr = (String) reqParameters.get("completeNBestStr");

        String dfResponseField = (String) reqParameters.get(dfResponseFiledName);
        String browserName = pref.getBrowserType();

        String audiumVxmlLog = (String)reqParameters.get("audium_vxmlLog");

        // NOINPUT TIMEOUT
        VProperty prop = vxml.getProperties();
        String timeout = config.getSettingValue("noinput_timeout", md);
        if (timeout == null) {
            prop.add(VProperty.NOINPUT_TIMEOUT, AudiumConstants.DEFAULT_NOINPUT_TIMEOUT);
        } else {
            prop.add(VProperty.NOINPUT_TIMEOUT, timeout);
        }

        //	adds the VoiceGenie specific property "com.voicegenie.fieldobject"
        MiscVoiceElementUtil.setBrowserProperties(pref.getBrowserType(), prop);
        //add the document-scoped VoiceXML variables noinput, and nomatch counts.
        VoiceElementUtil.declareCollectionNomatchNoInputVariables(vxml);

        String elementDataCreated = md.getElementData(md.getCurrentElement(), ELEMENT_DATA_CREATED);
        if (elementDataCreated == null) {
            elementDataCreated = "";
        }

        //get the noinput and nomatch counters, set counters value as Element Data and log them.
        String collect_noinput_count = (String) reqParameters.get(VoiceElementUtil.COLLECTION_NOINPUT_COUNT);
        String collect_nomatch_count = (String) reqParameters.get(VoiceElementUtil.COLLECTION_NOMATCH_COUNT);

        if (collect_noinput_count != null) {
            md.setElementData("collect_noinput_count", collect_noinput_count, VoiceElementData.PD_INT, true);
            elementDataCreated += "collect_noinput_count,";
        }
        if (collect_nomatch_count != null) {
            md.setElementData("collect_nomatch_count", collect_nomatch_count, VoiceElementData.PD_INT, true);
            elementDataCreated += "collect_nomatch_count,";
        }
        if (reqParameters.get("maxNoMatch") != null) {
            // Store the element data created by this element in a single element data value.

            md.setElementData(ELEMENT_DATA_CREATED, elementDataCreated, VoiceElementData.PD_STRING, false);
            return "max_nomatch";
        }

        //returns the max noinput state
        if (reqParameters.get("maxNoInput") != null) {

            // Store the element data created by this element in a single element data value.
            md.setElementData(ELEMENT_DATA_CREATED, elementDataCreated, VoiceElementData.PD_STRING, false);
            return "max_noinput";
        }

        //get the configuration to log sensitive data or not (value, result, slot elements, nbestUtterance, nbestInterpretation)
        //if secureLogging, then do not log the sensitive element data.
        boolean secureLogging = config.getBooleanSettingValue("secure_logging", false, md);
        boolean turnOnLogging = true;
        if (secureLogging) {
            turnOnLogging = false;
        }

        String intentSetting = config.getSettingValue("Intent", md);
        String variable = config.getSettingValue("variable", md);
        String maxNoMatchCountString = config.getSettingValue("max_nomatch_count", md);
        int maxNoMatchCount = Integer.parseInt(maxNoMatchCountString);
        String lastNode = config.getSettingValue("last_node", md);
        String allowedIntents = config.getSettingValue("Intent_Allowed", md);
        String projectId = config.getSettingValue("project_id", md);
        String botLiteUrl = config.getSettingValue("botlite_url", md);

       // MMSessionContext sessionContext = null;
        MMService mmService = new MMService(md,turnOnLogging);

        try {

             md.setElementData("subflowname", md.getCurrentSubflow(), VoiceElementData.PD_STRING, true);
             mmService.createSessionContext(md.getSessionId(), projectId,botLiteUrl,md.getCurrentSubflow());

            if(StringUtils.isNotBlank(allowedIntents)){
                mmService.setAllowedIntents(allowedIntents);
            }else{
                mmService.setAllowedIntents(null);
            }
            //conditions at root node
            //new logic do nothing plz do transcribe at root
            if(StringUtils.isBlank(intentSetting)) {
                if (StringUtils.isNotBlank(mmService.getChangedIntent())) {
                    Intent changedIntent = mmService.getIntent(mmService.getChangedIntent());
                    populateElementData(null, null, changedIntent, md, turnOnLogging);
                    mmService.setChangedIntent(null); //only one time

                    return new ExitState(ExitState.DONE).getRealName();
                }
            }else{ //condition of param node
                Intent currentIntent = mmService.getIntent(intentSetting);

                currentIntent.updateElementName(variable,md.getCurrentSubflow(),md.getCurrentElement());
                if (currentIntent.checkIfParameterExistsInIntent(variable)) {
                    populateElementData(variable,  null, currentIntent, md,turnOnLogging);
                    //if this field is set then it is expecting confirmation.
                    if("true".equals(lastNode)){
                        currentIntent.setCompleted(true);
                    }
                    return new ExitState(ExitState.DONE).getRealName();
                }

            }
            //conditon2 --> continue to vvb for transcribe

        } catch (Exception ex) {
            throw new ElementException(ex);
        }



        if (StringUtils.isBlank(dfResponseField) && StringUtils.isBlank(completeNBestStr)) {
            return submitVxml(vxml,reqParameters,md);

        } else {

            if(StringUtils.isBlank(intentSetting)){ //root mode

                Pair<String, String> textAndLanguage = MMUtil.parseTranscriptionValue(completeNBestStr);
                String transcribedText = textAndLanguage.getLeft();
                String languageToUse = MMUtil.getLanguageForDetectIntent(md, textAndLanguage.getRight());
                String inputType = MMUtil.parseInputType(audiumVxmlLog);
                String exitState = new ExitState(ExitState.MAX_NOMATCH).getRealName();

                try {
                    QueryIntentResponse queryIntentResponse;
                    //step1: detect intent

                    if (AudiumConstants.DTMF.equalsIgnoreCase(inputType)) {
                       mmService.detectIntentTextWithParam(transcribedText, null, null, languageToUse);
                        populateElementData(inputType, transcribedText, null, md, turnOnLogging);
                        return new ExitState(ExitState.DONE).getRealName();
                    }
                    queryIntentResponse = mmService.detectIntentTextWithParam(transcribedText, null, null, languageToUse);
                    exitState = returnExitStates(queryIntentResponse);
                    Intent intent = mmService.getIntent(queryIntentResponse.getIntent());
                    populateElementData(null, inputType, intent, md, turnOnLogging);
                } catch (Exception ex) {
                    throw new ElementException(ex);
                }

                return exitState;
            }else{ //param node
                //indidual elements check if params is filled by transcribed text
                Pair<String, String> textAndLanguage = MMUtil.parseTranscriptionValue(completeNBestStr);
                String transcribedText = textAndLanguage.getLeft();
                String languageToUse = MMUtil.getLanguageForDetectIntent(md, null);// take document language
                String inputType = MMUtil.parseInputType(audiumVxmlLog);
                String exitState = new ExitState(ExitState.MAX_NOMATCH).getRealName();

                try {
                    QueryIntentResponse queryIntentResponse;
                    //step1: detect intent
                    if (StringUtils.isNotBlank(intentSetting) && StringUtils.isNotBlank(variable) && AudiumConstants.VOICE.equalsIgnoreCase(inputType)) {
                        Intent currentIntent = mmService.getIntent(intentSetting);
                        if (null != currentIntent) {
                            queryIntentResponse = mmService.detectIntentTextWithParam(transcribedText, intentSetting, variable, languageToUse);
                            if (queryIntentResponse.getStatusCode() == StatusCode.NO_MATCH) {
                                int noMatch = 0;
                                if(null!=currentIntent.getParameters() && currentIntent.getParameters().containsKey(variable)){
                                    noMatch = currentIntent.getParameters().get(variable).getNoMatchCount();
                                    if (noMatch < maxNoMatchCount) {
                                        noMatch = currentIntent.getParameters().get(variable).getNoMatchCount() + 1;
                                        currentIntent.getParameters().get(variable).setNoMatchCount(noMatch);
                                        submitVxml(vxml, reqParameters, md);
                                        return null;
                                    } else {
                                        exitState = returnExitStates(queryIntentResponse);
                                    }
                                }

                            }
                            currentIntent.setCompleted(false);
                            if(queryIntentResponse.getStatusCode() ==StatusCode.VARIABLE_FILLED && "true".equals(lastNode)){
                                //if this field is set then it is expecting confirmation.
                                currentIntent.setCompleted(true);
                            }
                            if(queryIntentResponse.getStatusCode() == StatusCode.INTENT_CHANGED){
                                populateElementData(variable,inputType, mmService.getIntent(mmService.getChangedIntent()), md,turnOnLogging);
                            }else {
                                populateElementData(variable, inputType, currentIntent, md, turnOnLogging);
                            }
                            exitState = returnExitStates(queryIntentResponse);
                        } else {
                            return new ExitState(ExitState.DONE).getRealName();
                        }

                    }else if (StringUtils.isNotBlank(intentSetting) && StringUtils.isNotBlank(variable) && AudiumConstants.DTMF.equalsIgnoreCase(inputType)){
                        mmService.detectIntentTextWithParam(transcribedText, intentSetting, variable, languageToUse);
                        Intent currentIntent = mmService.getIntent(intentSetting);
                        currentIntent.setCompleted(false);
                        if(null!=currentIntent){
                            if(null!=currentIntent.getParameters() && currentIntent.getParameters().containsKey(variable)){
                                currentIntent.getParameters().get(variable).setParamValue(transcribedText);
                                currentIntent.getParameters().get(variable).setParamObject(transcribedText);
                            }
                            if("true".equals(lastNode)){
                                currentIntent.setCompleted(true);
                            }
                        }

                        populateElementData( variable,inputType, currentIntent, md,turnOnLogging);
                        return new ExitState(ExitState.DONE).getRealName();
                    }

                } catch (Exception ex) {
                    throw new ElementException(ex);
                }

                return exitState;
            }

        }

    }


    private void populateElementData(String variable, String inputType,
                                     Intent currentIntent, VoiceElementData md,boolean turnOnLogging) throws ElementException {

        if (null!=currentIntent) {


            md.setElementData(ED_INTENT, currentIntent.getIntentName(), VoiceElementData.PD_STRING, true);


            if (null != currentIntent.getParameters()) {
                Parameter parameter = currentIntent.getParameters().get(variable);
                if (null != parameter) {
                    if (StringUtils.isNotBlank(parameter.getParamValue())) {
                        md.setElementData(ED_VALUE, parameter.getParamValue(), VoiceElementData.PD_STRING, turnOnLogging);
                        MMUtil.secureLog(ED_VALUE,!turnOnLogging,md);
                    }


                }
            }

            if (StringUtils.isNotBlank( currentIntent.getQueryResult())) {
                md.setElementData(ED_JSON, currentIntent.getQueryResult(), VoiceElementData.PD_STRING, turnOnLogging);
                MMUtil.secureLog(ED_JSON,!turnOnLogging,md);
            }

        }



        if (StringUtils.isNotBlank(inputType)) {
            md.setElementData(ED_INPUT_TYPE, inputType, VoiceElementData.PD_STRING, true);
        }

    }


    @SuppressWarnings("all")
    private String submitVxml(VMain vxml, Hashtable reqParameters, VoiceElementData md) throws VException, ElementException {

        VoiceElementConfig config = md.getVoiceElementConfig();
        VPreference pref = md.getPreference();
        md.removeAllElementData();
        md.removeAllScratchData();

        String dfResponseFiledName = md.getCurrentVoiceElement().concat("_fld");
        String completeNBestStr = (String) reqParameters.get("completeNBestStr");

        String dfResponseField = (String) reqParameters.get(dfResponseFiledName);
        String browserName = pref.getBrowserType();

        // NOINPUT TIMEOUT
        VProperty prop = vxml.getProperties();
        String timeout = config.getSettingValue("noinput_timeout", md);
        if (timeout == null) {
            prop.add(VProperty.NOINPUT_TIMEOUT, AudiumConstants.DEFAULT_NOINPUT_TIMEOUT);
        } else {
            prop.add(VProperty.NOINPUT_TIMEOUT, timeout);
        }

        //	adds the VoiceGenie specific property "com.voicegenie.fieldobject"
        MiscVoiceElementUtil.setBrowserProperties(pref.getBrowserType(), prop);
        //add the document-scoped VoiceXML variables noinput, and nomatch counts.
        VoiceElementUtil.declareCollectionNomatchNoInputVariables(vxml);

        String elementDataCreated = md.getElementData(md.getCurrentElement(), ELEMENT_DATA_CREATED);
        if (elementDataCreated == null) {
            elementDataCreated = "";
        }

        //get the noinput and nomatch counters, set counters value as Element Data and log them.
        String collect_noinput_count = (String) reqParameters.get(VoiceElementUtil.COLLECTION_NOINPUT_COUNT);
        String collect_nomatch_count = (String) reqParameters.get(VoiceElementUtil.COLLECTION_NOMATCH_COUNT);

        if (collect_noinput_count != null) {
            md.setElementData("collect_noinput_count", collect_noinput_count, VoiceElementData.PD_INT, true);
            elementDataCreated += "collect_noinput_count,";
        }
        if (collect_nomatch_count != null) {
            md.setElementData("collect_nomatch_count", collect_nomatch_count, VoiceElementData.PD_INT, true);
            elementDataCreated += "collect_nomatch_count,";
        }
        if (reqParameters.get("maxNoMatch") != null) {
            // Store the element data created by this element in a single element data value.

            md.setElementData(ELEMENT_DATA_CREATED, elementDataCreated, VoiceElementData.PD_STRING, false);
            return "max_nomatch";
        }

        //returns the max noinput state
        if (reqParameters.get("maxNoInput") != null) {

            // Store the element data created by this element in a single element data value.
            md.setElementData(ELEMENT_DATA_CREATED, elementDataCreated, VoiceElementData.PD_STRING, false);
            return "max_noinput";
        }

        String maxNoMatchCountString = config.getSettingValue("max_nomatch_count", md);
        String projectId = config.getSettingValue("project_id", md);
        boolean secureLogging = config.getBooleanSettingValue("secure_logging", false, md);


        //Right before we enter the element, we have to wipe clean all the element data created
        // by previous visites to this exact element otherwise the data might get merged and
        // we don't want that. We do this by looking at the value of a special element data
        // value that contains the names of all element data previously created (comma delimited)
        // and systematically deletes each one.
        if (elementDataCreated != null && !elementDataCreated.equals("")) {
            MMUtil.removePriorElementData(md, elementDataCreated);
            elementDataCreated = "";
        }

        // Start of the vxml form
        VForm form = VForm.getNew(pref, "start");
        if (form.getUniqueFormNameFormat() != null) {
            form.setName(FORM_NAME);
        }

        VBuiltInField field = null;
        String inputmode = config.getSettingValue("inputmode", md);
        VProperty property = null;
        int inputModeVar = VBuiltInField.DTMF_SPEECH;

        // adds the confidence setting to the properties in the generated VXML page.
        VoiceElementUtil.addConfidenceProperty(vxml, inputmode, config.getSettingValue("confidence_level", md),
                AudiumConstants.DEFAULT_CONFIDENCE);

        if (AudiumConstants.VOICE.equalsIgnoreCase(inputmode)) { //only voice
            property = VProperty.getNew(pref, VProperty.INPUT_MODES_ALLOWED, AudiumConstants.VOICE);
            field = VBuiltInField.getNew(pref, VBuiltInField.GRAMMAR, "PLACEHOLDER", VBuiltInField.SPEECH);
            inputModeVar = VBuiltInField.SPEECH;

        } else {
            property = VProperty.getNew(pref, VProperty.INPUT_MODES_ALLOWED, "dtmf voice");
            field = VBuiltInField.getNew(pref, VBuiltInField.GRAMMAR, "PLACEHOLDER", VBuiltInField.DTMF_SPEECH);
            inputModeVar = VBuiltInField.DTMF_SPEECH;
        }


        field.setName(dfResponseFiledName);
        field.setUniqueFormItemNameFormat(null);

        field.setParentFormName(form.getCompleteFormName());

        String completeFieldName = field.getCompleteFormItemName();


        //add secure logging
        VProperty fieldProp = null;
        fieldProp = field.getProperties();
        if (fieldProp == null) {
            fieldProp = VProperty.getNew(pref, AudiumConstants.PROPERTY_SECURE_LOGGING,
                    secureLogging ? "true" : "false");
            field.setProperties(fieldProp);
        } else {
            fieldProp.add(AudiumConstants.PROPERTY_SECURE_LOGGING, secureLogging ? "true"
                    : "false");
        }
        //add phrase hints
        String phraseHints = config.getSettingValue("phrase_hints", md);
        if(StringUtils.isNotBlank(phraseHints)) {
            fieldProp.add(AudiumConstants.PROPERTY_PHRASE_HINTS, phraseHints);
        }

        //add alternate Languages
        String alternateLanguages = config.getSettingValue("alternate_languages",md);
        if(StringUtils.isNotBlank(alternateLanguages)) {
            fieldProp.add(AudiumConstants.PROPERTY_ALTERNATE_LANGUAGES, alternateLanguages);
        }

        //project_id
        if (StringUtils.isNotBlank(projectId)) {
            fieldProp.add(AudiumConstants.PROPERTY_PROJECT_ID, projectId);
        }


        //termchar
        String termChar = config.getSettingValue(AudiumConstants.TERM_CHAR_SETTING,md);
        if(StringUtils.isNotBlank(termChar)){
            fieldProp.add(AudiumConstants.TERM_CHAR, termChar);
        }

        //maxtime
        String maxInput = config.getSettingValue(Setting.MAX_INPUT_KEY,md);
        if(StringUtils.isNotBlank(maxInput)){
            fieldProp.add(AudiumConstants.MAX_TIME,maxInput);
        }

        //final silence
        String finalSilence = config.getSettingValue(Setting.FINAL_SILENCE_KEY,md);
        if(StringUtils.isNotBlank(finalSilence)){
            fieldProp.add(AudiumConstants.FINAL_SILENCE,finalSilence);
        }


        //output_as_audio


        fieldProp.add(AudiumConstants.PROPERTY_OUTPUT_AUDIO_SUPPORT, "false");


        // Maxnbest
        if (!(inputModeVar == VBuiltInField.DTMF_SPEECH)) {
            int maxNBest = 1;

            MiscVoiceElementUtil.setFormProperty(pref.getBrowserType(), prop, Integer.toString(maxNBest));
        }
        form.setProperties(property);

        // external voice grammar
        String asr[] = null;
        if ((inputModeVar == VBuiltInField.DTMF_SPEECH) || (inputModeVar == VBuiltInField.SPEECH)) {
            asr = new String[]{AudiumConstants.GRAMMAR_TRANSCRIBE};
        }


        // external DTMF grammar
        VGrammar gram = new VGrammarDialogFlowV21();
        //VGrammar gram = VGrammar.getNew(pref);
        if (md.getDocumentLanguage() != null) {
            gram.setLanguage(md.getDocumentLanguage());
        }
        gram.setMediaType("application/grammar+regex");
        boolean hasInlineGrammar = false;
        boolean hasExternalGrammar = false;

        String slot = config.getSettingValue("slot_name", md);
        if (slot == null) slot = "dummy";


        //set The external VoiceGrammer
        boolean hasExternalVoiceGrammar = setExternalVoiceGrammar(asr, md, vxml, field);

// external DTMF grammar
        String dtmf[] = config.getSettingValues("dtmf_grammar", md);

        boolean hasExternalDtmfGrammar = MMUtil.setExternalDtmfGrammar(dtmf, md, field, pref);


        hasExternalGrammar = hasExternalVoiceGrammar && hasExternalDtmfGrammar;
        if ((inputModeVar == VBuiltInField.DTMF_SPEECH)) {
            if (!hasExternalGrammar) {
                throw new ElementException("At least one grammar setting should be configured for " + md.getCurrentElement());
            }
        }
        field.setGrammar(gram);
        if (slot != null) {
            field.setSlot(slot);
        }


        VoiceElementConfig.AudioGroup initialPrompt = config.getAudioGroup("initial_audio_group", 1);
        if (initialPrompt != null) {

            field.setPromptCount(1, initialPrompt.constructAudio(md));
        }


        //NOINPUT
        NoInputPromptWithCounters noinput = new NoInputPromptWithCounters(md, "noinput_audio_group", "max_noinput_count",
                getSubmitURL(), AudiumConstants.DEFAULT_MAX_NOINPUT_COUNT_INT);
        field.add(noinput.getEvent());

        //required to handle dtmf no match
        if((inputModeVar == VBuiltInField.DTMF_SPEECH )){
            //NOMATCH
            NoMatchPrompt nomatch = new NoMatchPrompt(md, "nomatch_audio_group", "max_nomatch_count",
                    getSubmitURL(), AudiumConstants.DEFAULT_MAX_NOMATCH_COUNT_INT);
            field.add(nomatch.getEvent());
        }



        //adds additional block and field for done prompt (this allows bargein during the done prompt.
        VAction finalAction = VAction.getNew(pref, VAction.VARIABLE, "completeNBestStr", "", VAction.WITH_QUOTES);
        if (browserName.equals("osb_osr_01") || browserName.equals("nortel_osr_01")) {
            finalAction.add(VAction.getNew(pref, VAction.VARIABLE, "multipleSlotStr", "", VAction.WITH_QUOTES));
        }

        //If secureLogging, then suppress logging the actual shadow variable for the utterance & interpretation.
        //Instead, will log utterance_secureLogging and interpretation_secureLogging, both with the value of "*****".
        finalAction.add(VoiceElementUtil.getUtteranceInteraction(completeFieldName + "$.utterance",
                completeFieldName + "$.inputmode",
                completeFieldName,
                completeFieldName + "$.confidence",
                pref,
                secureLogging));


        finalAction.add(VAction.getNew(pref, VAction.SCRIPT, formScript()));

        finalAction.add(VAction.getNew(pref, VAction.VARIABLE, "confidence", completeFieldName + "$.confidence", VAction.WITHOUT_QUOTES));


        if (browserName.equals("osb_osr_01") || browserName.equals("nortel_osr_01")) {
            finalAction.add(getSubmitURL(), VXML_LOG_VARIABLE_NAME + " completeNBestStr multipleSlotStr confidence "
                    + VoiceElementUtil.COLLECTION_NOINPUT_COUNT + " "
                    + VoiceElementUtil.COLLECTION_NOMATCH_COUNT);
        } else {
            finalAction.add(getSubmitURL(), VXML_LOG_VARIABLE_NAME + " completeNBestStr confidence "
                    + VoiceElementUtil.COLLECTION_NOINPUT_COUNT + " "
                    + VoiceElementUtil.COLLECTION_NOMATCH_COUNT /*+ " " + VoiceElementUtil.UTTERANCE_FILENAME*/);
        }

        field.add(finalAction);

        // Logs initial prompt
        if (initialPrompt != null) {
            form.add(VoiceElementUtil.getInitialAudioGroupLogBlock(pref, null, "initial_audio_group", completeFieldName));
        }
        form.add(field);
        vxml.add(form);

        //set the element data elementDataCreated
        md.setElementData(ELEMENT_DATA_CREATED, elementDataCreated, VoiceElementData.PD_STRING, false);

        md.setScratchData(SUBMIT_MAIN_FIELD_NAME_SCRATCH, completeFieldName);
        return null;

    }

    private String returnExitStates(QueryIntentResponse queryIntentResponse) throws Exception {
        String exitState = new ExitState(ExitState.DONE).getRealName();
        if (null != queryIntentResponse) {
            switch (queryIntentResponse.getStatusCode()) {
                case NO_MATCH:
                    exitState = new ExitState(ExitState.MAX_NOMATCH).getRealName();
                    break;
                case VARIABLE_FILLED:
                    exitState = new ExitState(ExitState.DONE).getRealName();
                    break;
                case UNKNOWN_INTENT:
                    exitState = new ExitState(ExitState.INTENT_CHANGE).getRealName();
                    break;
                case INTENT_MATCH:
                    exitState = new ExitState(ExitState.DONE).getRealName();
                    break;
                default:
                    exitState = new ExitState(ExitState.DONE).getRealName();
                    break;

            }
        }

        return exitState;
    }


    public String[] getAudioGroupDisplayOrder() {

        String[] displayOrder = new String[1];

        displayOrder[0] = "Audio Groups";


        return displayOrder;
    }

    /**
     * getAudioGroups
     */
    public HashMap getAudioGroups() throws ElementException {

        HashMap groups = new HashMap(1);

        AudioGroup[] audioGroupArray = new AudioGroup[3];
        audioGroupArray[0] = new AudioGroup("initial_audio_group",
                "Initial prompt",
                "This audio group will be played on entry into the voice element.",
                true,
                true);

        audioGroupArray[1] = new AudioGroup("noinput_audio_group", "NoInput Prompt",
                "This audio group will be played when the caller does not respond to the prompt to enter a value.",
                false,
                false);
        audioGroupArray[2] = new AudioGroup("nomatch_audio_group", "NoMatch Prompt",
                "This audio group will be played when the caller response does not match the field.",
                false,
                false);

        groups.put("Audio Groups", audioGroupArray);


        return groups;
    }

    public ExitState[] getExitStates() throws ElementException {
        ExitState[] exitStateArray = new ExitState[4];

        exitStateArray[0] = new ExitState(ExitState.DONE);
        exitStateArray[1] = new ExitState(ExitState.INTENT_CHANGE);

        exitStateArray[2] = new ExitState(ExitState.MAX_NOINPUT);

        exitStateArray[3] = new ExitState(ExitState.MAX_NOMATCH);

        return exitStateArray;
    }


    public Setting[] getSettings() throws ElementException {
        Setting[] settingArray = new Setting[17];

        settingArray[0] = new Setting("element_type",
                "Root Element",
                "Indicates if the element is Root (Intent) (true) or Paramater element (false). Default=true",
                true,
                true,
                true,
                Setting.BOOLEAN);
        settingArray[1] = new Setting("project_id",
                "Agent ID",
                "Agent ID of the bot",
                Setting.REQUIRED,
                true,
                true,
                Setting.STRING);
        settingArray[2] = new Setting(Setting.INPUT_MODE);
        settingArray[3] = new Setting(Setting.NOINPUT_TIMEOUT);
        settingArray[4] = new Setting("max_noinput_count",
                "Max NoInput Count",
                "The maximum number of noinput events allowed during input capture." +
                        NEW_LINE + "Possible values: int >= 0. 0 = infinite noinputs. Default = 3.",
                true,
                true,
                true,
                0,
                null);
        settingArray[5] = new Setting("max_nomatch_count",
                "Max NoMatch Count",
                "The maximum number of nomatch events allowed during input capture." +
                        NEW_LINE + "Possible values: int >= 0. 0 = infinite nomatches. Default = 3.",
                true,
                true,
                true,
                0,
                null);




        settingArray[6] = new Setting("dtmf_grammar",
                "DTMF Grammar",
                "External dtmf grammar regex.",
                true,
                true,
                true,
                Setting.STRING);


        settingArray[7] = new Setting(AudiumConstants.TERM_CHAR_SETTING,
                "Termination Character",
                "Specifies the character for which dtmf or voice input capture will be terminated. Default = #.",
                false,
                true,
                true,
                Setting.STRING);

        settingArray[8] = new Setting(Setting.MAX_INPUT);
        settingArray[9] = new Setting(Setting.FINAL_SILENCE);
        settingArray[10] = new Setting("Intent",
                "Intent",
                "Intent of the called. This is matched to intent name in the Botlite agent",
                true,
                true,
                false,
                Setting.STRING);
        settingArray[11] = new Setting("Intent_Allowed",
                "Allowed Intents",
                "Intents allowed",
                false,
                true,
                false,
                Setting.STRING);

        settingArray[12] = new Setting("variable",
                "Variable",
                "It is parameter or slot to be filled",
                true,
                true,
                false,
                Setting.STRING);
        settingArray[13] = new Setting("last_node",
                "Last Parameter",
                "Indicates last parameter.",
                false,
                true,
                true,
                new String[]{"true", "false"});
        settingArray[14] = new Setting("phrase_hints",
                "Phrase Hints",
                "Hints for recognition",
                false,
                true,
                true,
                Setting.STRING);
        settingArray[15] = new Setting("botlite_url",
                "Botlite url",
                "Botlite nlp url.",
                true,
                true,
                true,
                Setting.STRING);


        settingArray[16] = new Setting("secure_logging",
                "Secure Logging",
                "Whether or not to enable logging of potentially sensitive data of the Form element." +
                        NEW_LINE + "If set to true, the element's potentially sensitive data will not log. Default = false.",
                true,
                true,
                true,
                Setting.BOOLEAN);




        settingArray[0].setDefaultValue("true");
        settingArray[2].setDefaultValue(AudiumConstants.VOICE);
        settingArray[3].setDefaultValue(AudiumConstants.DEFAULT_NOINPUT_TIMEOUT);
        settingArray[4].setDefaultValue(AudiumConstants.DEFAULT_MAX_NOINPUT_COUNT);
        settingArray[5].setDefaultValue(AudiumConstants.DEFAULT_MAX_NOMATCH_COUNT);
        settingArray[7].setDefaultValue(AudiumConstants.DEFAULT_TERMINATION_CHAR);
        settingArray[8].setDefaultValue(AudiumConstants.DEFAULT_MAXINPUT);
        settingArray[9].setDefaultValue(AudiumConstants.DEFAULT_FINALSILENCE);
        settingArray[13].setDefaultValue("false");
        settingArray[16].setDefaultValue("false");

        Dependency depBoth = new Dependency(settingArray[2].getRealName(), AudiumConstants.DTMF_VOICE, Dependency.EQUAL);
        Dependency[] depArrayBoth = {depBoth};
        settingArray[6].setDependencies(depArrayBoth);

        //intent/param dependency
        Dependency depParam = new Dependency(settingArray[0].getRealName(), "false", Dependency.EQUAL);
        Dependency depIntent = new Dependency(settingArray[0].getRealName(), "true", Dependency.EQUAL);
        Dependency[] depParamBoth = {depParam};
        Dependency[] depIntentBoth = {depIntent};
        settingArray[10].setDependencies(depParamBoth);
        settingArray[12].setDependencies(depParamBoth);
        settingArray[13].setDependencies(depParamBoth);
        settingArray[1].setDependencies(depIntentBoth);
        settingArray[15].setDependencies(depIntentBoth);




        return settingArray;
    }

    /**
     *
     * @param asr
     * @param md
     * @param vxml
     * @param field
     * @return
     * @throws VException
     */
    private boolean setExternalVoiceGrammar(String[] asr, VoiceElementData md, VMain vxml, VBuiltInField field) throws ElementException,VException{

        boolean hasExternalGrammar=false;
        // add external voice grammar per language
        if(asr != null) {
            for(int i=0; i < asr.length; i++) {
                VGrammar gram2=new VGrammarDialogFlowV21();
                //VGrammar gram2 = VGrammar.getNew(pref);
                if (md.getDocumentLanguage() != null) {
                    gram2.setLanguage(md.getDocumentLanguage());
                }
                gram2.setVersion("1.0");
                String externalVoiceGrammar = asr[i].trim();
                String[] parts = externalVoiceGrammar.split(";");
                String lang1Part = null;
                String lang2Part = null;
                String weightPart = null;
                String typePart = null;
                String urlPart = null;
                if (parts.length == 1) {
                    //external voice grammar format = url
                    urlPart = trim(parts[0]);
                }
                else if (parts.length == 3) {
                    //external voice grammar format = weight;type;url
                    urlPart = trim(parts[2]);
                    typePart = trim(parts[1]);
                    weightPart = trim(parts[0]);
                }
                else if (parts.length == 5) {
                    //external voice grammar format = lang1;lang2;weight;type;url
                    urlPart = trim(parts[4]);
                    typePart = trim(parts[3]);
                    weightPart = trim(parts[2]);
                    lang2Part = trim(parts[1]);
                    lang1Part = trim(parts[0]);
                }
                else {
                    throw new ElementException("The \"Voice Grammar\" setting for " + md.getCurrentElement() +
                            " should be configured in the format \"[[language-context];[language-code];[weight];[type];]URL\".");
                }

                // set the external voice grammar source
                if(urlPart != null){
                    gram2.setSpeechSource(urlPart);
                } else {
                    throw new ElementException("The URL for a \"Voice Grammar\" setting for the element "
                            + md.getCurrentElement() + " is missing.");
                }

                // set the mime type for the external voice grammar
                if(typePart != null){
                    gram2.setMediaType(typePart);
                }

                // set the weight
                if(weightPart != null){
                    try {
                        float floatWeight = Float.parseFloat(weightPart);
                        gram2.setWeight(floatWeight);
                    } catch (NumberFormatException ne) {
                        throw new ElementException("\"Grammar Weight\" for a  \"Voice Grammar\" setting should be a float value for "
                                + md.getCurrentElement() + " voice element.");
                    }
                }

                //set the grammar xml:lang attribute
                gram2.setLanguage(lang2Part);

                // the same as the vxml language should be used
                if (hasSameLanguage(vxml, lang1Part)) {
                    field.setGrammar(gram2);
                    hasExternalGrammar = true;
                }
            }
        }
        return hasExternalGrammar;
    }

    private String formScript()
            throws VException, ElementException {
        return

                "\n" + "function recurse(interpretation)" + "\n"
                        + "{" + "\n\t"
                        + "var slot_string;" + "\n\t"

                        + "if (interpretation instanceof Object)" + "\n\t"
                        + "{" + "\n\t\t"
                        + "slot_string = '+';" + "\n\t\t"
                        + "var count = 0;" + "\n\t\t"
                        + "for (var attr in interpretation)" + "\n\t\t"
                        + "{" + "\n\t\t\t"
                        + "if (count++ > 0)" + "\n\t\t\t\t"
                        + "slot_string += '+';" + "\n\t\t\t"
                        + "slot_string += attr + ':' + recurse(interpretation[attr]);" + "\n\t\t"
                        + "}" + "\n\t"
                        //"slot_string += '}';" + "\n\t" +
                        + "} else {" + "\n\t\t"
                        + "slot_string = interpretation;" + "\n\t"
                        + "}" + "\n\t"
                        + "return slot_string;" + "\n"
                        + "}" + "\n"

                        + "var len = application.lastresult$.length;" + "\n"
                        + "for (var i = 0; i < len; i++)" + "\n"
                        + "{" + "\n\t"
                        + "var slot_string = recurse(application.lastresult$[i].interpretation);" + "\n\t"
                        + "completeNBestStr += \"|||\" + application.lastresult$[i].confidence + "
                        //+		   "\"^^^\" + application.lastresult$[i].utterance + "
                        + "\"^^^\" + application.lastresult$[i].inputmode + "
                        + "\"^^^\" + slot_string;" + "\n"
                        + "}";
    }

}