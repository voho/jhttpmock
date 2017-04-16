package eu.voho.jhttpmock.model.interaction;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import org.hamcrest.Matcher;

import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Created by vojta on 16/04/2017.
 */
public class VerificationRequestStubbing implements RequestStubbing {
    private MockInteractions mockInteractions;
    private RequestStubbingData requestStubbingData = new RequestStubbingData();

    public VerificationRequestStubbing(MockInteractions mockInteractions) {
        this.mockInteractions = mockInteractions;
    }

    @Override
    public RequestStubbing custom(final Matcher<RequestWrapper> requestWrapper) {
        // TODO
        return null;
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
