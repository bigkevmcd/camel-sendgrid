package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.*;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class SendGridComponentTest extends SendGridTestSupport {

    @Test
    public void sendInOnlyMessageUsingUrlOptions() throws Exception {
        doReturn(createResponse()).when(mockSendGrid).api(any(Request.class));

        Exchange exchange = template.send("direct:start", exchange1 -> exchange1.getIn().setBody("This is my message text."));

        assertEquals(MESSAGE_ID, exchange.getIn().getHeader(SendGridConstants.MESSAGE_ID));
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(mockSendGrid).api(captor.capture());

        Mail expected = createMail("from@example.com", "to1@example.com", "Subject", "This is my message text.");
        assertEquals(expected.build(), captor.getValue().getBody());
    }

    @Test
    public void sendMessageUsingMessageHeaders() throws Exception {
        doReturn(createResponse()).when(mockSendGrid).api(any(Request.class));
        Exchange exchange = template.send("direct:start", exchange1 -> {
            exchange1.getIn().setBody("This is the message.");
            exchange1.getIn().setHeader(SendGridConstants.FROM, "anotherFrom@example.com");
            exchange1.getIn().setHeader(SendGridConstants.TO, "anotherTo1@example.com");
            exchange1.getIn().setHeader(SendGridConstants.SUBJECT, "anotherSubject");
        });

        assertEquals(MESSAGE_ID, exchange.getIn().getHeader(SendGridConstants.MESSAGE_ID));
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(mockSendGrid).api(captor.capture());

        Mail expected = createMail("anotherFrom@example.com", "anotherTo1@example.com", "anotherSubject", "This is the message.");
        assertEquals(expected.build(), captor.getValue().getBody());
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

    private Mail createMail(String from, String to, String subject, String body) {
        Content content = new Content("text/plain", body);
        return new Mail(new Email(from), subject, new Email(to), content);
    }
}