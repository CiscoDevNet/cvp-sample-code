
# Application for Custom Code Exceution on Remote Server
Demonstrates the capabilities of Callstudio Custom Elements executed on Remote Server with graceful error handling.

## Application Behaviour
* Call studio Application - Properties - Remote Url settings has been added as part of this feature. 
  Values configured in the application are:
  Static- 127.0.0.1(localhost) Port- 8090
  Type- RPC
  These values related to remote servers has to be configured according to the requirement.
  (Refer to Programming/Configuration guide- Remote server for more details)
* Option 1 in Choose One( Menu Element)is added to fetch customer name based on PIN entered ( which is configured as "123" in this application) and then validate it by fetching the EmployeeName
* Option 2 is added to ask for user confirmation for fetching balance
* Option 3 is added to demonstrate event exception handling in case encountered
  For this app, provided wrong class name under remote execution value to get "ClassNotFoundException"

## Application Enhancements
* The HTTP port listens at "8080",  HTTP secure port listens at "8084" and GRPC port listens at "8090".
* For remote execution of the Elements, the following syntax for URI is to be used 
  For HTTP and RPC call-  remote://system/?classurl=<fully_qualified_java_class_path>

  "remote://system" indicates that the configurations will be fetched from the Remote Url Settings property tab which is application specific.

  Note:- In case direct remote server URI is provided, then that IP:Port will be used and not fetched from the  Remote Url Settings property tab.
  ex- http://<IP>:<Port>/<target_path>/?classurl=<fully_qualified_java-class_path>
* Remote Execution checkbox has been added in the General Settings Tab of configurable voice, action and decision elements.
On selecting the checkbox, the default URI mentioned below is being auto populated.
For HTTP and RPC call-  remote://system/?classurl=<fully_qualified_java_class_path>
* A checkbox named "Remote Execution" has been added to the Say It Smart Plugin for executing it remotely.
  In case enabled, it will use the URI values provided in the Remote Url Settings tab.

  Note: Say It Smart plugin gets remotely executed for RPC type and not HTTP connection type.



## Feature guide
* [ Configuration guide: Remote server](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/12-6-2/configuration/guide/ccvp_b_1262-configuration-guide-for-cisco-unified-customer-voice-portal.html)
* [ Programming guide ](https://www.cisco.com/c/en/us/td/docs/voice_ip_comm/cust_contact/contact_center/customer_voice_portal/12-6-2/programming/guide/ccvp_b_1262-programming-guide-for-cisco-unified-cvp-vxml-server-and-cisco-unified-call-studio1.html)


## Instructions
* The file attached contains call studio application- inside 'BankingAppCustomCode_CS" folder
* Deployed application on Vxml Server-  inside "BankingAppCustomCode_deployed" folder
* Add the "CustomCode.jar" attached in zip folder at the following location in Call Studio- CallStudio\eclipse\plugins\com.audiumcorp.studio.elements.core and re-open Call Studio for changes to be reflected in Call Studio Builder.
* "CustomCode.jar" is to be added on the remote server at location- %Apache Software Foundation%\webapps\customapis\WEB-INF\lib and then re-start the server.
* Logs can be monitored at- 

   For VXML-  %CVP_HOME%\logs\VXML\CVP <timestamp>.log
              %CVP_HOME%\logs\VXML\ERROR <timestamp>.log
              %CVP_HOME%\VXMLserver\application\<applicationname>\logs

   For Remote Server- %Apache Software Foundation%\Tomcat 9.0\logs\cvp.log


