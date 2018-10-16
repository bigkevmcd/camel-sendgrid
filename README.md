# SendGrid Component

The sendgrid component supports sending emails with [SendGrid](https://sendgrid.com/).

## Prerequisites

You must have a valid SendGrid account.

### URI Format

------------------------
sendgrid://from[?options]
------------------------

You can append query options to the URI in the following format,
?options=value&option2=value&...

### URI Options

The SendGrid component supports 5 options, which are listed below.

|===
| Name | Description | Default | Type
| *configuration* (advanced) | The SendGrid default configuration |  | SendGridConfiguration
| *apiKey* (producer) | SendGrid API Key |  | String
| *resolveProperty Placeholders* (advanced) | Whether the component should resolve property placeholders on itself when starting. Only properties which are of String type can use property placeholders. | true | boolean

The SendGrid endpoint is configured using URI syntax:

----
sendgrid:from
----

with the following path and query parameters:

### Path Parameters (1 parameters):

|Name|Description|Default|Type|
|---|---|---|---|
|*from*|*Required* The sender's email address.| |String|

### Query Parameters (3 parameters):
| Name | Description | Default | Type
| *subject* (producer) | The subject which is used if the message header 'CamelSendGridSubject' is not present. |  | String
| *to* (producer) | List of destination email address. Can be overriden with 'CamelSendGridTo' header. |  | List
| *apiKey* (security) | API Access Key |  | String

Required SendGrid component options

You have to provide the sendGridClient in the Registry or your apiKey.

## Usage

### Message headers evaluated by the SendGrid producer

|Header |Type |Description
|`CamelSendGridFrom` |`String` |The sender's email address.
|`CamelSendGridTo` |`List<String>` |The destination(s) for this email.
|`CamelSendGridSubject` |`String` |The subject of the message.

### Message headers set by the SendGrid producer

|Header |Type |Description

|`CamelSendGridMessageId` |`String` |The SendGrid message ID.

#### Advanced SendGrid configuration

If you need more control over the `SendGrid` instance
configuration you can create your own instance and refer to it from the
URI:

```Java
from("direct:start")
.to("sendgrid://example@example.com?sendGridClient=#sendgrid");
```

The `#sendgrid` refers to a `SendGrid` in the Registry.

```Java
import com.sendgrid.Client;
import com.sendgrid.SendGrid;
Client client = new Client();
SendGrid sendgrid = new SendGrid(apiKey, client);

registry.bind("sendgrid", sendgrid);
```
----------------------------------------------------------------------------------------------------------

### Dependencies

`sendgrid-java`

### See Also

* SendGrid's Java Library](https://github.com/sendgrid/sendgrid-java)
