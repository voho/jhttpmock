package eu.voho.jhttpmock;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicUseCase {
    @Rule
    public MockHttpServerRule mockHttpServerRule = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void test() throws IOException {
        for (int i = 0; i < 5; i++) {
            mockHttpServerRule.reset();

            mockHttpServerRule
                    .onRequest()
                    .withUrlEqualTo("/ping")
                    .thenRespond()
                    .withCode(201)
                    .withGaussianRandomDelay(Duration.ofMillis(50), Duration.ofMillis(30))
                    .withBody("Hello!");

            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                final HttpGet get = new HttpGet("http://localhost:8080/ping");

                try (CloseableHttpResponse response = client.execute(get)) {
                    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
                }
            }

            mockHttpServerRule
                    .verifyThatRequest()
                    .withUrlEqualTo("/ping")
                    .wasReceivedOnce();
        }
    }
}
