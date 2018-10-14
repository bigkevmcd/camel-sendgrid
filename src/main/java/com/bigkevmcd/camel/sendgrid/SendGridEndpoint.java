package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.SendGrid;
import org.apache.camel.*;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

/**
 * SendGrid Endpoint<br/>
 * <p>
 * A SendGrid Endpoint is defined by the next pattern:<br/>
 *
 * <code>sendgrid:[from]?options]</code>
 */
@UriEndpoint(scheme = "sendgrid", title = "SendGrid", syntax = "sendgrid:from[?options]", producerOnly = true)
public class SendGridEndpoint extends DefaultEndpoint {
    @UriParam
    private SendGridConfiguration configuration;
    private SendGrid sendGridClient;

    @Deprecated
    public SendGridEndpoint(String uri, CamelContext context, SendGridConfiguration configuration) {
        super(uri, context);
        this.configuration = configuration;
    }

    SendGridEndpoint(String uri, Component component, SendGridConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    @Override
    public Producer createProducer() {
        return new SendGridProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    SendGridConfiguration getConfiguration() {
        return configuration;
    }

    SendGrid getSendGridClient() {
        return sendGridClient;
    }

    @Override
    public void doStart() throws Exception {
        super.doStart();
        sendGridClient = configuration.getSendGridClient() != null
                ? configuration.getSendGridClient()
                : createSendGridClient();
    }

    private SendGrid createSendGridClient() {
        return new SendGrid(configuration.getApiKey());
    }
}
