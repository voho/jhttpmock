package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.http.RequestWrapper;

import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

public class VerificationRequestStubbing implements RequestStubbing {
    private final MockInteractions<RequestWrapper> mockInteractions;
    private final RequestPredicate requestPredicate;

    public VerificationRequestStubbing(final MockInteractions<RequestWrapper> mockInteractions) {
        this.mockInteractions = mockInteractions;
        requestPredicate = new RequestPredicate();
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
    public ResponseStubbing thenRespond() {
        throw new UnsupportedOperationException("Cannot define a response in the verification mode.");
    }

    @Override
    public void wasReceivedTimes(final Predicate<Integer> timesMatcher) {
        assertTrue(
                "The number of matching mock HTTP server invocations is different.",
                timesMatcher.test(mockInteractions.find(requestPredicate).size())
        );
    }
}
