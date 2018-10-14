[[sendgrid-component]]
== SendGrid Component

The sendgrid component supports sending emails with
https://sendgrid.com/[SendGrid].

Prerequisites

You must have a valid SendGrid account.

### URI Format

[source,java]
------------------------
sendgrid://from[?options]
------------------------

You can append query options to the URI in the following format,
?options=value&option2=value&...

### URI Options

// component options: START
The SendGrid component supports 5 options, which are listed below.

[width="100%",cols="2,5,^1,2",options="header"]
|===
| Name | Description | Default | Type
| *configuration* (advanced) | The SendGrid default configuration |  | SendGridConfiguration
| *apiKey* (producer) | SendGrid API Key |  | String
| *resolveProperty Placeholders* (advanced) | Whether the component should resolve property placeholders on itself when starting. Only properties which are of String type can use property placeholders. | true | boolean
|===
// component options: END

// endpoint options: START
The SendGrid endpoint is configured using URI syntax:

----
sendgrid:from
----

with the following path and query parameters:

==== Path Parameters (1 parameters):


[width="100%",cols="2,5,^1,2",options="header"]
|===
| Name | Description | Default | Type
| *from* | *Required* The sender's email address. |  | String
|===


==== Query Parameters (4 parameters):

[width="100%",cols="2,5,^1,2",options="header"]
|===
| Name | Description | Default | Type
| *replyToAddresses* (producer) | List of reply-to email address(es) for the message, override it using 'CamelSendGridReplyToAddresses' header. |  | List
| *subject* (producer) | The subject which is used if the message header 'CamelSendGridSubject' is not present. |  | String
| *to* (producer) | List of destination email address. Can be overriden with 'CamelSendGridTo' header. |  | List
| *apiKey* (security) | API Access Key |  | String
|===
// endpoint options: END

Required SendGrid component options

You have to provide the sendGridClient in the
Registry or your apiKey.

### Usage

#### Message headers evaluated by the SendGrid producer

[width="100%",cols="10%,10%,80%",options="header",]
|=======================================================================
|Header |Type |Description

|`CamelSendGridFrom` |`String` |The sender's email address.

|`CamelSendGridTo` |`List<String>` |The destination(s) for this email.

|`CamelSendGridSubject` |`String` |The subject of the message.

|=======================================================================

#### Message headers set by the SendGridS producer

[width="100%",cols="10%,10%,80%",options="header",]
|=======================================================================
|Header |Type |Description

|`CamelSendGridMessageId` |`String` |The SendGrid message ID.
|=======================================================================

#### Advanced SendGrid configuration

If you need more control over the `SendGrid` instance
configuration you can create your own instance and refer to it from the
URI:

[source,java]
-------------------------------------------------------------
from("direct:start")
.to("sendgrid://example@example.com?sendGridClient=#sendgrid");
-------------------------------------------------------------

The `#sendgrid` refers to a `SendGrid` in the
Registry.

[source,java]
----------------------------------------------------------------------------------------------------------
import com.sendgrid.Client;
import com.sendgrid.SendGrid;
Client client = new Client();
SendGrid sendgrid = new SendGrid(apiKey, client);

registry.bind("sendgrid", sendgrid);
----------------------------------------------------------------------------------------------------------

### Dependencies

// TODO

### See Also

* []SendGrid's Java Library](https://github.com/sendgrid/sendgrid-java)
s