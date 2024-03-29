package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.Request;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.camel.test.junit5.TestSupport.assertIsInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SendGridProducerTest extends SendGridTestSupport {

    @Test
    public void toStringSanitisesUri() throws Exception {
        SendGridComponent component = new SendGridComponent(context);
        SendGridEndpoint endpoint = (SendGridEndpoint) component.createEndpoint("sendgrid://from@example.com?apiKey=zzz");
        SendGridProducer producer = (SendGridProducer) endpoint.createProducer();
        assertEquals("SendGridProducer[sendgrid://from@example.com?apiKey=xxxxxx]", producer.toString());
    }

    @Test
    public void process() throws InterruptedException, IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put(SendGridConstants.FROM, "anotherFrom@example.com");
        headers.put(SendGridConstants.TO, "anotherTo1@example.com");
        headers.put(SendGridConstants.SUBJECT, "anotherSubject");
        doReturn(createResponse()).when(mockSendGrid).api(any(Request.class));

        template.sendBodyAndHeaders("direct:start", "Message ", headers);

        MockEndpoint.assertIsSatisfied(context);

    }

    @Test
    public void processHandlesExceptions() throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put(SendGridConstants.FROM, "anotherFrom@example.com");
        headers.put(SendGridConstants.TO, "anotherTo1@example.com");
        headers.put(SendGridConstants.SUBJECT, "anotherSubject");
        when(mockSendGrid.api(any(Request.class))).thenThrow(new IOException("failed request"));


        try {
            template.requestBodyAndHeaders("direct:start", "Message ", headers);
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(IOException.class, e.getCause());
            assertTrue(e.getMessage().contains("Exception occurred during execution on the exchange"), e.getMessage());
        }
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start").to("sendgrid://camel@localhost?sendGridClient=#sendGridClient", "mock:result");
            }
        };

    }
}