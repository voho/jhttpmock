package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import eu.voho.jhttpmock.utility.TestUtility;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;

public class RequestMatcherUsingQueryParameterTest {
    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void testQueryParameterMatching() throws IOException {
        mock
                .onRequest()
                .withQueryParameterEqualTo("p1", "v1")
                .thenRespond()
                .withCode(200);

        TestUtility.executeGetAndVerify(
                a -> a.setURI(URI.create("http://localhost:8080/?p1=v1"))
        );

        mock
                .verifyThatRequest()
                .withQueryParameterEqualTo("p1", "v1")
                .wasReceivedOnce();

        mock
                .verifyThatRequest()
                .withQueryParameterEqualTo("WRONG-NAME", "v1")
                .wasNeverReceived();

        mock
                .verifyThatRequest()
                .withQueryParameterEqualTo("p1", "WRONG-VALUE")
                .wasNeverReceived();
    }

    @Test
    public void testQueryParameterMatchingMultipleValues() throws IOException {
        mock
                .onRequest()
                .withQueryParameterEqualTo("p1", "v1")
                .thenRespond()
                .withCode(200);

        TestUtility.executeGetAndVerify(
                a -> a.setURI(URI.create("http://localhost:8080/?p1=v1&p1=v2"))
        );

        mock
                .verifyThatRequest()
                .withQueryParameterEqualTo("p1", new HashSet<>(Arrays.asList("v1", "v2")));

        mock
                .verifyThatRequest()
                .withQueryParameterEqualTo("p1", "v1")
                .wasNeverReceived();

        mock
                .verifyThatRequest()
                .withQueryParameterEqualTo("p1", "v2")
                .wasNeverReceived();
    }
}
