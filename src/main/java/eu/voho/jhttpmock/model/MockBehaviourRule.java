package eu.voho.jhttpmock.model;

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