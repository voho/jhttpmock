package eu.voho.jhttpmock.base;

import eu.voho.jhttpmock.CloseableMockHttpServer;
import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.model.behaviour.BehaviourDefiningRequestStubing;
import eu.voho.jhttpmock.model.behaviour.MockBehaviour;
import eu.voho.jhttpmock.model.interaction.MockInteractions;
import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.interaction.ResponseWrapper;
import eu.voho.jhttpmock.model.interaction.VerificationRequestStubbing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by vojta on 14/04/2017.
 */
public abstract class AbstractCloseableMockHttpServer implements CloseableMockHttpServer {
    private final MockBehaviour behaviour;
    private final MockInteractions interactions;

    public AbstractCloseableMockHttpServer() {
        this.behaviour = new MockBehaviour();
        this.interactions = new MockInteractions();
    }

    @Override
    public RequestStubbing onRequest() {
        return new BehaviourDefiningRequestStubing(behaviour);
    }

    @Override
    public RequestStubbing verifyThatRequest() {
        return new VerificationRequestStubbing(interactions);
    }

    protected boolean handleByMock(final HttpServletRequest request, final HttpServletResponse response) {
        final RequestWrapper requestWrapper = new RequestWrapper(request);
        final ResponseWrapper responseWrapper = new ResponseWrapper(response);
        interactions.addRequest(requestWrapper);
        return behaviour.apply(requestWrapper, responseWrapper);
    }
}
