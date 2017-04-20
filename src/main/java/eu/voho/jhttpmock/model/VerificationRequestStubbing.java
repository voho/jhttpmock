package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.http.RequestWrapper;
import org.hamcrest.Matcher;

import java.util.Collection;
import java.util.function.Predicate;

import static org.junit.Assert.assertThat;

public class VerificationRequestStubbing implements RequestStubbing {
    private final MockInteractions<RequestWrapper> mockInteractions;
    private final RequestPredicate requestPredicate;

    public VerificationRequestStubbing(final MockInteractions<RequestWrapper> mockInteractions) {
        this.mockInteractions = mockInteractions;
        this.requestPredicate = new RequestPredicate();
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
    public RequestStubbing withHeader(final Matcher<String> nameMatcher, final Matcher<Iterable<? extends String>> valueMatcher) {
        requestPredicate.addHeaderMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withQueryParameter(final Matcher<String> nameMatcher, final Matcher<Iterable<? extends String>> valueMatcher) {
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
        throw new UnsupportedOperationException("Cannot define a response in the verification mode.");
    }

    @Override
    public void wasReceivedTimes(final Matcher<Integer> timesMatcher) {
        assertThat(
                "The number of matching mock HTTP server invocations is different.",
                mockInteractions.find(requestPredicate).size(),
                timesMatcher
        );
    }
}
