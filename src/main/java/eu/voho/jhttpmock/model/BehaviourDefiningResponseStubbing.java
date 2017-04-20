package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.ResponseStubbing;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class BehaviourDefiningResponseStubbing implements ResponseStubbing {
    private final ResponseConsumer responseConsumer;

    public BehaviourDefiningResponseStubbing(final ResponseConsumer responseConsumer) {
        this.responseConsumer = responseConsumer;
    }

    @Override
    public ResponseStubbing withRandomDelay(final Duration minDelay, final Duration maxDelay) {
        responseConsumer.setDelayGenerator(() -> {
            final long minMs = minDelay.toMillis();
            final long maxMs = maxDelay.toMillis();
            return Duration.ofMillis(nextRandomLong(minMs, maxMs));
        });

        return this;
    }

    @Override
    public ResponseStubbing withPoissonRandomDelay(final Duration lambda) {
        responseConsumer.setDelayGenerator(() -> {
            final long lambdaMs = lambda.toMillis();
            final long delayMs = nextPoissonRandomLong(lambdaMs);
            return Duration.ofMillis(delayMs);
        });

        return this;
    }

    @Override
    public ResponseStubbing withHeader(final String name, final Iterable<String> values) {
        responseConsumer.addHeader(name, values);
        return this;
    }

    @Override
    public ResponseStubbing withCode(final int code) {
        responseConsumer.setCode(code);
        return this;
    }

    @Override
    public ResponseStubbing withBody(final String body) {
        responseConsumer.setBody(body.toCharArray());
        return this;
    }

    private long nextPoissonRandomLong(final double lambda) {
        return Math.round(Math.log(1.0 - ThreadLocalRandom.current().nextDouble()) / (-1 / lambda));
    }

    private long nextRandomLong(final long minMs, final long maxMs) {
        if (minMs == maxMs) {
            return minMs;
        }

        return ThreadLocalRandom.current().nextLong(minMs, maxMs + 1);
    }
}