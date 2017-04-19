package eu.voho.jhttpmock.model.interaction;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import org.hamcrest.Matcher;

import java.util.List;

import static org.junit.Assert.assertThat;

public class VerificationRequestStubbing implements RequestStubbing {
    private final MockInteractions<RequestWrapper> mockInteractions;
    private final RequestStubbingData requestStubbingData;

    public VerificationRequestStubbing(final MockInteractions<RequestWrapper> mockInteractions) {
        this.mockInteractions = mockInteractions;
        this.requestStubbingData = new RequestStubbingData();
    }

    @Override
    public RequestStubbing withMethod(final Matcher<String> methodMatcher) {
        requestStubbingData.setMethodMatcher(methodMatcher);
        return this;
    }

    @Override
    public RequestStubbing withHeader(final Matcher<String> nameMatcher, final Matcher<Iterable<String>> valueMatcher) {
        requestStubbingData.addHeaderMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withQueryParameter(final Matcher<String> nameMatcher, final Matcher<Iterable<String>> valueMatcher) {
        requestStubbingData.addQueryParameterMatcher(nameMatcher, valueMatcher);
        return this;
    }

    @Override
    public RequestStubbing withBody(final Matcher<char[]> bodyMatcher) {
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
        throw new UnsupportedOperationException("Cannot define a response in the verification mode.");
    }

    @Override
    public void wasReceivedTimes(final Matcher<Integer> timesMatcher) {
        final List<RequestWrapper> requests = mockInteractions.findByRule(requestStubbingData);
        assertThat(requests.size(), timesMatcher);
    }
}
