package com.bigkevmcd.camel.sendgrid;

import org.apache.camel.*;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

/**
 * PagerDuty Endpoint<br/>
 * <p>
 * A PagerDuty Endpoint is defined by the next pattern:<br/>
 *
 * <code>sendgrid:[from]?options]</code>
 */
@UriEndpoint(scheme = "sendgrid", title = "SendGrid", syntax = "sendgrid:from[?options]", producerOnly = true)
public class SendGridEndpoint extends DefaultEndpoint {
    @UriParam
    private SendGridConfiguration configuration;

    @Deprecated
    public SendGridEndpoint(String uri, CamelContext context, SendGridConfiguration configuration) {
        super(uri, context);
        this.configuration = configuration;
    }

    public SendGridEndpoint(String uri, Component component, SendGridConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    @Override
    public Producer createProducer() throws Exception {
        return null;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public SendGridConfiguration getConfiguration() {
        return configuration;
    }
}
