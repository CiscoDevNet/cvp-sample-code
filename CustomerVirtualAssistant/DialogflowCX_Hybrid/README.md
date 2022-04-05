
# Application DFCX
Demonstrates the capabilities of DialogflowCX Callstudio Element.

## Application Behaviour
* Sends the "welcome_event" to Dialogflow CX to initialize the dialog by default, if Element Data- “event_name” is not provided by user.
* Streams the user audio to Google Dialogflow CX, receives the audio response and play it and loops the flow.
* Receive the detectIntentResponse from Dialogflow CX, Handle the exit state to break the loop based on exit events (Agent Handoff or End of Session or Custom Exit).
* Re-entry after custom exit is done through DialogflowCX element (here in this sample application different DFCX element with same config ID is used to re-enter) after digit/value collection.
* Custom event name from payload is used by default as re-entry event name. Provide Element data- “event_name” in the re-enter DialogflowCX element to over-ride the default re-entry event name.
* Sub-dialog application is invoked after the custom exit and returned value is collected as PIN number.

## Application Enhancements
* In the DialogflowCX element, mention the Config ID in the "Config ID" field. If this field is left blank default config is fetched.
* Store the information from the custom payload sent by Dialogflow CX agent and captures the value as custom_payload and custom_event_name in the element data of DialogflowCX element.
* Custom Exit takes place upon encountering Execute_Request and Event_Name in the custom payload.
* Re-entry can be done through DialogflowCX element (here in this sample application different DialogflowCX element is used to re-enter) after digit/value collection.
* Output element data of DialogflowCX element custom_payload can be parsed and used as per requirement.

## Dialogflow CX UCCE feature guide
* In the Dialogflow element, mention the Config ID in the "Config ID" field. If this field is left blank default config is fetched.
* Element data- “event_name” can be used to over-ride both the “welcome_event” and “custom_event_name” (default values) if provided properly at appropriate places.
* Re-entry after custom exit can be done through same/different DialogflowCX element as per requirement.


## Feature guide
* [Dialogflow CX UCCE Feature guide](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/icm_enterprise/icm_enterprise_12_6_1/configuration/ucce_b_features-guide-1261/ucce_m_dialogflow_cx-1261.html)
* [Dialogflow CX PCCE Feature guide](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/pcce/pcce_12_6_1/maintenance/guide/pcce_b_features-guide-1261/ucce_b_features-guide-1261_chapter_01000.pdf)

## Instructions
* The file attached contains call studio applications- cx_diff and subdialogApp
* Deployed applications- cx_diff_deployed and sudialogApp_deployed 
* Exported cx agent configured to use "start_event" as the welcome event name which can be changed accordingly