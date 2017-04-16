package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.behaviour.MockBehaviour;
import eu.voho.jhttpmock.model.behaviour.MockBehaviourRule;
import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import eu.voho.jhttpmock.model.stub.ResponseStubbingData;
import org.hamcrest.Matcher;

/**
 * Created by vojta on 16/04/2017.
 */
public class BehaviourDefiningRequestStubing implements RequestStubbing {
    private RequestStubbingData requestStubbingData = new RequestStubbingData();
    private ResponseStubbingData responseStubbingData = new ResponseStubbingData();
    private MockBehaviour targetMockBehaviour;

    public BehaviourDefiningRequestStubing(MockBehaviour targetMockBehaviour) {
        this.targetMockBehaviour = targetMockBehaviour;
        targetMockBehaviour.addRule(new MockBehaviourRule(requestStubbingData, responseStubbingData));
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
        return new ResponseStubbing() {
            @Override
            public ResponseStubbing withCode(final int code) {
                responseStubbingData.setCode(code);
                return this;
            }

            @Override
            public ResponseStubbing withBody(final char[] body) {
                responseStubbingData.setBody(body);
                return this;
            }
        };
    }

    @Override
    public void wasReceivedTimes(final Matcher<Integer> timesMatcher) {
        throw new UnsupportedOperationException("Cannot verify while in behaviour definition mode.");
    }
}
