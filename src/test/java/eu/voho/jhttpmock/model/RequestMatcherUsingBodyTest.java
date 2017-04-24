package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import eu.voho.jhttpmock.utility.TestUtility;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestMatcherUsingBodyTest {
    private static final String EITHER_BODY = "one";
    private static final String ANOTHER_BODY = "two";

    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void testRequestBodyMatching() throws IOException {
        mock
                .onRequest()
                .withPostMethod()
                .withBodyEqualTo(EITHER_BODY)
                .thenAlwaysRespond()
                .withCode(200)
                .withPoissonRandomDelay(Duration.ofMillis(50));

        mock
                .onRequest()
                .withPostMethod()
                .withBodyEqualTo(ANOTHER_BODY)
                .thenAlwaysRespond()
                .withCode(200)
                .withPoissonRandomDelay(Duration.ofMillis(50));

        TestUtility.executePostAndVerify(
                post -> {
                    post.setURI(mock.getLocalhostHttpUri());
                    post.setEntity(TestUtility.toEntity(EITHER_BODY));
                },
                response -> assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200)
        );

        TestUtility.executePostAndVerify(
                post -> {
                    post.setURI(mock.getLocalhostHttpUri());
                    post.setEntity(TestUtility.toEntity(ANOTHER_BODY));
                },
                response -> assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200)
        );

        mock
                .verifyThatRequest()
                .withBodyEqualTo(EITHER_BODY)
                .wasReceivedOnce();

        mock
                .verifyThatRequest()
                .withBodyEqualTo(ANOTHER_BODY)
                .wasReceivedOnce();
    }
}
