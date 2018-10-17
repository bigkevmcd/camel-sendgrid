package com.bigkevmcd.camel.sendgrid;

/**
 * Constants used in Camel SendGrid component.
 */
public interface SendGridConstants {
    /**
     * These can be used in the input exchange to configure the mail that is sent.
     */
    String FROM = "CamelSendGridFrom";
    String MESSAGE_ID = "CamelSendGridMessageId";
    String SUBJECT = "CamelSendGridSubject";
    String TO = "CamelSendGridTo";
    String BCC_ADDRESSES = "CamelSendGridBcc";

    String RESPONSE_MESSAGE_ID = "X-Message-Id";
}
