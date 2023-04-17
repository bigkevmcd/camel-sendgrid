package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * SendGridProducer sends the emails to SendGrid.
 */
class SendGridProducer extends DefaultProducer {
    private transient String sendGridProducerToString;
    private static final Pattern SECRET = Pattern.compile("([?&][^=]*(?:apiKey)[^=]*)=([^&]*)", Pattern.CASE_INSENSITIVE);

    SendGridProducer(SendGridEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Request request;
        if ((exchange.getIn().getBody() instanceof Mail)) {
            request = createMailRequest((Mail) exchange.getIn().getBody());
        } else {
            Mail mail = createMailRequest(exchange);
            request = createMailRequest(mail);
        }

        try {
            Response response = getEndpoint().getSendGridClient().api(request);
            String messageId = response.getHeaders().get(SendGridConstants.RESPONSE_MESSAGE_ID);
            exchange.getIn().setHeader(SendGridConstants.MESSAGE_ID, messageId);
        } catch (IOException e) {
            exchange.setException(e);
        }
    }

    private Mail createMailRequest(Exchange exchange) {
        Email from = new Email(determineFrom(exchange));
        Email to = determineTo(exchange);
        String subject = determineSubject(exchange);
        Content content = new Content("text/plain", exchange.getIn().getBody(String.class));
        Mail mail = new Mail(from, subject, to, content);

        determineBccAddresses(exchange).ifPresent((bccAddresses) -> {
            for(String address: bccAddresses) {
                mail.getPersonalization().add(createBcc(address));
            }
        });
        return mail;
    }

    private Request createMailRequest(Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        return request;
    }

    @Override
    public SendGridEndpoint getEndpoint() {
        return (SendGridEndpoint) super.getEndpoint();
    }

    private String determineFrom(Exchange exchange) {
        String from = exchange.getIn().getHeader(SendGridConstants.FROM, String.class);
        if (from == null) {
            from = getConfiguration().getFrom();
        }
        return from;
    }

    private Email determineTo(Exchange exchange) {
        String to = exchange.getIn().getHeader(SendGridConstants.TO, String.class);
        if (to == null) {
            to = getConfiguration().getTo();
        }
        return new Email(to);
    }

    private String determineSubject(Exchange exchange) {
        String subject = exchange.getIn().getHeader(SendGridConstants.SUBJECT, String.class);
        if (subject == null) {
            subject = getConfiguration().getSubject();
        }
        return subject;
    }

    private Optional<List<String>> determineBccAddresses(Exchange exchange) {
        List<String> bccAddresses = exchange.getIn().getHeader(SendGridConstants.BCC_ADDRESSES, List.class);
        if (bccAddresses == null) {
            bccAddresses = getConfiguration().getBccAddresses();
        }
        return Optional.ofNullable(bccAddresses);
    }

    private Personalization createBcc(String email) {
        Email bcc = new Email();
        bcc.setEmail(email);
        Personalization personalization = new Personalization();
        personalization.addBcc(bcc);
        return personalization;
    }

    @Override
    public String toString() {
        if (sendGridProducerToString == null) {
            sendGridProducerToString = "SendGridProducer[" + sanitizeUri(getEndpoint().getEndpointUri()) + "]";
        }
        return sendGridProducerToString;
    }

    private String sanitizeUri(String uri) {
        return SECRET.matcher(uri).replaceAll("$1=xxxxxx");
    }

    private SendGridConfiguration getConfiguration() {
        return getEndpoint().getConfiguration();
    }

}
