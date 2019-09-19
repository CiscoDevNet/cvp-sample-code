**Introduction**

This call studio application is the editable version of the sample survey application which will be deployed by default with CVP. It supports 3 types of surveys - CSAT, CES and NPS.

**Customization**

If needed, this app can be customized in multiple ways:

1. To change the content/language of the prompts played by default, replace the audio prompts by name *cx_\*.wav* deployed with CVP. Make sure to record the new audio files using the same codec that was chosen during CVP installation. 

2. Customize this application to suit your custom survey needs, by including new/different survey types or new/more audio files. Make sure to deploy the new/updated app(s) on your CVP VXML Server(s).

**Testing**

By default, this application will be testable only in a comprehensive setup with the required CX KPI configurations in place. To test it using a standalone call, add a session variable called *surveyList* in the '*Data*' tab of the '*Welcome*' audio prompt element configuration, with values like *2:3:1* or *1:3:2*. The convention is: *1 = CSAT, 2 = CES, 3 = NPS*. 
