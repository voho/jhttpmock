package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.http.RequestWrapper;
import eu.voho.jhttpmock.model.http.ResponseWrapper;

import java.util.Set;
import java.util.function.Predicate;

public class BehaviourDefiningRequestStubbing implements RequestStubbing {
    private final RequestPredicate requestPredicate;
    private final ResponseConsumer responseConsumer;

    public BehaviourDefiningRequestStubbing(final MockBehaviour<RequestWrapper, ResponseWrapper> targetMockBehaviour) {
        requestPredicate = new RequestPredicate();
        responseConsumer = new ResponseConsumer();
        targetMockBehaviour.addRule(requestPredicate, responseConsumer);
    }

    @Override
    public RequestStubbing matching(final Predicate<RequestWrapper> predicate) {
        requestPredicate.setCustomPredicate(predicate);
        return this;
    }

    @Override
    public RequestStubbing withMethod(final Predicate<String> methodMatcher) {
        requestPredicate.setMethodMatcher(methodMatcher);
        return this;
    }

    @Override
    public RequestStubbing withHeader(final Predicate<String> nameMatcher, final Predicate<Set<String>> valueMatcher) {
        requestPredicate.addHeaderMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withQueryParameter(final Predicate<String> nameMatcher, final Predicate<Set<String>> valueMatcher) {
        requestPredicate.addQueryParameterMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withBody(final Predicate<char[]> bodyMatcher) {
        requestPredicate.setBodyMatcher(bodyMatcher);
        return this;
    }

    @Override
    public RequestStubbing withUrl(final Predicate<String> urlMatcher) {
        requestPredicate.setUrlMatcher(urlMatcher);
        return this;
    }

    @Override
    public ResponseStubbing thenAlwaysRespond() {
        return new BehaviourDefiningResponseStubbing(responseConsumer);
    }

    @Override
    public void wasReceivedTimes(final Predicate<Integer> timesMatcher) {
        throw new UnsupportedOperationException("Cannot verify while in behaviour definition mode.");
    }
}
