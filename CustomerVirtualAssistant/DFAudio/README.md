# Application DFAudio
Demonstrates the capabilities of Dialogflow Callstudio Element.
## Preconditions
* Enable [Dialogflow API](https://cloud.google.com/dialogflow/docs/quick/setup#api)
* Enable [Dialogflow Billing](https://cloud.google.com/dialogflow/docs/quick/setup#billing)
* Upgrade to [Enterprise Edition](https://cloud.google.com/dialogflow/docs/editions#choose_an_edition_and_pricing_plan) for advanced Dialogflow features.
* Enable [Enhanced Models](https://cloud.google.com/dialogflow/docs/data-logging#enabling_data_logging_and_using_enhanced_models) for best speech recognition results.
* Create [Dialogflow Authentication Key](https://cloud.google.com/dialogflow/docs/quick/setup#sa-create)
* [Dialogflow Basics](https://cloud.google.com/dialogflow/docs/basics)
* [Setting up Dialogflow Agent](https://cloud.google.com/dialogflow/docs/quick/setup)
* [Creating a Dialogflow Agent](https://cloud.google.com/dialogflow/docs/quick/build-agent)
* [Configure CVA Services in UCCE](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/cvp_12_5/administration/guide/ccvp_b_1251-administration-guide-for-cisco-unified-customer-voice-portal/ccvp_b_1251-administration-guide-for-cisco-unified-customer-voice-portal_chapter_01.html#topic_39D6199BE6CBA2F5472BC57F4DD5D465) using OAMP.
* [Configure CVA Services in PCCE](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/pcce/pcce_12_5_1/configuration/guide/pcce_b_admin-and-config-guide_12_5/pcce_b_admin-and-config-guide_12_5_chapter_011.html#concept_F7500EC077579D73709659B08E642C69) using PCCE Admin.
* Make sure the keyword "hello" is added as a "Training phrase" in "Default Welcome Intent".
* Create a new Intent "AgentTransfer" and train it with relevant "Training Phrases".
## Application Behaviour
* Sends the "Initiation Text" to Dialogflow to initialize the dialog.
* Streams the user audio to Google Dialogflow, receives the audio response and play it and loops the flow.
* Receive the Intent and other parameter from Dialogflow, Handle the exit state to break the loop based on intent.
* Store the values as required from the dialogflow responses and return it with subdialog return callstudio element back to CallServer / ICM, so that based on this routing can happen to right skill set.
## Application Ehnancements
* In the Dialogflow element, mention the GCP account id in the "Service Account ID" field.
* Store the information which needs to be sent back to CCE and return it in "CVP Subdialog Return_01" element. 
