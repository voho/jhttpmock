package eu.voho.jhttpmock;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicUseCase {
    @Rule
    public MockHttpServerRule mockHttpServerRule = new MockHttpServerRule(new JettyMockHttpServer(8081));

    @Test
    public void test() throws IOException {
        mockHttpServerRule
                .onRequest()
                .withUrl("/ping")
                .thenRespond()
                .withCode(201)
                .withBody("Hello!");

        // TODO fire

        HttpGet get = new HttpGet("http://localhost:8081/ping");
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(get);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);

        mockHttpServerRule
                .verifyThatRequest()
                .withUrl("/ping")
                .receivedTimes(1);
    }
}
