

# Application Virtual Agent Voice
Demonstrates the capabilities of Virtual Agent Voice Call Studio Element with graceful call handling, custom exit and re-entry using  Connector Type as Webex CCAI.

## Application Behaviour
* Select  Connector Type as Webex CCAI and Virtual Agent as Autonomous in the VirtualAgentVoice element to configure the Agent ID required by Virtual Agent Voice.
* Provide a valid Agent ID in the element settings, as this is mandatory for initiating the AI session.
* VAV handles the internal conversational loop, streams user audio to Virtual Agent Voice, receives responses, plays them back, and continues until an exit condition is provided by the agent.
* Decision logic evaluates VAV element data such as agent-handoff, end_session, and error events to determine the next flow path.
* Agent handoff is supported by capturing metadata from the VAV response and processing it through the Set Value element before playing a transfer prompt.
* Fallback handling ensures the application responds gracefully to VXML badfetch, noresource, or adaptor-level gRPC errors.
* gRPC errors are handled through the error handler block, which plays a dedicated audio prompt before returning to the calling application.
* All branches—handoff, session end, fallback, or error—terminate cleanly via the subdialog return element to ensure proper call cleanup.
* For processing outputs from VirtualAgentVoice element, pre-process the JSON data received as shown in the Set Value element of application.
 Sample reference - Refer to Set Value Node for parsing of agent-handoff JSON to extract required information for further processing.

        var inputJSON ={Data.Element.VirtualAgentVoice_01.agent_handoff};
        var fixJSON1 = inputJSON.replace(/\\:/g,':');  
        var fixJSON2 = fixJSON1.replace(/\\,/g,',');
       
* Note : The Set Value node and audio node can be enabled when the agent-handoff JSON contains data that needs to be parsed and used at a later stage.


## Application Enhancements
* In the VirtualAgentVoice element, ConnectorType dropdown has been added.
* Select Webex CCAI for Autonomous to configure the "Agent ID"
* Store the information from the custom payload and captures the value as eventData and eventName in the element data of VirtualAgentVoice element.
* Custom Exit takes place upon encountering Execute_Request and Event_Name in the custom payload.
* Re-entry can be done through VirtualAgentVoice element (here in this sample application different VirtualAgentVoice element is used to re-enter) after digit/value collection.
* Output element data of VirtualAgentVoice element eventData can be parsed and used as per requirement.
* Values in the Event Data attribute of VirtualAgentVoice is of form name:value pairs which support string, Valid Json as well as substitution with desired valid value.

* Local prompt can be used for transition experience optimisation and various other properties can be used during call start/in-between for VirtualAgentVoice.
* Support to the following VXML Properties added to VirtualAgentVoice Element -

       
       com.cisco.voicebrowser.welcomeLatencyPromptURL 
       com.cisco.voicebrowser.welcomeLatencyInitialWait
       com.cisco.voicebrowser.welcomemaxwaittime
       com.cisco.responseThreshold

* Note: Provider needs to be capable of handling these VXML properties.

## Virtual Agent Voice Element specific guide
* For Virtual Agent Voice flow: In the VirtualAgentVoice element, mention the Agent ID in the "Agent ID" field. This is a mandatory field.
* Element data- “eventName” and “eventData” can be used to provide Event_Name and Data value specified in the custom payload from Virtual Agent Voice or Vendor side.
* Re-entry after custom exit can be done through same/different VirtualAgentVoice element as per requirement.

## Graceful Call Handling on Error
* In VirtualAgentVoice element, add a event with event type as "VXML Event" and select "error.noresource" in event list. With this a new separate flow can be created for any noresource error. Refer the pdf attached for different types of noresource scenarios.
* The "Error" exit state of VirtualAgentVoice element can be used to handle any grpc error from Universal Harness services. The "error_code" is populated as element data of VirtualAgentVoice on grpc error,which can be used to create a separate flow to gracefully handle any grpc error from Universal Harness services. Refer the pdf attached for different types of grpc error scenarios.

### Error Handling for Virtual Agent Voice
For seamless integration of VirtualAgentVoice, every dialogue in the with the Virtual Agent Voice MUST have a response to be played back to the caller.

If an Virtual Agent Voice  lacks any dialogue without agent response defined, the VirtualAgentVoice call flow will terminate with an error.badfetch. This prevents undesirable “dead air” with indication to ensures agent responses are properly defined in the Virtual Agent Voice. Thus, it is recommended to provide agent responses for every dialogue to avoid VirtualAgentVoice call flow termination with an error.badfetch.

If handling error.badfetch gracefully is preferred instead of defining responses for every dialogue, Call Studio application developers using the VirtualAgentVoice element can manage this error similarly to error.noresource.

To define an exit state in the VirtualAgentVoice element:
* Add an event with Event Type set to "VXML Event"
* Select "error.badfetch" from the event list

This exit state in VirtualAgentVoice element ensures a controlled call flow, even in cases where agent responses are missing.

## Instructions

* The file attached contains:
  * VAV_Native_Agent_Autonomous -  Virtual Agent Voice with Fallback as well as agent transfer and end session.

* Call studio application is present in folder named "VAV_Native_Agent_Autonomous_CS" which can be imported and used
* Deployed application on vxml server is present in folder named "VAV_Native_Agent_Autonomous-deployed"
