package eu.voho.jhttpmock.other;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import eu.voho.jhttpmock.utility.TestUtility;
import org.junit.Rule;
import org.junit.Test;

import java.net.URI;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleServerTest {
    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void testUrls() {
        assertThat(mock.getLocalhostHttpsUri()).isEqualTo(URI.create("https://localhost:8080/"));
        assertThat(mock.getLocalhostHttpUri()).isEqualTo(URI.create("http://localhost:8080/"));
        assertThat(mock.getLocalhostHttpsUri("/whatever")).isEqualTo(URI.create("https://localhost:8080/whatever"));
        assertThat(mock.getLocalhostHttpUri("/whatever")).isEqualTo(URI.create("http://localhost:8080/whatever"));
    }

    @Test
    public void testSingleRound() throws Exception {
        mock
                .onRequest()
                .withUrlEqualTo("/ping")
                .thenRespond()
                .withCode(201)
                .withPoissonRandomDelay(Duration.ofMillis(50))
                .withBody("Hello!");

        TestUtility.executeGetAndVerify(
                a -> a.setURI(mock.getLocalhostHttpUri("/ping")),
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
