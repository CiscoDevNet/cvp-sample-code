This Section contains souce code to build MindMeld callstudio element and sample callstudio application to demonstrate capability of MindMeld with CVP.

# Instruction to set up VXML server

## Prerequisite
  * CVP and Call Studio 12.6 EFT Build
  * Required 12.6 Cloud Connect setup.
  
  ## Call Studio setup
  * Close Call Studio
  * Copy the mindmeld.jar in <CALLSTUDIO_HOME>\eclipse\plugins\com.audiumcorp.studio.elements.core\lib
  * reopen Call Studio.
  * MindMeld element appears under "Customer Virtual Assistant" in Elements section.
  
  ## VXML Server setup
  * Stop VXML server
  * Copy mindmeld.jar to <CVP_HOME>\VXMLServer\common\lib
  * Restart VXML server.
  
  ## Deploy sample Call Studio app
  * download callstudio_app.zip
  * import into Call Studio
  * Validate it and deploy into vxml server
  
  
  ## Below are the steps to configure CloudConnect
 
    Before you begin
    Import the certificate from Call Server to OAMP Server.
    For more information, see Secure HTTP Communication between OAMP Server and Call Server section in Configuration Guide for Cisco Unified Customer Voice           Portal at https://www.cisco.com/c/en/us/support/customer-collaboration/unified-customer-voice-portal/tsd-products-support-series-home.html.
    Ensure Unified CVP hostname is DNS resolvable from OAMP Server.
    Restart CVP OPSConsoleServer service.
    Procedure
    Step 1	
    In Cisco Unified Customer Voice Portal, click Integration > Cloud Connect.
    Step 2	
    From the Device drop-down list, select a Unified CVP device.
    Step 3	
    In the Publisher IP Address / Hostname text box, enter the FQDN / IP address of the publisher.
    Step 4	
    In the Subscriber IP Address / Hostname text box, enter the FQDN / IP address of the subscriber.
    Step 5	
    In the User Name text box, enter the Cloud Connect administrator username.
    Step 6	
    In the Password text box, enter the Cloud Connect administrator password.
    Step 7	
    Click Save.
    Step 8
    Add below properties in <CVP_HOME>/conf/sip.properties
        SIP.CloudConnect.AgentAssistAuthTokenApi = /cloudconnectmgmt/token
        SIP.CloudConnect.AgentAssistAuthTokenScopes = cjp-ccai:read
    Step 8	
    Restart Cisco CVP CallServer service.
    
    Link to register Cloud connect on to Control Hub (https://help.webex.com/en-us/n24wo0fb/Register-Cloud-Connect)

    
  ## Element Settings
 
 ![Alt text](https://github.com/CiscoDevNet/cvp-sample-code/blob/master/CustomerVirtualAssistant/MindMeld/resources/root_element.png?raw=true "Element Configuration")
 
  ![Alt text](https://github.com/CiscoDevNet/cvp-sample-code/blob/master/CustomerVirtualAssistant/MindMeld/resources/param_element.png?raw=true "Element Configuration")
  
  

