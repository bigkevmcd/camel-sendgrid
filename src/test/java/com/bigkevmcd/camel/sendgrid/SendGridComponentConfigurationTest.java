package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.SendGrid;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SendGridComponentConfigurationTest extends CamelTestSupport {

    @Test
    public void createEndpointWithMinimalConfiguration() throws Exception {
        SendGridComponent component = new SendGridComponent(context);

        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?apiKey=xxx");
        assertEquals("from@example.com", endpoint.getConfiguration().getFrom());
        assertEquals("xxx", endpoint.getConfiguration().getApiKey());
        assertNull(endpoint.getConfiguration().getTo());
        assertNull(endpoint.getConfiguration().getSubject());
        assertNull(endpoint.getConfiguration().getReplyToAddress());
    }

    @Test
    public void createEndpointWithMaximalConfiguration() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?apiKey=zzz"
                + "&to=to1@example.com&subject=Subject"
                + "&replyToAddress=replyTo1@example.com");

        assertEquals("from@example.com", endpoint.getConfiguration().getFrom());
        assertEquals("zzz", endpoint.getConfiguration().getApiKey());
        assertEquals("to1@example.com", endpoint.getConfiguration().getTo());
        assertEquals("Subject", endpoint.getConfiguration().getSubject());
        assertEquals("replyTo1@example.com", endpoint.getConfiguration().getReplyToAddress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEndpointWithoutSourceName() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        component.createEndpoint("sendgrid:// ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createEndpointWithoutApiKeyConfiguration() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        component.createEndpoint("sendgrid://from@example.com");
    }

    @Test
    public void createEndpointWithComponentElements() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        component.setApiKey("XXX");
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com");

        assertEquals("from@example.com", endpoint.getConfiguration().getFrom());
        assertEquals("XXX", endpoint.getConfiguration().getApiKey());
    }

    @Test
    public void createEndpointWithComponentAndEndpointElements() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        component.setApiKey("XXX");
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?apiKey=xxxxxx");

        assertEquals("from@example.com", endpoint.getConfiguration().getFrom());
        assertEquals("xxxxxx", endpoint.getConfiguration().getApiKey());
    }

    @Test
    public void createEndpointWithProvidedClient() throws Exception {
        SendGrid mock = mock(SendGrid.class);

        ((JndiRegistry) ((PropertyPlaceholderDelegateRegistry) context.getRegistry()).getRegistry())
                .bind("sendGridClient", mock);

        SendGridComponent component = new SendGridComponent(context);
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?"
                + "sendGridClient=#sendGridClient");

        assertSame(mock, endpoint.getConfiguration().getSendGridClient());
    }
}
