package com.bigkevmcd.camel.sendgrid;

/**
 * Constants used in Camel SendGrid component.
 */
public interface SendGridConstants {
    String FROM = "CamelSendGridFrom";
    String MESSAGE_ID = "CamelSendGridMessageId";
    String SUBJECT = "CamelSendGridSubject";
    String TO = "CamelSendGridTo";

    String RESPONSE_MESSAGE_ID = "X-Message-Id";
}
