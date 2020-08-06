Customer Virtual Assistant(CVA) enables the Customer Voice Portal(CVP) IVR Platform to integrate with cloud based speech services like  [Text to Speech](https://cloud.google.com/text-to-speech/), [Speech to Text](https://cloud.google.com/speech-to-text/) and [Dialogflow](https://cloud.google.com/text-to-speech/docs/).
# CVA Documentation
* [CVA Design](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/icm_enterprise/icm_enterprise_12_5_1/design/guide/ucce_b_soldg-for-unified-cce-12_5/ucce_b_soldg-for-unified-cce-12_5_chapter_01000.html#concept_504D901FE4FB5DA0D6F0701E4BFC4CA3)  Callflows and Architecture.
* [Configure CVA Services in UCCE](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/administration/guide/ccvp_b_1251-administration-guide-for-cisco-unified-customer-voice-portal/ccvp_b_1251-administration-guide-for-cisco-unified-customer-voice-portal_chapter_01.html#topic_39D6199BE6CBA2F5472BC57F4DD5D465) using OAMP.
* [Configure CVA Services in PCCE](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/pcce/pcce_12_5_1/configuration/guide/pcce_b_admin-and-config-guide_12_5/pcce_b_admin-and-config-guide_12_5_chapter_011.html#concept_F7500EC077579D73709659B08E642C69) using PCCE Admin.
* [Dialogflow](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111001.html) Call Studio Element Specification
* [DialogflowIntent](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111011.html) Call Studio Element Specification
* [DialogflowParam](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111100.html) Call Studio Element Specification
* [Transcribe](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/reference/guide/ccvp_b_1251-element-specification-guide-cvp/ccvp_b_1251-element-specification-guide-cvp_chapter_0111010.html) Call Studio Element Specification
# Sample Examples
Download the Sample Application zip file as per your need and import it into Call Studio tool and perform the necessary customization in the application and then deploy the application to VXML Server.
* DFAudio: Demonstrates **Dialogflow Call Studio Element** (Google Dialogflow Audiooutput=true), streams the audio to Google Dialogflow and receives the audio from Dialogflow and plays it. Use this app for easiest and fastest integration with Google Dialogflow Agent.
* DFText: Demonstrates **Dialogflow Call Studio Element** (Google Dialogflow Audiooutput=false), streams the audio to Google Dialogflow and receives the text from Dialogflow and plays the text using Google TTS. Use this sample application as a reference for better performance compare to DFAudio as repeatative prompts are cached.
* DFRemote: Demonstrates **DialogflowIntent & DialogflowParam Call Studio Element**, streams the audio to configured ASR receives the text sends the text for NLU to Dialogflow receives text from Dialogflow, plays the text using TTS. Use this application as a reference to get better control over the call flow in Call Studio, DTMF detection etc.
* Transcribe: Demonstrates **Transcribe Call Studio Element**, streams the audio to configured ASR receives the text. Use this application as a reference for plain Speech to text operation, DTMF detection, Multilingual recognition etc.
# Google Documentation
* Enable [Dialogflow API](https://cloud.google.com/dialogflow/docs/quick/setup#api)
* Enable [Cloud Speech-to-Text API](https://cloud.google.com/apis/docs/getting-started#enabling_apis) (Optional)
* Enable [Cloud Text-to-Speech API](https://cloud.google.com/apis/docs/getting-started#enabling_apis) (Optional)
* Enable [Dialogflow Billing](https://cloud.google.com/dialogflow/docs/quick/setup#billing)
* Upgrade to [Enterprise Edition](https://cloud.google.com/dialogflow/docs/editions#choose_an_edition_and_pricing_plan) for advanced Dialogflow features.
* Enable [Enhanced Models](https://cloud.google.com/dialogflow/docs/data-logging#enabling_data_logging_and_using_enhanced_models) for best speech recognition results.
* Create [Dialogflow Authentication Key](https://cloud.google.com/dialogflow/docs/quick/setup#sa-create)
* Create [Speech-to-Text Key](https://cloud.google.com/speech-to-text/docs/quickstart-client-libraries#before-you-begin)
* Create [Text-to-Speech Key](https://cloud.google.com/text-to-speech/docs/quickstart-client-libraries#before-you-begin)
* [Dialogflow Basics](https://cloud.google.com/dialogflow/docs/basics)
* [Setting up Dialogflow Agent](https://cloud.google.com/dialogflow/docs/quick/setup)
* [Creating a Dialogflow Agent](https://cloud.google.com/dialogflow/docs/quick/build-agent)

**Note**: For OEM users onboarding process, refer to the **Customer Virtual Assistant > Prerequisites** section in the **Cisco Unified Contact Center Enterprise Features Guide** at https://www.cisco.com/c/en/us/support/customer-collaboration/unified-contact-center-enterprise/products-feature-guides-list.html.
