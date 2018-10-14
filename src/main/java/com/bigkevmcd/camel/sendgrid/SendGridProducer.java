package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.*;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SendGridProducer sends the emails to SendGrid.
 */
public class SendGridProducer extends DefaultProducer {
    public SendGridProducer(SendGridEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if ((exchange.getIn().getBody() instanceof Mail)) {
            Request request = createMailRequest((Mail)exchange.getIn().getBody());
            log.trace("Sending request [{}] from exchange [{}]...", request, exchange);
            Response response = getEndpoint().getSendGridClient().api(request);
            log.trace("Received result [{}]", response);
            exchange.getIn().setHeader(SendGridConstants.MESSAGE_ID, response.getHeaders().get(SendGridConstants.RESPONSE_MESSAGE_ID));
        } else {
            try {
                Mail mail = createMailRequest(exchange);
                Request request = createMailRequest(mail);
                log.trace("Sending request [{}] from exchange [{}]...", request, exchange);
                Response response = getEndpoint().getSendGridClient().api(request);
                log.trace("Received result [{}]", response);
                exchange.getIn().setHeader(SendGridConstants.MESSAGE_ID, response.getHeaders().get(SendGridConstants.RESPONSE_MESSAGE_ID));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private Mail createMailRequest(Exchange exchange) {
        Email from = new Email(determineFrom(exchange));
        Email to = determineTo(exchange);
        String subject = determineSubject(exchange);
        Content content = new Content("text/plain", exchange.getIn().getBody(String.class));
        Mail mail = new Mail(from, subject, to, content);
        return mail;
    }

    private Request createMailRequest(Mail mail) {
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            return request;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    protected SendGridConfiguration getConfiguration() {
        return getEndpoint().getConfiguration();
    }

}
