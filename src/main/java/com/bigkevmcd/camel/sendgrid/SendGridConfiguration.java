package com.bigkevmcd.camel.sendgrid;


import com.sendgrid.SendGrid;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.spi.UriPath;

import java.util.Arrays;
import java.util.List;

@UriParams
public class SendGridConfiguration implements Cloneable {
    @UriPath
    @Metadata(required = "true")
    private String from;

    @UriParam(label = "security", secret = true)
    private String apiKey;

    @UriParam
    private List<String> to;

    @UriParam
    private String subject;

    @UriParam
    private List<String> replyToAddresses;

    @UriParam
    private SendGrid sendGridClient;

    /**
     * SendGrid API key.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    /**
     * The sender's email address.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    /**
     * List of destination email addresses.
     *
     * Can be overridden with 'CamelSendGridTo' header.
     * @return
     */
    public List<String> getTo() { return to; }

    public void setTo(List<String> to) { this.to = to; }

    public void setTo(String to) {
        this.to = Arrays.asList(to.split(","));
    }

    /**
     * The subject line for sent emails.
     *
     * Can be overridden with 'CamelSendGridSubject' header.
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * List of reply-to email address(es) for the message.
     * Can be overridden using 'CamelSendGridReplyToAddresses' header.
     * @return
     */
    public List<String> getReplyToAddresses() {
        return replyToAddresses;
    }

    public void setReplyToAddresses(List<String> replyToAddresses) {
        this.replyToAddresses = replyToAddresses;
    }

    public void setReplyToAddresses(String replyToAddresses) {
        this.replyToAddresses = Arrays.asList(replyToAddresses.split(","));
    }

    public SendGrid getSendGridClient() {
        return sendGridClient;
    }

    /**
     * To use the SendGrid as the client
     */
    public void setSendGridClient(SendGrid sendGridClient) {
        this.sendGridClient = sendGridClient;
    }

    public SendGridConfiguration copy() {
        try {
            return (SendGridConfiguration) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeCamelException(e);
        }
    }
}


