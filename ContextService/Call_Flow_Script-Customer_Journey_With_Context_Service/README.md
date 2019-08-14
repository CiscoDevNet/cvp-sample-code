# Introduction
This out-of-the-box CVP call flow enables Cisco CCE customers to deliver a personalized customer and agent experience based on the omnichannel interactions stored in Context Service.

### Prerequisites
This project requires CVP Call Studio.

### Customer Omnichannel Journey History for Agents

This project contains a call flow that demonstrates the customer journey:

1. The script looks up a customer using their home phone number, work phone number, or cell phone number.

 If a customer is found:

 * The lookup returns a unique customer record.
 * The customer is greeted by name.
 * A new activity is created and associated with the customer.

 If the lookup returned no customers or multiple customers:

 * The lookup does not return a customer record.
 * The customer is greeted without a name.
 * A new activity is created, but not associated with a customer.

 If the lookup results in an exception:

 * You are allowed two more retries. After two retries, the customer is greeted without a name. A new activity is then created, but not associated with a customer.

1. The script saves the POD ID to the <code>POD.ID</code> ECC variable.</li>

1. An agent answers the call on the Cisco Finesse agent desktop. The new activity opens on the agent desktop.

 If the activity is associated with a customer:

 * The customer details are shown on the agent desktop.
 * Previous customer journey information is shown on the agent desktop.

 If the activity is not associated with a customer:

 * The agent can enter customer details in a new customer record.

## Getting Started
Import the CustomerJourney files into CVP Call Studio as an existing project.