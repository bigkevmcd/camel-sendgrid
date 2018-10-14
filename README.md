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
// spring-boot-auto-configure options: START
=== Spring Boot Auto-Configuration

The component supports 16 options, which are listed below.

[width="100%",cols="2,5,^1,2",options="header"]
|===
| Name | Description | Default | Type
| *camel.component.sendgrid.access-key* | Amazon AWS Access Key |  | String
| *camel.component.sendgrid.configuration.access-key* | Amazon AWS Access Key |  | String
| *camel.component.sendgrid.configuration.amazon-s-e-s-client* | To use the AmazonSimpleEmailService as the client |  | AmazonSimpleEmail Service
| *camel.component.sendgrid.configuration.from* | The sender's email address. |  | String
| *camel.component.sendgrid.configuration.proxy-host* | To define a proxy host when instantiating the SES client |  | String
| *camel.component.sendgrid.configuration.proxy-port* | To define a proxy port when instantiating the SES client |  | Integer
| *camel.component.sendgrid.configuration.region* | The region in which SES client needs to work |  | String
| *camel.component.sendgrid.configuration.reply-to-addresses* | List of reply-to email address(es) for the message, override it using 'CamelAwsSesReplyToAddresses' header. |  | List
| *camel.component.sendgrid.configuration.return-path* | The email address to which bounce notifications are to be forwarded, override it using 'CamelAwsSesReturnPath' header. |  | String
| *camel.component.sendgrid.configuration.secret-key* | Amazon AWS Secret Key |  | String
| *camel.component.sendgrid.configuration.subject* | The subject which is used if the message header 'CamelAwsSesSubject' is not present. |  | String
| *camel.component.sendgrid.configuration.to* | List of destination email address. Can be overriden with 'CamelAwsSesTo' header. |  | List
| *camel.component.sendgrid.enabled* | Enable sendgrid component | true | Boolean
| *camel.component.sendgrid.region* | The region in which SES client needs to work |  | String
| *camel.component.sendgrid.resolve-property-placeholders* | Whether the component should resolve property placeholders on itself when starting. Only properties which are of String type can use property placeholders. | true | Boolean
| *camel.component.sendgrid.secret-key* | Amazon AWS Secret Key |  | String
|===
// spring-boot-auto-configure options: END




Required SES component options

You have to provide the amazonSESClient in the
Registry or your accessKey and secretKey to access
the http://aws.amazon.com/ses[Amazon's SES].

### Usage

#### Message headers evaluated by the SES producer

[width="100%",cols="10%,10%,80%",options="header",]
|=======================================================================
|Header |Type |Description

|`CamelAwsSesFrom` |`String` |The sender's email address.

|`CamelAwsSesTo` |`List<String>` |The destination(s) for this email.

|`CamelAwsSesSubject` |`String` |The subject of the message.

|`CamelAwsSesReplyToAddresses` |`List<String>` |The reply-to email address(es) for the message.

|`CamelAwsSesReturnPath` |`String` |The email address to which bounce notifications are to be forwarded.

|`CamelAwsSesHtmlEmail` |`Boolean` |*Since Camel 2.12.3* The flag to show if email content is HTML.
|=======================================================================

#### Message headers set by the SES producer

[width="100%",cols="10%,10%,80%",options="header",]
|=======================================================================
|Header |Type |Description

|`CamelAwsSesMessageId` |`String` |The Amazon SES message ID.
|=======================================================================

#### Advanced AmazonSimpleEmailService configuration

If you need more control over the `AmazonSimpleEmailService` instance
configuration you can create your own instance and refer to it from the
URI:

[source,java]
-------------------------------------------------------------
from("direct:start")
.to("sendgrid://example@example.com?amazonSESClient=#client");
-------------------------------------------------------------

The `#client` refers to a `AmazonSimpleEmailService` in the
Registry.

For example if your Camel Application is running behind a firewall:

[source,java]
----------------------------------------------------------------------------------------------------------
AWSCredentials awsCredentials = new BasicAWSCredentials("myAccessKey", "mySecretKey");
ClientConfiguration clientConfiguration = new ClientConfiguration();
clientConfiguration.setProxyHost("http://myProxyHost");
clientConfiguration.setProxyPort(8080);
AmazonSimpleEmailService client = new AmazonSimpleEmailServiceClient(awsCredentials, clientConfiguration);

registry.bind("client", client);
----------------------------------------------------------------------------------------------------------

### Dependencies

Maven users will need to add the following dependency to their pom.xml.

*pom.xml*

[source,xml]
---------------------------------------
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-aws</artifactId>
    <version>${camel-version}</version>
</dependency>
---------------------------------------

where `${camel-version`} must be replaced by the actual version of Camel
(2.8.4 or higher).

### See Also

* Configuring Camel
* Component
* Endpoint
* Getting Started

* AWS Component
