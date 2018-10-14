package com.bigkevmcd.camel.sendgrid;

import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

class SendGridTestSupport extends CamelTestSupport {
    static final String MESSAGE_ID = "kA5kcHA8SVGtVh5S9rAUew";
    SendGrid mockSendGrid;

    @Override
    @Before
    public void setUp() throws Exception {
        mockSendGrid = mock(SendGrid.class);
        super.setUp();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        registry.bind("sendGridClient", mockSendGrid);
        return registry;
    }

    Response createResponse() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Message-Id", SendGridTestSupport.MESSAGE_ID);
        Response response = new Response();
        response.setStatusCode(200);
        response.setHeaders(headers);
        return response;
    }
}
