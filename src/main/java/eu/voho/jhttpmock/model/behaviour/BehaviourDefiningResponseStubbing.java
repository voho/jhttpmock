package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.stub.ResponseStubbingData;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class BehaviourDefiningResponseStubbing implements ResponseStubbing {
    private final ResponseStubbingData responseStubbingData;

    public BehaviourDefiningResponseStubbing(final ResponseStubbingData responseStubbingData) {
        this.responseStubbingData = responseStubbingData;
    }

    @Override
    public ResponseStubbing withRandomDelay(final Duration minDelay, final Duration maxDelay) {
        responseStubbingData.setDelayGenerator(() -> {
            final long minMs = minDelay.toMillis();
            final long maxMs = maxDelay.toMillis();
            return Duration.ofMillis(nextRandomLong(minMs, maxMs));
        });

        return this;
    }

    @Override
    public ResponseStubbing withGaussianRandomDelay(final Duration mean, final Duration deviation) {
        responseStubbingData.setDelayGenerator(() -> {
            final long meanMs = mean.toMillis();
            final long deviationMs = deviation.toMillis();
            final long delayMs = nextGaussianRandomLong(meanMs, deviationMs);
            return Duration.ofMillis(delayMs);
        });

        return this;
    }

    @Override
    public ResponseStubbing withHeader(final String name, final Iterable<String> values) {
        responseStubbingData.addHeader(name, values);
        return this;
    }

    @Override
    public ResponseStubbing withCode(final int code) {
        responseStubbingData.setCode(code);
        return this;
    }

    @Override
    public ResponseStubbing withBody(final String body) {
        responseStubbingData.setBody(body.toCharArray());
        return this;
    }

    private long nextGaussianRandomLong(final long meanMs, final long stdevMs) {
        final double gaussian = ThreadLocalRandom.current().nextGaussian();
        return Math.max(0, Math.round((double) meanMs + gaussian * (double) stdevMs));
    }

    private long nextRandomLong(final long minMs, final long maxMs) {
        if (minMs == maxMs) {
            return minMs;
        }

        return ThreadLocalRandom.current().nextLong(minMs, maxMs + 1);
    }
}
