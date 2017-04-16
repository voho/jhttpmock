package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import eu.voho.jhttpmock.model.stub.ResponseStubbingData;
import org.hamcrest.Matcher;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class BehaviourDefiningRequestStubbing implements RequestStubbing {
    private RequestStubbingData requestStubbingData = new RequestStubbingData();
    private ResponseStubbingData responseStubbingData = new ResponseStubbingData();

    public BehaviourDefiningRequestStubbing(MockBehaviour targetMockBehaviour) {
        targetMockBehaviour.addRule(new MockBehaviourRule(requestStubbingData, responseStubbingData));
    }

    @Override
    public RequestStubbing withMethod(Matcher<String> methodMatcher) {
        requestStubbingData.setMethodMatcher(methodMatcher);
        return this;
    }

    @Override
    public RequestStubbing withHeader(Matcher<String> nameMatcher, Matcher<Iterable<String>> valueMatcher) {
        requestStubbingData.addHeaderMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withQueryParameter(Matcher<String> nameMatcher, Matcher<Iterable<String>> valueMatcher) {
        requestStubbingData.addQueryParameterMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withBody(Matcher<String> bodyMatcher) {
        requestStubbingData.setBodyMatcher(bodyMatcher);
        return this;
    }

    @Override
    public RequestStubbing withUrl(final Matcher<String> urlMatcher) {
        requestStubbingData.setUrlMatcher(urlMatcher);
        return this;
    }

    @Override
    public ResponseStubbing thenRespond() {
        return new ResponseStubbing() {
            @Override
            public ResponseStubbing withRandomDelay(Duration minDelay, Duration maxDelay) {
                responseStubbingData.setDelayGenerator(() -> {
                    long minMs = minDelay.toMillis();
                    long maxMs = maxDelay.toMillis();
                    if (minMs == maxMs) {
                        return minDelay;
                    } else {
                        long delayMs = ThreadLocalRandom.current().nextLong(minMs, maxMs);
                        System.out.println("delay: " + delayMs);// TODO
                        return Duration.ofMillis(delayMs);
                    }
                });
                return this;
            }

            @Override
            public ResponseStubbing withGaussianRandomDelay(Duration mean, Duration deviation) {
                responseStubbingData.setDelayGenerator(() -> {
                    double gaussian = ThreadLocalRandom.current().nextGaussian();
                    double meanMs = mean.toMillis();
                    double deviationMs = deviation.toMillis();
                    long delayMs = Math.max(0, Math.round(meanMs + gaussian * deviationMs));
                    System.out.println("delay: " + delayMs); // TODO
                    return Duration.ofMillis(delayMs);
                });
                return this;
            }

            @Override
            public ResponseStubbing withHeader(String name, Iterable<String> values) {
                responseStubbingData.addHeader(name, values);
                return this;
            }

            @Override
            public ResponseStubbing withCode(final int code) {
                responseStubbingData.setCode(code);
                return this;
            }

            @Override
            public ResponseStubbing withBody(String body) {
                responseStubbingData.setBody(body.toCharArray());
                return this;
            }
        };
    }

    @Override
    public void wasReceivedTimes(final Matcher<Integer> timesMatcher) {
        throw new UnsupportedOperationException("Cannot verify while in behaviour definition mode.");
    }
}
