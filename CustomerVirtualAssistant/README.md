Customer Virtual Assistant(CVA) enables the IVR Platform to integrate with cloud based speech services. 

Following are the speech services which are offered:
* Text to Speech - Use Google [Text to Speech](https://cloud.google.com/text-to-speech/) services in your application for TTS operations. 
* Speech to Text - Use Google [Speech to Text](https://cloud.google.com/speech-to-text/) services in your application for user speech transcription operation.
* Speech to Intent - Use Google [Dialogflow](https://dialogflow.com/docs) services in your applications voice virtual agent creation.

Documentation:
* [CVA Design](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/icm_enterprise/icm_enterprise_12_5_1/design/guide/ucce_b_soldg-for-unified-cce-12_5/ucce_b_soldg-for-unified-cce-12_5_chapter_01000.html#concept_504D901FE4FB5DA0D6F0701E4BFC4CA3) 

For CVA feature CVP Call Studio introduced four new elements.

* Dialogflow Element: The DialogFlow element can be used to engage the Google Dialogflow services. 
The DialogFlow element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is an extension of Form element and it engages the special resource on VVB called Speech Server to communicate with the Dialogflow Server. 
To indicate the Dialogflow server resource requirement, Call Studio creates a specific grammar - builtin:speech/nlp@dialogflow - and sends it to VVB in VXML Page.

[Dialogflow Call Studio Element Specification](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111001.html)

* DialogflowIntent Element: The DialogFlowIntent element can be used to engage the Google Dialogflow services. The DialogFlowIntent element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is an extension of Form element and it engages the Speech Server resource on VVB to communicate with the Google Speech to Text Server to get user input and then send it to dialogflow and finds user intent from it. 
To indicate the Dialogflow server resource requirement, Call Studio creates a specific grammar - builtin:speech/transcribe - and sends it to VVB in VXML Page.

[DialogflowIntent Call Studio Element Specification](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111011.html)

* DialogflowParam Element: The DialogFlowParam element can be used to engage the Google Dialogflow services. The DialogFlowParam element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is an extension of Form element and it engages the Speech Server resource on VVB to communicate with the Google Speech to Text Server to get user input and then send it to dialogflow and fills param value from it. 
To indicate the Dialogflow server resource requirement, Call Studio creates a specific grammar - builtin:speech/transcribe - and sends it to VVB in VXML Page.

[DialogflowParam Call Studio Element Specification](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111100.html)

* Transcribe Element: The Transcribe element in Call Studio can be used to engage the Google Speech to Text services. 
The Transcribe element is located under the Customer Virtual Assistant group in the Call Studio Elements. 
This element is extension of the Form element and it engages the Speech Server resource on VVB to communicate with the Google Speech to Text Server. 
To indicate the Speech to Text server resource requirement, Call Studio creates a specific grammar - builtin:speech/transcribe - and sends it to VVB in a VXML Page. 
It does not specify which transcribe service is to be used, this is configured at VVB.

[Transcribe Call Studio Element Specification](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111010.html)

Note: The Transcribe element works only in VoiceXML 2.1 with Cisco DTMF VoiceXML Gateway adaptor.
