package eu.voho.jhttpmock;

import java.time.Duration;
import java.util.Arrays;

/**
 * Stubbing that allows you to setup the response.
 */
public interface ResponseStubbing {
    default ResponseStubbing withHeader(final String name, final String... values) {
        return withHeader(name, Arrays.asList(values));
    }

    ResponseStubbing withHeader(String name, Iterable<String> values);

    ResponseStubbing withCode(int code);

    ResponseStubbing withBody(String body);

    default ResponseStubbing withFixedDelay(final Duration delay) {
        return withRandomDelay(delay, delay);
    }

    ResponseStubbing withRandomDelay(Duration minDelay, Duration maxDelay);

    ResponseStubbing withPoissonRandomDelay(Duration lambda);
}
