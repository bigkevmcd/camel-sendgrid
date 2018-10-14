package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SendGridComponentTest extends CamelTestSupport {

    public static final String MESSAGE_ID = "kA5kcHA8SVGtVh5S9rAUew";
    private SendGrid mockSendGrid = mock(SendGrid.class);

    @Test
    public void sendInOnlyMessageUsingUrlOptions() throws Exception {
        doReturn(createResponse(200, MESSAGE_ID)).when(mockSendGrid).api(any(Request.class));

        Exchange exchange = template.send("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) {
                exchange.getIn().setBody("This is my message text.");
            }
        });

        assertEquals(MESSAGE_ID, exchange.getIn().getHeader(SendGridConstants.MESSAGE_ID));
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(mockSendGrid).api(captor.capture());

        Mail expected = createMail("from@example.com", "to1@example.com", "Subject", "This is my message text.");
        assertEquals(expected.build(), captor.getValue().getBody());
    }

    @Test
    public void sendMessageUsingMessageHeaders() throws Exception {
        doReturn(createResponse(200, MESSAGE_ID)).when(mockSendGrid).api(any(Request.class));
        Exchange exchange = template.send("direct:start", new Processor() {
            public void process(Exchange exchange) {
                exchange.getIn().setBody("This is the message.");
                exchange.getIn().setHeader(SendGridConstants.FROM, "anotherFrom@example.com");
                exchange.getIn().setHeader(SendGridConstants.TO, "anotherTo1@example.com");
                exchange.getIn().setHeader(SendGridConstants.SUBJECT, "anotherSubject");
            }
        });

        assertEquals(MESSAGE_ID, exchange.getIn().getHeader(SendGridConstants.MESSAGE_ID));
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(mockSendGrid).api(captor.capture());

        Mail expected = createMail("anotherFrom@example.com", "anotherTo1@example.com", "anotherSubject", "This is the message.");
        assertEquals(expected.build(), captor.getValue().getBody());
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        registry.bind("sendGridClient", mockSendGrid);
        return registry;
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start")
                        .to("sendgrid://from@example.com"
                                + "?to=to1@example.com"
                                + "&subject=Subject"
                                + "&sendGridClient=#sendGridClient");
            }
        };
    }

    private Response createResponse(int statusCode, String messageId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Message-Id", messageId);
        Response response = new Response();
        response.setStatusCode(200);
        response.setHeaders(headers);
        return response;
    }

    private Mail createMail(String from, String to, String subject, String body) {
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(new Email(from), subject, new Email(to), content);
        return mail;
    }
}