

# Application Virtual Agent Voice
Demonstrates the capabilities of Virtual Agent Voice Call Studio Element with graceful call handling, custom exit and re-entry using Connector Type as Service App.

## Download Instructions
* The zip file must be downloaded from GitHub using “Download raw file” option (or via git clone / curl)

## Application Behaviour
* User needs to select Connector Type as Service App from dropdown and Virtual Agent as Scripted to start event flow in the setting attribute of VirtualAgentVoice.
* Add mandatory Config ID and Agent ID.
* Streams the user audio via Universal Harness, receives the audio response, play it and loops the flow.
* Receive the Response from via Universal Harness, handle the exit state to break the loop through Decision Element based on exit events (Agent Handoff or End of Session).
* For processing outputs from VirtualAgentVoice element, pre-process the JSON data received as shown in the example below.
    Sample reference - Refer to Set Value Node for parsing of agent-handoff JSON to extract required information for further processing.

        var inputJSON ={Data.Element.VirtualAgentVoice_01.agent_handoff};
        var fixJSON1 = inputJSON.replace(/\\:/g,':');  
        var fixJSON2 = fixJSON1.replace(/\\,/g,',');


## Application Enhancements
* In the VirtualAgentVoice element, ConnectorType dropdown has been added.
* Select Service App for BYOVA Apps and then scripted dropdown is there, and then add the "Config ID" for service App and "Agent ID" for agent id.
* Local prompt can be used for transition experience optimisation and various other properties can be used during call start/in-between for VirtualAgentVoice.
* Support to the following VXML Properties added to VirtualAgentVoice Element -

       
       com.cisco.language
       Recognize.model
       Recognize.modelVariant
       com.cisco.voicebrowser.welcomeLatencyPromptURL
       com.cisco.voicebrowser.welcomeLatencyInitialWait
       com.cisco.voicebrowser.welcomemaxwaittime
       com.cisco.responseThreshold

* Note: Provider needs to be capable of handling these VXML properties.

## Virtual Agent Voice Element specific guide
* For BYOVA flow: In the VirtualAgentVoice element, mention the config ID in the "Config ID" field and Agent ID in the "Agent ID" field. This is a mandatory field.


## Graceful Call Handling on Error
* In VirtualAgentVoice element, add a event with event type as "VXML Event" and select "error.noresource" in event list. With this a new separate flow can be created for any noresource error. Refer the pdf attached for different types of noresource scenarios.
* The "Error" exit state of VirtualAgentVoice element can be used to handle any grpc error from Universal Harness services. The "error_code" is populated as element data of VirtualAgentVoice on grpc error,which can be used to create a separate flow to gracefully handle any grpc error from Universal Harness services. Refer the pdf attached for different types of grpc error scenarios.

### Error Handling for Virtual Agent Voice
For seamless integration of VirtualAgentVoice, every dialogue in the with the Virtual Agent Voice MUST have a response to be played back to the caller.

If an Virtual Agent Voice lacks any dialogue without agent response defined, the VirtualAgentVoice call flow will terminate with an error.badfetch. This prevents undesirable “dead air” with indication to ensures agent responses are properly defined in the Virtual Agent Voice. Thus, it is recommended to provide agent responses for every dialogue to avoid VirtualAgentVoice call flow termination with an error.badfetch.

If handling error.badfetch gracefully is preferred instead of defining responses for every dialogue, Call Studio application developers using the VirtualAgentVoice element can manage this error similarly to error.noresource.

To define an exit state in the VirtualAgentVoice element:
* Add an event with Event Type set to "VXML Event"
* Select "error.badfetch" from the event list

This exit state in VirtualAgentVoice element ensures a controlled call flow, even in cases where agent responses are missing.

## Instructions
* The file attached contains:
   * VAV_AI_Agent_BYOVA_Scripted -  Virtual Agent Voice with Fallback as well as Custom Exit and Re-entry to same or diff Virtual Agent Voice.
   

* Call studio application is present in folder named "VAV_AI_Agent_BYOVA_Scripted_CS" which can be imported and used
* Deployed application on vxml server is present in folder named "VAV_AI_Agent_BYOVA_Scripted_deployed"
