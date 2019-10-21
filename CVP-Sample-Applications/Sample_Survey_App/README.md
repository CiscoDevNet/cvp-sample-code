**Introduction**

This call studio application is a sample (post call) survey application. It supports 3 standard types of surveys - CSAT, CES and NPS.

**Customization**

This app can be customized in multiple ways:

1. Provide the the prompts to be played for each survey type by placing these files using names *cx_\*.wav* corresponding to the names specified in the application. Make sure to record the audio files using the same codec that was chosen during CVP installation. 

2. Customize this application to suit your custom survey needs, by including new/different survey types or new/more audio files. Make sure to deploy the new/updated app(s) on your CVP VXML Server(s).

**Testing**

By default, this application will be testable only in a comprehensive setup with the usual PCS configurations in place. To test it using a standalone call, add a session variable called *surveyList* in the '*Data*' tab of the '*Welcome*' audio prompt element configuration, with values like *2:3:1* or *1:3:2*. The convention is: *1 = CSAT, 2 = CES, 3 = NPS*. 
