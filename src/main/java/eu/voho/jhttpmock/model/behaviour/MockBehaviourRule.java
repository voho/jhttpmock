package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.interaction.ResponseWrapper;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import eu.voho.jhttpmock.model.stub.ResponseStubbingData;

class MockBehaviourRule {
    private final RequestStubbingData requestStubbingData;
    private final ResponseStubbingData responseStubbingData;

    MockBehaviourRule(final RequestStubbingData requestStubbingData, final ResponseStubbingData responseStubbingData) {
        this.requestStubbingData = requestStubbingData;
        this.responseStubbingData = responseStubbingData;
    }

    boolean isApplicableTo(final RequestWrapper request) {
        return requestStubbingData.asPredicate().test(request);
    }

    void applyTo(final ResponseWrapper response) {
        responseStubbingData.asConsumer().accept(response);
    }
}
