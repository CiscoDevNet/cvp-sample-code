# Call Studio DFAudio Application for Dialogflow element
## Preconditions
* Create the Google DialogFlow Agent by defining Intents / Entities (or import DialogflowAgent.zip into Dialogflow account).
* Define the required parameter and corresponding prompts for parameter for each Intent.
* Perform the fulfilment if you want to use DialogFlow Webhook based fulfilment (DFAudio Flow)
* Configure the Dialogflow Key as suggested in Config guide in "Configuring Google DialogFlow Service" section.

## Application Development
* Receive the Intents from Dialogflow, Handle the exit state to break the loop based on intent.
* Store the values as required from the dialogflow responses and return it with subdialog return callstudio element back to CallServer / ICM, so that based on this routing can happen to right skill set.


