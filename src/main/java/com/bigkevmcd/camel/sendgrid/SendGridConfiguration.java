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
    @Metadata(required = true)
    private String from;

    @UriParam(label = "security", secret = true)
    private String apiKey;

    @UriParam
    private String to;

    @UriParam
    private String subject;

    @UriParam
    private List<String> bccAddresses;

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
     * List of bcc email address(es) for the message.
     * Can be overridden using 'CamelSendGridBccAddresses' header.
     * @return
     */
    public List<String> getBccAddresses() {
        return bccAddresses;
    }

    /**
     * Set BCC addresses from a comma-separated string.
     * @param bccAddresses comma-separated email addresses
     */
    public void setBccAddressesFromString(String bccAddresses) {
        if (bccAddresses != null && !bccAddresses.trim().isEmpty()) {
            this.bccAddresses = Arrays.asList(bccAddresses.split(","));
        }
    }

    public void setBccAddresses(String bccAddresses) {
        setBccAddressesFromString(bccAddresses);
    }

    public void setBccAddresses(List<String> bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    public SendGrid getSendGridClient() {
        return sendGridClient;
    }

    /**
     * To use the provided SendGrid as the client.
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