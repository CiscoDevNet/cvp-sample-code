This Section contains instructions to setup and configure CVP with MindMeld(Botlite) Along with sample Call Studio Application.

# Instruction to set up CVP with MindMeld(Botlite)

## Prerequisite
  * 12.6 EFT 1 Build - CVP and Call Studio.
  * 12.6 Cloud Connect setup.
  
  ## Call Studio setup
  * Close Call Studio IDE
  * Copy the mindmeld.jar in <CALLSTUDIO_HOME>\eclipse\plugins\com.audiumcorp.studio.elements.core\lib
  * reopen Call Studio IDE.
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
    
   ## Registration of Cloud Connect to Webex Control hub. 
 [Register Cloud Connect on Control Hub](https://help.webex.com/en-us/n24wo0fb/Register-Cloud-Connect)
 
 ## BotLite configuration guide 
 [MindMeld (Botlite) configuration Guide](https://github.com/CiscoDevNet/cvp-sample-code/blob/master/CustomerVirtualAssistant/MindMeld/resources/Botlite_documentation.pdf)
 
  ## Element Settings
  
  ### Root element setting
 ![Alt text](https://github.com/CiscoDevNet/cvp-sample-code/blob/master/CustomerVirtualAssistant/MindMeld/resources/root_element_config.png?raw=true "Root Element Setting")
 
  ### Param element setting
 ![Alt text](https://github.com/CiscoDevNet/cvp-sample-code/blob/master/CustomerVirtualAssistant/MindMeld/resources/param_element_config.png?raw=true "Param Element Configuration")
 
 ### Exit States
 ![Alt text](https://github.com/CiscoDevNet/cvp-sample-code/blob/master/CustomerVirtualAssistant/MindMeld/resources/exit_state.png?raw=true "Exit States")
  
  ### Element Output
 ![Alt text](https://github.com/CiscoDevNet/cvp-sample-code/blob/master/CustomerVirtualAssistant/MindMeld/resources/element_data.png?raw=true "Element Output")
  
