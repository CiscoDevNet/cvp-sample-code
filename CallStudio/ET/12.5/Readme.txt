Prerequisite: CVP 12.5.1 FCS Call Studio
Compatible with CVP 12.5 ES 24 onwards.

ET is present in Call_Studio_12.5_ET.zip

This ET has below defects fixed.


CSCvy39652 - DialogFlowParam Element Reset Parameter.
CSCvy39125 - Allow Substitutions for Intent Settings in DialogFllowParam Element
CSCvw09507 - CVP application with WSDL takes long time to load upon clicking decision editor.
CSCvy53187 - Decision element gives an error: "An error has occurred. See error log for more details.

Instruction to patch call studio

1. Close call studio IDE
2. Replace elements.jar in below locations
    --CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.debug.runtime\AUDIUM_HOME\common\lib
    --CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.elements.core\lib
	
3. Replace builder-core.jar in below locations
	--CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.builder.core
	
3. Replace elementConfig.jar in below locations
	--CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.builder.elementconfig
	

Note: Take a back up of jars before replacing.



