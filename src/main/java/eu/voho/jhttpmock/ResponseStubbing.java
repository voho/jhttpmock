package eu.voho.jhttpmock;

import java.time.Duration;
import java.util.Arrays;

public interface ResponseStubbing {
    default ResponseStubbing withHeader(String name, String... values) {
        return withHeader(name, Arrays.asList(values));
    }

    ResponseStubbing withHeader(String name, Iterable<String> values);

    ResponseStubbing withCode(int code);

    ResponseStubbing withBody(String body);

    ResponseStubbing withRandomDelay(Duration minDelay, Duration maxDelay);

    ResponseStubbing withGaussianRandomDelay(Duration mean, Duration deviation);

    default ResponseStubbing withDelay(Duration delay) {
        return withRandomDelay(delay, delay);
    }
}
