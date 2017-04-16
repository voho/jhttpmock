package eu.voho.jhttpmock.model.interaction;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import org.hamcrest.Matcher;

import java.util.List;

import static org.junit.Assert.assertThat;

public class VerificationRequestStubbing implements RequestStubbing {
    private MockInteractions mockInteractions;
    private RequestStubbingData requestStubbingData = new RequestStubbingData();

    public VerificationRequestStubbing(MockInteractions mockInteractions) {
        this.mockInteractions = mockInteractions;
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
        throw new UnsupportedOperationException("Cannot respond while in verification mode.");
    }

    @Override
    public void wasReceivedTimes(final Matcher<Integer> timesMatcher) {
        final List<RequestWrapper> requests = mockInteractions.findByRule(requestStubbingData.asPredicate());
        assertThat(requests.size(), timesMatcher);
    }
}
