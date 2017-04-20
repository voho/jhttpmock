package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.http.RequestWrapper;
import eu.voho.jhttpmock.model.http.ResponseWrapper;
import org.hamcrest.Matcher;

import java.util.Collection;
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
        requestPredicate.setPredicate(predicate);
        return this;
    }

    @Override
    public RequestStubbing withMethod(final Matcher<String> methodMatcher) {
        requestPredicate.setMethodMatcher(methodMatcher);
        return this;
    }

    @Override
    public RequestStubbing withHeader(final Matcher<String> nameMatcher, final Matcher<Collection<String>> valueMatcher) {
        requestPredicate.addHeaderMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withQueryParameter(final Matcher<String> nameMatcher, final Matcher<Collection<String>> valueMatcher) {
        requestPredicate.addQueryParameterMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withBody(final Matcher<char[]> bodyMatcher) {
        requestPredicate.setBodyMatcher(bodyMatcher);
        return this;
    }

    @Override
    public RequestStubbing withUrl(final Matcher<String> urlMatcher) {
        requestPredicate.setUrlMatcher(urlMatcher);
        return this;
    }

    @Override
    public ResponseStubbing thenRespond() {
        return new BehaviourDefiningResponseStubbing(responseConsumer);
    }

    @Override
    public void wasReceivedTimes(final Matcher<Integer> timesMatcher) {
        throw new UnsupportedOperationException("Cannot verify while in behaviour definition mode.");
    }
}
