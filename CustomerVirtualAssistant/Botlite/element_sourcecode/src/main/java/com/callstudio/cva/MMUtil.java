package com.callstudio.cva;

import com.audium.core.vfc.VException;
import com.audium.core.vfc.VPreference;
import com.audium.core.vfc.form.VBuiltInField;
import com.audium.core.vfc.util.VGrammar;
import com.audium.server.session.VoiceElementData;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.MiscVoiceElementUtil;
import com.audium.server.voiceElement.util.VoiceElementUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.StringTokenizer;

public class MMUtil {

    /**
     * transcription contains a json in the format {"transcript":"book a room","languagecode":"en-us"}
     * @param transcribedText
     * @return Pair -  Left:text, RightL: language code
     */
    protected static Pair<String, String> parseTranscriptionValue(String transcribedText){
        String value = getActualTranscribedText(transcribedText);
        String languageCode = "";
        if (isJson(value)){
            JsonObject jsonObject = new JsonParser().parse(value).getAsJsonObject();
            value = jsonObject.get("transcript").getAsString();
            languageCode = jsonObject.get("language_code").getAsString();
        }
        return Pair.of(value, languageCode);
    }

    /**
     * language obtained from transcription takes precedence, then document language,
     * otherwise defaults to en-US.
     * @param md
     * @param languageFromTranscribe
     * @return
     */
    protected static String getLanguageForDetectIntent(VoiceElementData md, String languageFromTranscribe) {
        return StringUtils.isNotBlank(languageFromTranscribe) ? languageFromTranscribe :
                StringUtils.isNotBlank(md.getDocumentLanguage()) ? md.getDocumentLanguage() :
                        AudiumConstants.DEFAULT_LANGUAGE_FIELD_NAME;
    }


    /**
     * parses input type from vvb response.
     * @param audiumVxmlLog
     * @return
     */
    protected static String parseInputType(String audiumVxmlLog){
        String inputType = "";
        try {
            StringTokenizer st2 = new StringTokenizer(audiumVxmlLog, "|||");
            while (st2.hasMoreTokens()) {
                String subST2 = st2.nextToken();
                if (subST2.contains("inputmode")) {
                    //inputmode$$$voice^^^9546
                    StringTokenizer subST3 = new StringTokenizer(subST2, "$$$");
                    subST3.nextToken();
                    String st21 = subST3.nextToken();
                    StringTokenizer subST22 = new StringTokenizer(st21, "^^^");
                    inputType = subST22.nextToken();
                    break;
                }

            }

        }catch (Exception ex){
            inputType = "";
        }
        return inputType;
    }

    protected static String toJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    protected static boolean setExternalDtmfGrammar(String[] dtmf, VoiceElementData md, VBuiltInField field, VPreference pref) throws ElementException, VException {

        boolean hasExternalGrammar = false;
        if (dtmf != null) {
            for (int j = 0; j < dtmf.length; j++) {
                VGrammar gram = VGrammar.getNew(pref);
                if (md.getDocumentLanguage() != null) {
                    gram.setLanguage(md.getDocumentLanguage());
                }
                String[] dtmfArray = new String[1];
                dtmfArray[0] = dtmf[j];
                gram.setDtmfInline(dtmfArray);
                MiscVoiceElementUtil.setDefaultMimeTypeForExternalDtmfGrammars(pref.getBrowserType(), gram);
                gram.setMediaType(AudiumConstants.MEDIA_TYPE_GRAMMAR);
                field.setGrammar(gram);
                hasExternalGrammar = true;
            }
        }

        return hasExternalGrammar;
    }


    /*
     * This method is used internally to remove all element data previously created by the same
     * element instance. This is necessary because with n-best lists, there may be different data
     * returned each time the element is visited and you do not want the element data created after
     * one visit to the element to affect the subsequent visits. Also, we want to make sure any
     * element data created by dynamic configurations and in the data tab in the Builder to remain
     * untouched. The solution to this is to keep track of all the element data created by the
     * element in a comma-delimited list, then remove only those element data in the list when the
     * element is visited again.
     */
    protected static void removePriorElementData(VoiceElementData ved, String elementDataNames) {
        StringTokenizer token = new StringTokenizer(elementDataNames, ",");
        try {
            while (token.hasMoreTokens()) {
                String name = token.nextToken();
                if (name != null) {
                    ved.setElementData(name, null);
                }
            }
            // We can safely ignore the exceptions because the name will not be null and the data
            // type will always be correct.
        } catch (ElementException ignore) {
        }
    }


    private static boolean isJson(String str){
        try {
            return new JsonParser().parse(str).isJsonObject();
        }catch(JsonSyntaxException j){
            return false;
        }
    }

    private static String getActualTranscribedText(String transcribedText) {
        String value = "";
        try {
            StringTokenizer st1 = new StringTokenizer(transcribedText, "^^^");
            if (st1.hasMoreTokens()) {
                st1.nextToken();
                st1.nextToken();
                value =  st1.nextToken();
            }
        }catch (Exception ex){
            value = "";
        }
        return value;
    }

    /**
     * Logs the element data securely with ****
     * @param elementDataKey
     * @param secureLogging
     * @param md
     * @throws ElementException
     */
    protected static void secureLog(String elementDataKey, boolean secureLogging, VoiceElementData md) throws ElementException{
        if(secureLogging){
            md.setElementData(elementDataKey + VoiceElementUtil.SECURE_LOGGING_SUFFIX, VoiceElementUtil.SECURE_LOGGING_VALUE, VoiceElementData.PD_STRING, true);
        }
    }
}