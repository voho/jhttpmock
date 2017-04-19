package eu.voho.jhttpmock.base;

import eu.voho.jhttpmock.CloseableMockHttpServer;
import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.model.behaviour.BehaviourDefiningRequestStubbing;
import eu.voho.jhttpmock.model.behaviour.MockBehaviour;
import eu.voho.jhttpmock.model.interaction.MockInteractions;
import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.interaction.ResponseWrapper;
import eu.voho.jhttpmock.model.interaction.VerificationRequestStubbing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract base class for mock HTTP servers.
 * Stores mock behaviour and interactions.
 */
public abstract class AbstractCloseableMockHttpServer implements CloseableMockHttpServer {
    private final MockBehaviour<RequestWrapper, ResponseWrapper> behaviour;
    private final MockInteractions<RequestWrapper> interactions;

    protected AbstractCloseableMockHttpServer() {
        this.behaviour = new MockBehaviour<>();
        this.interactions = new MockInteractions<>();
    }

    @Override
    public void reset() {
        behaviour.reset();
        interactions.reset();
    }

    @Override
    public RequestStubbing onRequest() {
        return new BehaviourDefiningRequestStubbing(behaviour);
    }

    @Override
    public RequestStubbing verifyThatRequest() {
        return new VerificationRequestStubbing(interactions);
    }

    protected boolean handleByMock(final HttpServletRequest request, final HttpServletResponse response) {
        final RequestWrapper requestWrapper = wrapHttpRequest(request);
        final ResponseWrapper responseWrapper = wrapHttpResponse(response);
        interactions.addInteraction(requestWrapper);
        return behaviour.applyBestMatchingRule(requestWrapper, responseWrapper);
    }

    private RequestWrapper wrapHttpRequest(final HttpServletRequest request) {
        return new RequestWrapper(request);
    }

    private ResponseWrapper wrapHttpResponse(final HttpServletResponse response) {
        return new ResponseWrapper(response);
    }
}
