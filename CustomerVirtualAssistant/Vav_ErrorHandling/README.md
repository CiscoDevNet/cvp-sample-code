
# Application Virtual Agent Voice
Demonstrates the capabilities of Virtual Agent Voice Call Studio Element with graceful call handling.

## Application Behaviour
* User needs to add desired start event name in the setting attribute of VirtualAgentVoice otherwise default event (WELCOME) is invoked towards Vendor Side via Universal Harness at beginning of call.
* Streams the user audio to Vendor side (Google/Nuance/etc) via Universal Harness, receives the audio response, play it and loops the flow.
* Receive the Response from vendor side via Universal Harness, handle the exit state to break the loop through Decision Element based on exit events (Agent Handoff or End of Session or Custom Exit).
* Re-entry after custom exit is done through VirtualAgentVoice element (here in this sample application different VAV element with same config ID is used to re-enter) after digit/value collection.
* At re-entry provide the desired Custom event name.
* For re-entry to the same VirtualAgentVoice element, _set value_ can be used to override the Event Name or Event Data value with desired ones.
* After the custom exit, value is collected  on-prem as PIN number and added as Event Data using substitution for further use on cloud side service.


## Application Enhancements
* In the VirtualAgentVoice element, mention the Config ID in the "Config ID" field. If this field is left blank default config is fetched.
* Store the information from the custom payload sent by Vendor Specific Agent and captures the value as eventData and eventName in the element data of VirtualAgentVoice element.
* Custom Exit takes place upon encountering Execute_Request and Event_Name in the custom payload.
* Re-entry can be done through VirtualAgentVoice element (here in this sample application different VirtualAgentVoice element is used to re-enter) after digit/value collection.
* Output element data of VirtualAgentVoice element eventData can be parsed and used as per requirement.
* Values in the Event Data attribute of VirtualAgentVoice is of form name:value pairs which support string, Valid Json as well as substitution with desired valid value.
* SIP Headers Restricted is an optional attribute which can contain comma separated list for the sip headers  to be excluded from propagating to orchestration layer.                                                        Default value is null. All the headers will flow through if nothing specified.
* Local prompt can be used for transition experience optimisation and various other properties can be used during call start/in-between for VirtualAgentVoice.
* Support to the following VXML Properties added to VirtualAgentVoice Element -

       Recognize.model - Name of the model to be used for recognition
       com.cisco.language - To set project language preference. Root Application language will be used if not provided
       Recognize.modelVariant - It can have a few values including USE_BEST_AVAILABLE, USE_STANDARD, USE_ENHANCED
       Synthesize.voiceName - It refers to the name of the Voice to be used for the prompts coming from vendor side
       Synthesize.voiceGender - It refers to the gender of the Voice to be used for the prompts coming from vendor side

## Virtual Agent Voice Element specific guide
* In the VirtualAgentVoice element, mention the Config ID in the "Config ID" field. If this field is left blank default config is fetched.
* Element data- “eventName” and “eventData” can be used to provide Event_Name and Data value specified in the custom payload from Vendor side respectively.
* Re-entry after custom exit can be done through same/different VirtualAgentVoice element as per requirement.

## Graceful Call Handling on Error
* In VirtualAgentVoice element, add a event with event type as "VXML Event" and select "error.noresource" in event list. With this a new separate flow can be created for any noresource error. Refer the pdf attached for different types of noresource scenarios.
* The "Error" exit state of VirtualAgentVoice element can be used to handle any grpc error from Universal Harness services. The "error_code" is populated as element data of VirtualAgentVoice on grpc error,which can be used to create a separate flow to gracefully handle any grpc error from Universal Harness services. Refer the pdf attached for different types of grpc error scenarios.

### Error Handling for Virtual Agent Voice
 For seamless integration of VirtualAgentVoice with the Google DFCX Agent, every dialogue response must include at least one of the following:
1.	Output audio text (with or without SSML)
2.	Pre-recorded audio playback

   Agent responses can contain multiple instances of Output audio text and Pre-recorded audio, in any order. However, Output audio text cannot be empty or consist solely of spaces.

   If a DFCX Agent lacks any dialogue without agent response defined, the VirtualAgentVoice call flow will terminate with an error.badfetch. This prevents undesirable “dead air” with indication  to ensures agent responses are properly defined in Google DFCX. Thus, it is recommended to provide agent responses for every dialogue to avoid VirtualAgentVoice call flow termination with an error.badfetch.

   If handling error.badfetch gracefully is preferred instead of defining responses for every dialogue, Call Studio application developers using the VirtualAgentVoice element can manage this error similarly to error.noresource.

   To define an exit state in the VirtualAgentVoice element:
 * Add an event with Event Type set to "VXML Event"
 * Select "error.badfetch" from the event list

This exit state in VirtualAgentVoice element ensures a controlled call flow, even in cases where agent responses are missing.

##  Element Specifications Guide
* [VirtualAgentVoice Element specifications guide](https://www-author3.cisco.com/content/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/12-6-2/elementspecification/guide/ccvp_b_1262-element-specifications-guide/ccvp_m_1261-vav-element.html)


## Instructions
* The file attached contains call studio application- VAV_CallStudio
* Deployed application on vxml server- VAV_deployed
