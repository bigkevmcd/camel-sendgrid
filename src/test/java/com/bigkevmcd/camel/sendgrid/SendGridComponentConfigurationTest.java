package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.SendGrid;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertSame;
import static org.junit.jupiter.api.Assertions.*;
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
        assertNull(endpoint.getConfiguration().getBccAddresses());
    }

    @Test
    public void createEndpointWithMaximalConfiguration() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?apiKey=zzz"
                + "&to=to1@example.com&subject=Subject"
                + "&bccAddresses=bcc1@example.com,bcc2@example.com");

        assertEquals("from@example.com", endpoint.getConfiguration().getFrom());
        assertEquals("zzz", endpoint.getConfiguration().getApiKey());
        assertEquals("to1@example.com", endpoint.getConfiguration().getTo());
        assertEquals("Subject", endpoint.getConfiguration().getSubject());
        assertEquals(2, endpoint.getConfiguration().getBccAddresses().size());
        assertTrue(endpoint.getConfiguration().getBccAddresses().contains("bcc1@example.com"));
        assertTrue(endpoint.getConfiguration().getBccAddresses().contains("bcc2@example.com"));
    }

    @Test
    public void createEndpointWithoutSourceName() throws Exception {
        SendGridComponent component = new SendGridComponent(context);

        assertThrows(IllegalArgumentException.class, () -> {
            component.createEndpoint("sendgrid:// ");
        });
    }

    @Test
    public void createEndpointWithoutApiKeyConfiguration() {
        SendGridComponent component = new SendGridComponent(context);


        assertThrows(IllegalArgumentException.class, () -> {
            component.createEndpoint("sendgrid://from@example.com");
        });
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

        context.getRegistry().bind("sendGridClient", mock);

        SendGridComponent component = new SendGridComponent(context);
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?"
                + "sendGridClient=#sendGridClient");

        assertSame(mock, endpoint.getConfiguration().getSendGridClient());
    }
}
