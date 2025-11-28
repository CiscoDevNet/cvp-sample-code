

# Application Virtual Agent Voice
Demonstrates the capabilities of Virtual Agent Voice Call Studio Element with graceful call handling, custom exit and re-entry using Connector Type as Integration.

## Application Behaviour
* User needs to select Integration from dropdown and then add config ID and add mandatory desired event name to start event flow in the setting attribute of VirtualAgentVoice 
  * This setting value is mandatory for Integration.
 * Streams the user audio to Webex AI Agent via Universal Harness, receives the audio response, play it and loops the flow.
* Receive the Response from Webex AI Agent via Universal Harness, handle the exit state to break the loop through Decision Element based on exit events (Agent Handoff or End of Session or Custom Exit).
* Re-entry after custom exit is done through VirtualAgentVoice element (here in this sample application different VAV element is used) after say digit/value/PIN collection.
* After the custom exit, value is collected  on-prem say as PIN number and added as Event Data using substitution for further use on Webex AI Agent.
* At re-entry provide the desired Custom event name and Custom Data to be passed into the context.

 # * NOTE: For Scripted AI Agent Custom Event Name is Mandatory to be defined in AI Agent and same need to be passed while invoking the AI Agent Call Flow


## Application Enhancements
* In the VirtualAgentVoice element, ConnectorType dropdown has been added.
  * Select Integration for Vendor Specific Agent to configure "Config ID"  
  * Store the information from the custom payload sent by Webex AI agent and captures the value as eventData and eventName in the element data of VirtualAgentVoice element.
* Custom Exit takes place upon encountering Execute_Request and Event_Name in the custom payload.
* Re-entry can be done through VirtualAgentVoice element (here in this sample application different VirtualAgentVoice element is used to re-enter) after digit/value collection.
* Output element data of VirtualAgentVoice element eventData can be parsed and used as per requirement.
* Values in the Event Data attribute of VirtualAgentVoice is of form name:value pairs which support string, Valid Json as well as substitution with desired valid value.
* SIP Headers Restricted is an optional attribute which can contain comma separated list for the sip headers to be excluded from propagating to orchestrator layer.                                                        Default value is null. All the headers will flow through if nothing specified.
  * NOTE: This attribute is not supported for Webex AI Agent currently.
* Local prompt can be used for transition experience optimisation and various other properties can be used during call start/in-between for VirtualAgentVoice.
* Support to the following VXML Properties added to VirtualAgentVoice Element -

       
       language
       Recognise.model
       Recognize.modelVariant
       com.cisco.voicebrowser.welcomeLatencyPromptURL
       LatencyInitialWait
       maxwaittime

## Virtual Agent Voice Element specific guide
* For Integration flow: In the VirtualAgentVoice element, mention the config ID in the "Config ID" field. This is a mandatory field.
* Element data- “eventName” and “eventData” can be used to provide Event_Name and Data value specified in the custom payload from Webex AI Agent or Vendor side.
* Re-entry after custom exit can be done through same/different VirtualAgentVoice element as per requirement.
  * NOTE: For Scripted AI Agent Custom Event Name is Mandatory to be defined in AI Agent and same need to be passed while invoking the AI Agent Call Flow.

## Graceful Call Handling on Error
* In VirtualAgentVoice element, add a event with event type as "VXML Event" and select "error.noresource" in event list. With this a new separate flow can be created for any noresource error. Refer the pdf attached for different types of noresource scenarios.
* The "Error" exit state of VirtualAgentVoice element can be used to handle any grpc error from Universal Harness services. The "error_code" is populated as element data of VirtualAgentVoice on grpc error, which can be used to create a separate flow to gracefully handle any grpc error from Universal Harness services. Refer the pdf attached for different types of grpc error scenarios.

### Error Handling for Virtual Agent Voice
For seamless integration of VirtualAgentVoice, every dialogue in the with the AI Agent MUST have a response to be played back to the caller.

If an AI Agent lacks any dialogue without agent response defined, the VirtualAgentVoice call flow will terminate with an error.badfetch. This prevents undesirable “dead air” with indication to ensures agent responses are properly defined in the AI Agent. Thus, it is recommended to provide agent responses for every dialogue to avoid VirtualAgentVoice call flow termination with an error.badfetch.

If handling error.badfetch gracefully is preferred instead of defining responses for every dialogue, Call Studio application developers using the VirtualAgentVoice element can manage this error similarly to error.noresource.

To define an exit state in the VirtualAgentVoice element:
* Add an event with Event Type set to "VXML Event"
* Select "error.badfetch" from the event list

This exit state in VirtualAgentVoice element ensures a controlled call flow, even in cases where agent responses are missing.

## Instructions
* The file attached contains:
   * VAV_AI_Agent_Integration_Scripted -  AI Agent with Fallback as well as Custom Exit and Re-entry to same or diff AI Agent


* Call studio application is present in folder named "VAV_AI_Agent_Integration_Scripted_CS"" which can be imported and used
* Deployed application on vxml server is present in folder named "VAV_AI_Agent_Integration_Scripted_deployed"
