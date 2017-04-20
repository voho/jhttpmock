package eu.voho.jhttpmock;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import org.junit.Rule;
import org.junit.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleServerTest {
    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void testSingleRound() throws Exception {
        mock
                .onRequest()
                .withUrlEqualTo("/ping")
                .thenRespond()
                .withCode(201)
                .withPoissonRandomDelay(Duration.ofMillis(50))
                .withBody("Hello!");

        PrimitiveHttpClient.executeGetAndVerify(
                "http://localhost:8080/ping",
                response -> assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201)
        );

        mock
                .verifyThatRequest()
                .withUrlEqualTo("/ping")
                .wasReceivedOnce();
    }

    @Test
    public void testMultipleRounds() throws Exception {
        for (int i = 0; i < 5; i++) {
            testSingleRound();
            mock.reset();
        }
    }
}
