package com.bigkevmcd.camel.sendgrid.integration;

import com.bigkevmcd.camel.sendgrid.SendGridConstants;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("Must be manually tested. An API key must be provided in SENDGRID_API_KEY")
public class SendGridComponentIntegrationTest extends CamelTestSupport {

    @Test
    public void sendUsingAccessKeyAndSecretKey() throws Exception {
        Exchange exchange = template.send("direct:start", ExchangePattern.InOnly, new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(SendGridConstants.SUBJECT, "This is my subject");
                exchange.getIn().setHeader(SendGridConstants.TO, "to@example.com");
                exchange.getIn().setBody("This is my message text.");
            }
        });
        assertNotNull(exchange.getIn().getHeader(SendGridConstants.MESSAGE_ID));
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .to("sendgrid://from@example.com?apiKey={{env:SENDGRID_API_KEY}}");
            }
        };
    }
}
