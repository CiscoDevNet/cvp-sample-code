# Application CustomSIPHeader
Demonstrates the capabilities of Custom SIP Headers in the vxml server.

## Application Behaviour
* User needs to invoke vxml script using Sub_dialog Invoke element.
* In the vxml script, user needs to read the SIP headers (or Custom SIP Headers) using the session variable as session.com.cisco.protoHeaders['Keyname'].
* Any value which is retrieved from SIP headers can be stored in a variable.
* This variable can be used further in any element in the Call Studio Application.
* User can perform any regex opeartion on the value retrieved from SIP headers.
* If there is any PII in SIP header, that key can be restricted from being passed to vxml sever.

## Application Enhancements
* In the vxml properties, add the key "com.cisco.protoHeadersRestricted". Value can be the comma separated list of SIP headers. These values will be restricted from being passed to vxml server. Default value is null. All the headers will flow through if nothing specified.
* Output element data of VXML script can be parsed and used as per requirement with the return value.

## Related Documents 
* to be upadted

## Instructions
* The file attached contains call studio application - CustomSIPHeader_CS
* Deployed application on vxml server -  CustomSIPHeader_Deployed
* Sample SIPp script is included with custom SIP headers added - customSipHeader.xml
* Sample vxml script is included - getstoreinfo.vxml
