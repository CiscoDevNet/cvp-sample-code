# Call Studio DFText Application for Dialogflow element
## Preconditions
* Create the Google DialogFlow Agent by defining Intents / Entities (or import DialogflowAgent.zip into Dialogflow account).
* Define the required parameter and corresponding prompts for parameter for each Intent.
* Perform the fulfilment if you want to use DialogFlow Webhook based fulfilment (DFText Flow)
* Configure the Dialogflow Key as suggested in Config guide in "Configuring Google Dialogflow Service" section.
* Configure the TTS Key as suggested in Config guide in "Configuring Google Dialogflow Service" section.

## Application Development
* Play the welcome Prompt using TTS / Wav file.
* Handle the fulfilment response from Dialogflow response and synthesize it using audio element which will play it. Audio element can use [SSML](https://cloud.google.com/text-to-speech/docs/ssml) format to change language, voice etc.
* Handle the exit state to break the loop based on intent.
* Store the values as required from the dialogflow responses and return it with subdialog return callstudio element back to CallServer / ICM, so that based on this routing can happen to right skill set.
