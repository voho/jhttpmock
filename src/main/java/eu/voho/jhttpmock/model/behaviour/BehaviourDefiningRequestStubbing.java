package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.ResponseStubbing;
import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.interaction.ResponseWrapper;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import eu.voho.jhttpmock.model.stub.ResponseStubbingData;
import org.hamcrest.Matcher;

public class BehaviourDefiningRequestStubbing implements RequestStubbing {
    private final RequestStubbingData requestStubbingData;
    private final ResponseStubbingData responseStubbingData;

    public BehaviourDefiningRequestStubbing(final MockBehaviour<RequestWrapper, ResponseWrapper> targetMockBehaviour) {
        requestStubbingData = new RequestStubbingData();
        responseStubbingData = new ResponseStubbingData();
        targetMockBehaviour.addRule(requestStubbingData, responseStubbingData);
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
        return new BehaviourDefiningResponseStubbing(responseStubbingData);
    }

    @Override
    public void wasReceivedTimes(final Matcher<Integer> timesMatcher) {
        throw new UnsupportedOperationException("Cannot verify while in behaviour definition mode.");
    }
}
