# Application Virtual Agent Voice

Demonstrates the capabilities of the Virtual Agent Voice Call Studio element with graceful call handling, multiple-bot transitions, and custom exit and re-entry using **Webex CCAI** as the Connector Type.

## Application Behaviour

- Select **Webex AI Agent** and **Autonomous** mode in the VirtualAgentVoice element to configure the Agent ID required by the Webex AI Agent.
- Provide a valid **Agent ID** in the element settings. This field is mandatory for starting the AI session.
- VAV handles the internal conversational loop by streaming caller audio to the Webex AI Agent, receiving and playing responses, and continuing until the agent provides an exit condition.
- In a multiple-bot flow, the caller is routed to the VirtualAgentVoice element configured for the subsequent bot. When `dynamic_welcome_message` is enabled, that bot plays its dynamic welcome prompt upon entry.
- For a custom exit, VAV exposes `eventName` and `eventData` from the AI Agent's custom payload. The Call Studio flow can perform an intermediate action and then re-enter the same or another VirtualAgentVoice element.
- Decision logic evaluates VAV element data such as agent handoff, `end_session`, custom exit, and error events to determine the next flow path.
- Agent handoff is supported by capturing metadata from the VAV response and processing it through the Set Value element before playing a transfer prompt.
- Fallback handling allows the application to respond gracefully to VXML `error.badfetch`, `error.noresource`, and adapter-level gRPC errors.
- gRPC errors are handled through the error handler block, which plays a dedicated audio prompt before returning to the calling application.
- All branches—handoff, session end, fallback, custom exit, or error—terminate cleanly through the Subdialog Return element to ensure proper call cleanup.
- The Set Value and Audio elements can be enabled when the agent-handoff JSON contains data that must be parsed and used later in the flow.

## Application Enhancements

- A **Connector Type** dropdown has been added to the VirtualAgentVoice element.
- Select **Webex CCAI** and **Autonomous** mode to configure the required **Agent ID**.
- Information from the custom payload sent by the Webex AI Agent is stored as `eventData` and `eventName` in the VirtualAgentVoice element data.
- A custom exit occurs when `Execute_Request` and `Event_Name` are present in the custom payload.
- After digit or value collection, the application can re-enter through the same or a different VirtualAgentVoice element. This sample uses a different element for re-entry.
- The VirtualAgentVoice element's `eventData` output can be parsed and used as required.
- Values in the **Event Data** attribute use name-value pairs and support strings, valid JSON, and substitution with a valid value.
- Multiple-bot flows are supported by routing the caller to another VirtualAgentVoice element configured with the subsequent bot's Agent ID.
- The subsequent bot can play a dynamic welcome prompt when the custom VXML property `com.cisco.AIAgent.MetaData.DynamicWelcomeMessage` is set to `true`.
- Custom exit and re-entry are supported by setting the target VirtualAgentVoice element's **Event Name** to the exact event name defined in AI Agent Studio.
- Local prompts can improve the caller experience during call startup and transitions.
- The VirtualAgentVoice element supports the following VXML properties:

  ```text
  com.cisco.voicebrowser.welcomeLatencyPromptURL
  LatencyInitialWait
  maxwaittime
  ```

## Virtual Agent Voice Element specific guide

- For a Webex AI Agent flow, enter the Agent ID in the VirtualAgentVoice element's **Agent ID** field. This field is mandatory.
- The `eventName` and `eventData` element data provide the `Event_Name` and data values specified in a custom payload from the Webex AI Agent or vendor.
- Re-entry after a custom exit can use the same or a different VirtualAgentVoice element, depending on the flow requirement.

**Multiple bots and `dynamic_welcome_message`**

When `dynamic_welcome_message` is enabled, the subsequent bot plays its dynamic welcome-message prompt as soon as the caller enters it.

Add this custom VXML property to the VirtualAgentVoice element for the subsequent bot:

- **Name:** `com.cisco.AIAgent.MetaData.DynamicWelcomeMessage`
- **Value:** `true`

The default value is `false`.

**Custom exit and re-entry**

1. Define the exit event in AI Agent Studio, for example `custom_exit`.
2. Configure the AI Agent's custom payload with `Execute_Request` and `Event_Name` so that VAV takes the custom exit.
3. Use `eventName` and `eventData` to route the call or process values returned in the custom payload.
4. To re-enter through the same or a different VirtualAgentVoice element, set its **Event Name** property to the exact event name defined in AI Agent Studio, for example `custom_exit`.

The Event Name in Call Studio must exactly match the event name defined in AI Agent Studio. The `eventData` value can contain name-value pairs, strings, valid JSON, or substituted values and can be used for digit collection, routing, or other intermediate actions before re-entry.

## Graceful Call Handling on Error

- In the VirtualAgentVoice element, add an event with **Event Type** set to **VXML Event**, then select `error.noresource`. A separate flow can then handle unavailable resource scenarios.
- Use the VirtualAgentVoice element's **Error** exit state to handle gRPC errors from Universal Harness services. The element exposes the error through `error_code`, which can be evaluated to route the call to a dedicated fallback flow.

### Error Handling for Virtual Agent Voice

For seamless VirtualAgentVoice integration, every AI Agent dialogue must provide a response that can be played to the caller.

If an AI Agent dialogue does not define a response, the VirtualAgentVoice flow can terminate with `error.badfetch`. This behavior prevents dead air and helps ensure that agent responses are properly configured.

If `error.badfetch` must be handled instead of defining a response for every dialogue, configure it in the same way as `error.noresource`:

- Add an event with **Event Type** set to **VXML Event**.
- Select `error.badfetch` from the event list.

This exit state ensures a controlled call flow when an agent response is missing.

## Instructions

- The attached files include `VAV_Native_Agent_Autonomous_CustomEvents`, an AI Agent application with fallback, agent transfer, and end-session handling.
- The importable Call Studio application is in `VAV_Native_Agent_Autonomous_CustomEvents_CS`.
- The application deployed on the VXML server is in `VAV_Native_Agent_Autonomous_CustomEvents_deployed`.

> **Note:** Refer to the AI Agent documentation for more details.
