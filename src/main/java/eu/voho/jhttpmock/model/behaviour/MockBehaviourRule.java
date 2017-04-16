package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import eu.voho.jhttpmock.model.stub.ResponseStubbingData;
import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.interaction.ResponseWrapper;

/**
 * Created by vojta on 16/04/2017.
 */
public class MockBehaviourRule {
    private final RequestStubbingData requestStubbingData;
    private final ResponseStubbingData responseStubbingData;

    public MockBehaviourRule(RequestStubbingData requestStubbingData, ResponseStubbingData responseStubbingData) {
        this.requestStubbingData = requestStubbingData;
        this.responseStubbingData = responseStubbingData;
    }

    public boolean apply(RequestWrapper request, ResponseWrapper response) {
        if (requestStubbingData.asPredicate().test(request)) {
            responseStubbingData.asConsumer().accept(response);
            return true;
        }

        return false;
    }
}
