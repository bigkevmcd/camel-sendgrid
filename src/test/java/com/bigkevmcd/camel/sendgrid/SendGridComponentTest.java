package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.Request;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SendGridComponentTest extends SendGridTestSupport {

    @Test
    public void sendInOnlyMessageUsingUrlOptions() throws Exception {
        doReturn(createResponse()).when(mockSendGrid).api(any(Request.class));

        Exchange exchange = template.send("direct:start", exchange1 -> exchange1.getIn().setBody("This is my message text."));

        assertEquals(MESSAGE_ID, exchange.getIn().getHeader(SendGridConstants.MESSAGE_ID));
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(mockSendGrid).api(captor.capture());

        List<String> bcc = Arrays.asList("bcc1@example.com","bcc2@example.com");
        Mail expected = createMail(
                "from@example.com", "to1@example.com",
                "Subject", "This is my message text.", bcc
        );
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
            exchange1.getIn().setHeader(
                    SendGridConstants.BCC_ADDRESSES,
                    Arrays.asList("bcc3@example.com", "bcc4@example.com"));
        });

        assertEquals(MESSAGE_ID, exchange.getIn().getHeader(SendGridConstants.MESSAGE_ID));
        ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
        verify(mockSendGrid).api(captor.capture());

        List<String> bcc = Arrays.asList("bcc3@example.com","bcc4@example.com");
        Mail expected = createMail(
                "anotherFrom@example.com", "anotherTo1@example.com",
                "anotherSubject", "This is the message.", bcc
        );
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
                                + "&bccAddresses=bcc1@example.com,bcc2@example.com"
                                + "&sendGridClient=#sendGridClient");
            }
        };
    }

    private Mail createMail(String from, String to, String subject, String body, List<String> bccAddresses) {
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(new Email(from), subject, new Email(to), content);

        for (String address : bccAddresses) {
            Email bcc = new Email();
            bcc.setEmail(address);
            Personalization personalization = new Personalization();
            personalization.addBcc(bcc);
            mail.getPersonalization().add(personalization);
        }
        return mail;
    }
}