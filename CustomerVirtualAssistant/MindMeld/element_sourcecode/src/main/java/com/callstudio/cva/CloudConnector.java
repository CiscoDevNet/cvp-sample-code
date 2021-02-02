package com.callstudio.cva;

import com.cisco.cvp.callserver.agentassist.AgentAssistAuthProvider;
import com.cisco.cvp.callserver.provider.CallServerRestProvider;

import com.cisco.cvp.ivr.VXMLServerSubsystem;


public class CloudConnector {

    AgentAssistAuthProvider agentAssistAuthProvider;
    CallServerRestProvider callServerRestProvider;

    VXMLServerSubsystem csSubsytem = null;


    public void loadProperties(){


    }
}
