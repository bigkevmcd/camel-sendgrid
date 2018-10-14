package com.bigkevmcd.camel.sendgrid;


import com.sendgrid.SendGrid;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.spi.UriPath;

@UriParams
public class SendGridConfiguration implements Cloneable {
    @UriPath
    @Metadata(required = "true")
    private String from;

    @UriParam(label = "security", secret = true)
    private String apiKey;

    @UriParam
    private String to;

    @UriParam
    private String subject;

    @UriParam
    private String replyToAddress;

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
    public String getTo() { return to; }

    public void setTo(String to) { this.to = to; }

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
    public String getReplyToAddress() {
        return replyToAddress;
    }

    public void setReplyToAddress(String replyToAddress) {
        this.replyToAddress = replyToAddress;
    }

    public SendGrid getSendGridClient() {
        return sendGridClient;
    }

    /**
     * To use the provided SendGrid as the client.
     */
    public void setSendGridClient(SendGrid sendgrid) {
        this.sendGridClient = sendgrid;
    }

    public void setSendgridClient(String client) {
        this.sendGridClient = null;
    }

    public SendGridConfiguration copy() {
        try {
            return (SendGridConfiguration) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeCamelException(e);
        }
    }
}


