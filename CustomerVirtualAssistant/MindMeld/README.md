This Section contains souce code to build MindMeld callstudio element and sample callstudio application to demonstrate capability of MindMeld with CVP.

# Instruction to set up VXML server

## Prerequisite
  * CVP and Call Studio 12.6 latest Build
  
  ## Call Studio setup
  * cd into  element_sourcecode code and with maven (mvn clean package).
  * Place the compiled jar in <CALLSTUDIO_HOME>\eclipse\plugins\com.audiumcorp.studio.elements.core\lib
  * Close and reopen Call Studio.
  * MindMeld element appears under "Customer Virtual Assistant" in Elements section.
  
  ## VXML Server setup
  * cd into  element_sourcecode code and with maven (mvn clean package).
  * Stop VXML server
  * Place the compiled jar in <CVP_HOME>\VXMLServer\common\lib
  * Restart VXML server.
  
   ## Deploy sample Call Studio app
  * download callstudio_app.zip
  * import into Call Studio
  * Validate it and deploy into vxml server
  
  
  
  

