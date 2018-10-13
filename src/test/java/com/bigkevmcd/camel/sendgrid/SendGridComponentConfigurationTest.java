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
        assertNull(endpoint.getConfiguration().getReplyToAddresses());
    }


    @Test
    public void createEndpointWithMaximalConfiguration() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?apiKey=zzz"
                + "&to=to1@example.com,to2@example.com&subject=Subject"
                + "&replyToAddresses=replyTo1@example.com,replyTo2@example.com");

        assertEquals("from@example.com", endpoint.getConfiguration().getFrom());
        assertEquals("zzz", endpoint.getConfiguration().getApiKey());
        assertEquals(2, endpoint.getConfiguration().getTo().size());
        assertTrue(endpoint.getConfiguration().getTo().contains("to1@example.com"));
        assertTrue(endpoint.getConfiguration().getTo().contains("to2@example.com"));
        assertEquals("Subject", endpoint.getConfiguration().getSubject());
        assertEquals(2, endpoint.getConfiguration().getReplyToAddresses().size());
        assertTrue(endpoint.getConfiguration().getReplyToAddresses().contains("replyTo1@example.com"));
        assertTrue(endpoint.getConfiguration().getReplyToAddresses().contains("replyTo2@example.com"));
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
                .bind("sendgridClient", mock);

        SendGridComponent component = new SendGridComponent(context);
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?"
                + "sendgridClient=#sendgridClient");

        assertSame(mock, endpoint.getConfiguration().getSendGridClient());
    }
}
