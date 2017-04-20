package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.PrimitiveHttpClient;
import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class RequestMatcherUsingQueryParameterTest {
    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void testQueryParameterMatching() throws IOException {
        mock
                .onRequest()
                .withSingleQueryParameterEqualTo("p1", "v1")
                .thenRespond()
                .withCode(200);

        PrimitiveHttpClient.executeGetAndVerify("http://localhost:8080/?p1=v1");

        mock
                .verifyThatRequest()
                .withSingleQueryParameterEqualTo("p1", "v1")
                .wasReceivedOnce();

        mock
                .verifyThatRequest()
                .withSingleQueryParameterEqualTo("WRONG-NAME", "v1")
                .wasNeverReceived();

        mock
                .verifyThatRequest()
                .withSingleQueryParameterEqualTo("p1", "WRONG-VALUE")
                .wasNeverReceived();
    }

    @Test
    public void testQueryParameterMatchingMultipleValues() throws IOException {
        mock
                .onRequest()
                .withSingleQueryParameterEqualTo("p1", "v1")
                .thenRespond()
                .withCode(200);

        PrimitiveHttpClient.executeGetAndVerify("http://localhost:8080/?p1=v1&p1=v2");

        mock
                .verifyThatRequest()
                .withQueryParameter("p1", Matchers.containsInAnyOrder("v1", "v2"));

        mock
                .verifyThatRequest()
                .withSingleQueryParameterEqualTo("p1", "v1")
                .wasNeverReceived();

        mock
                .verifyThatRequest()
                .withSingleQueryParameterEqualTo("p1", "v2")
                .wasNeverReceived();
    }
}
