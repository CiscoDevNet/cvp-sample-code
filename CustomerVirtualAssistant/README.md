Customer Virtual Assistant(CVA) enables the IVR Platform to integrate with cloud based speech services. 

Following are the speech services which are offered:
* Text to Speech - Use Google [Text to Speech](https://cloud.google.com/text-to-speech/) services in your application for TTS operations. 
* Speech to Text - Use Google [Speech to Text](https://cloud.google.com/speech-to-text/) services in your application for user speech transcription operation.
* Speech to Intent - Use Google [Dialogflow](https://dialogflow.com/docs) services in your applications voice virtual agent creation.

For CVA feature CVP Call Studio introduced four new elements.

* Dialogflow Element
The DialogFlow element can be used to engage the Google Dialogflow services. 
The DialogFlow element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is an extension of Form element and it engages the special resource on VVB called Speech Server to communicate with the Dialogflow Server. 
To indicate the Dialogflow server resource requirement, Call Studio creates a specific grammar - builtin:speech/nlp@dialogflow - and sends it to VVB in VXML Page.

* DialogflowIntent Element
The DialogFlowIntent element can be used to engage the Google Dialogflow services. The DialogFlowIntent element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is an extension of Form element and it engages the Speech Server resource on VVB to communicate with the Google Speech to Text Server to get user input and then send it to dialogflow and finds user intent from it. 
To indicate the Dialogflow server resource requirement, Call Studio creates a specific grammar - builtin:speech/transcribe - and sends it to VVB in VXML Page.

* DialogflowParam Element
The DialogFlowParam element can be used to engage the Google Dialogflow services. The DialogFlowParam element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is an extension of Form element and it engages the Speech Server resource on VVB to communicate with the Google Speech to Text Server to get user input and then send it to dialogflow and fills param value from it. 
To indicate the Dialogflow server resource requirement, Call Studio creates a specific grammar - builtin:speech/transcribe - and sends it to VVB in VXML Page.

* Transcribe Element
The Transcribe element in Call Studio can be used to engage the Google Speech to Text services. 
The Transcribe element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is extension of the Form element and it engages the Speech Server resource on VVB to communicate with the Google Speech to Text Server. 
To indicate the Speech to Text server resource requirement, Call Studio creates a specific grammar - builtin:speech/transcribe - and sends it to VVB in a VXML Page. 
It does not specify which transcribe service is to be used, this is configured at VVB.

Note: The Transcribe element works only in VoiceXML 2.1 with Cisco DTMF VoiceXML Gateway adaptor.
