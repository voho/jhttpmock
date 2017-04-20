package eu.voho.jhttpmock;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import org.apache.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class RequestMatcherTest {
    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void testQueryParameterMatching() throws IOException {
        mock
                .onRequest()
                .withSingleQueryParameterEqualTo("p1", "v1")
                .thenRespond()
                .withCode(200);

        PrimitiveHttpClient.executeGetAndVerify(
                "http://localhost:8080/?p1=v1",
                new Consumer<HttpResponse>() {
                    @Override
                    public void accept(HttpResponse response) {

                    }
                }
        );

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

        PrimitiveHttpClient.executeGetAndVerify(
                "http://localhost:8080/?p1=v1&p1=v2",
                new Consumer<HttpResponse>() {
                    @Override
                    public void accept(HttpResponse response) {

                    }
                }
        );

        mock
                .verifyThatRequest()
                .withQueryParameter("p1", new TypeSafeMatcher<Collection<String>>() {
                    @Override
                    protected boolean matchesSafely(Collection<String> strings) {
                        return strings.containsAll(Arrays.asList("v1", "V2"));
                    }

                    @Override
                    public void describeTo(Description description) {
                        // TODO
                    }
                });

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
