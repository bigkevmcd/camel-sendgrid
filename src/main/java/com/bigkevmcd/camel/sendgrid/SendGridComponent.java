package com.bigkevmcd.camel.sendgrid;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.Metadata;
import org.apache.camel.util.ObjectHelper;

import java.util.Map;

/**
 * SendGrid component to allow sending of emails through SendGrid.
 */
public class SendGridComponent extends DefaultComponent {
    @Metadata
    private String apiKey;
    @Metadata(label = "advanced")
    private SendGridConfiguration configuration;

    public SendGridComponent() {
        this(null);
    }

    public SendGridComponent(CamelContext context) {
        super(context);
        this.configuration = new SendGridConfiguration();
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        SendGridConfiguration configuration = this.configuration.copy();
        setProperties(configuration, parameters);

        if (remaining == null || remaining.trim().length() == 0) {
            throw new IllegalArgumentException("From must be specified.");
        }
        configuration.setFrom(remaining);

        if (ObjectHelper.isEmpty(configuration.getApiKey())) {
            setApiKey(apiKey);
        }

        if (configuration.getSendGridClient() == null && configuration.getApiKey() == null) {
            throw new IllegalArgumentException("apiKey must be specified");
        }
        return new SendGridEndpoint(uri, this, configuration);
    }

    /**
     * The API key to authenticate with SendGrid.
     * @return
     */
    public String getApiKey() {
        return configuration.getApiKey();
    }

    public void setApiKey(String apiKey) {
        configuration.setApiKey(apiKey);
    }
}
