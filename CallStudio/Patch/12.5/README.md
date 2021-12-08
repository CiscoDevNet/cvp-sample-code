## Prerequisite: 
CVP 12.5.1 FCS Call Studio
Compatible with CVP 12.5 latest ES.

Note: Take a back up of jars before replacing.

Patch is present in Call_Studio_12.5_Patch.zip

## Defects fixed.

* CSCvy39652 - DialogFlowParam Element Reset Parameter.
* CSCvy39125 - Allow Substitutions for Intent Settings in DialogFllowParam Element
* CSCvw09507 - CVP application with WSDL takes long time to load upon clicking decision editor.
* CSCvy53187 - Decision element gives an error: "An error has occurred. See error log for more details.

## Instructions to patch call studio 12.5

1. Close call studio IDE
2. Replace elements.jar in 
    --CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.debug.runtime\AUDIUM_HOME\common\lib
    --CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.elements.core\lib
	
3. Replace builder-core.jar in
	--CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.builder.core
	
3. Replace elementConfig.jar in
	--CALLSTUDIO_HOME\eclipse\plugins\com.audiumcorp.studio.builder.elementconfig
	




