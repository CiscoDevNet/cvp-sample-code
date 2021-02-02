package com.callstudio.cva;

/**
 * Adding new file for defining constant used in studio application.
 *
 * @author kbhattad
 *
 */
public class AudiumConstants {


    public final static String PROPERTY_SECURE_LOGGING = "com.cisco.secureLogging";

    public final static String PROPERTY_PHRASE_HINTS = "Recognize.phraseHints";

    public final static String PROPERTY_ALTERNATE_LANGUAGES = "Recognize.alternateLanguages";

    public final static String PROPERTY_PROJECT_ID = "com.cisco.projectId";

    public final static String PROPERTY_LANGUAGE = "com.cisco.language";

    public final static String PROPERTY_OUTPUT_AUDIO_SUPPORT = "com.cisco.outputAudioSupport";


    public final static String GRAMMAR_TRANSCRIBE = "builtin:speech/transcribe";

    public final static String GRAMMAR_NLP_DIALOGFlOW = "builtin:speech/nlp@dialogflow";

    public final static String MAX_TIME = "com.cisco.maxtime";

    public final static String FINAL_SILENCE = "com.cisco.finalSilence";

    public final static String DTMF_OVERLAY = "com.cisco.dtmfoverlay";

    public final static String DTMF_OVERLAY_INTERVAL = "com.cisco.dtmfoverlayinterval";
    /**
     * This property is used to reference the preserved participantId and return it for
     * subsequent requests.
     */
    public final static String PARTICIPANT_ID = "Dialogflow.participantId";
    /**
     * This  property used to hold initialization text and its value is used by VVB
     * to initiate dialog with DialogFlow.
     */
    public final static String PROPERTY_INITIAL_AUDIO = "com.cisco.initialAudio";

    /**
     * callstudio DialogFlow element setting key (internally used) for Initialization Text
     */
    public final static String INITIAL_AUDIO_SETTING = "initial_audio";

    /**
     * poperty name sent to vvb for termination character
     */
    public final static String TERM_CHAR = "termchar";

    /**
     * Termination charcter setting key used internally
     */
    public final static String TERM_CHAR_SETTING = "term_char";

    public static final String DEFAULT_CONFIDENCE = "0.40";

    public static final String DEFAULT_NOINPUT_TIMEOUT = "5s";

    /**
     * default no input count
     */
    public static final String DEFAULT_MAX_NOINPUT_COUNT = "3";

    /**
     * Default no match count
     */
    public static final String DEFAULT_MAX_NOMATCH_COUNT = "3";

    public final static String DEFAULT_TERMINATION_CHAR = "#";

    public static final String DEFAULT_LANGUAGE_FIELD_NAME = "en-US";

    public static final String VOICE = "voice";

    public static final String DTMF = "dtmf";

    public static final String DTMF_VOICE = "dtmf+voice";

    public static final String CVA_ELEMENT_FOLDER = "Customer Virtual Assistant";

    /**
     * Default final silence value for voice element
     */
    public static final String DEFAULT_FINALSILENCE = "2s";

    /**
     * default Max input time for voice element
     */
    public static final String DEFAULT_MAXINPUT = "30s";

    /**
     * Grammar media type sent to vvb when DTMF grammar is selected
     */
    public static final String MEDIA_TYPE_GRAMMAR = "application/grammar+regex";

    /**
     * Default maximum no match count
     */
    public static final int DEFAULT_MAX_NOMATCH_COUNT_INT = 3;

    /**
     * Default maximum no input count
     */
    public static final int DEFAULT_MAX_NOINPUT_COUNT_INT = 3;






}