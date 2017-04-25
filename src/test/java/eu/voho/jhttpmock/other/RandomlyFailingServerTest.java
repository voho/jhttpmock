package eu.voho.jhttpmock.other;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import eu.voho.jhttpmock.utility.TestUtility;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomlyFailingServerTest {
    /**
     * This should be high enough to provide enough confidence about the random values.
     * The higher the number, the more will the final counts correspond to their probabilities.
     * Disclaimer: this is not mathematically sound approach, but I keep it for simplicity.
     */
    private static final int NUM_RETRIES = 1000;

    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test
    public void test() throws Exception {
        mock
                .onRequest()
                .withUrlEqualTo("/ping")
                .thenRespond()
                .withCode(200)
                .withBody("Hello!")
                .orRespondWithProbability(0.3)
                .withCode(500)
                .orRespondWithProbability(0.6)
                .withCode(503);

        final Map<Integer, AtomicLong> counters = new HashMap<>();

        for (int i = 0; i < NUM_RETRIES; i++) {
            TestUtility.executeGetAndVerify(
                    a -> a.setURI(mock.getLocalhostHttpUri("/ping")),
                    response -> {
                        final int code = response.getStatusLine().getStatusCode();
                        counters.putIfAbsent(code, new AtomicLong(0));
                        counters.get(code).incrementAndGet();
                    }
            );
        }

        // in theory (yes, only in THEORY) the counters should be:

        // 1) for response code 500 = 0.3 * NUM_RETRIES
        // 2) for response code 503 = 0.6 * NUM_RETRIES
        // 3) for response code 200 = (1 - 0.3 - 0.6) = 0.1 * NUM_RETRIES

        // here we are happy if the order is somewhat correct

        System.out.println(counters);

        assertThat(counters)
                .hasSize(3)
                .containsKeys(200, 500, 503);

        assertThat(counters.get(200).get()).isLessThan(counters.get(500).get());
        assertThat(counters.get(500).get()).isLessThan(counters.get(503).get());

        // no matter the response, the number of requests should be the same

        mock
                .verifyThatRequest()
                .withUrlEqualTo("/ping")
                .wasReceivedTimes(Integer.valueOf(NUM_RETRIES)::equals);
    }
}
