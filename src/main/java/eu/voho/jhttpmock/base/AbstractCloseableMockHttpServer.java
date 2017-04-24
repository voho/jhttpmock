package eu.voho.jhttpmock.base;

import eu.voho.jhttpmock.CloseableMockHttpServer;
import eu.voho.jhttpmock.RequestStubbing;
import eu.voho.jhttpmock.model.BehaviourDefiningRequestStubbing;
import eu.voho.jhttpmock.model.MockBehaviour;
import eu.voho.jhttpmock.model.MockInteractions;
import eu.voho.jhttpmock.model.VerificationRequestStubbing;
import eu.voho.jhttpmock.model.http.RequestWrapper;
import eu.voho.jhttpmock.model.http.ResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract base class for mock HTTP servers.
 * Encapsulates mock behaviour and interactions.
 */
public abstract class AbstractCloseableMockHttpServer implements CloseableMockHttpServer {
    private final MockBehaviour<RequestWrapper, ResponseWrapper> behaviour;
    private final MockInteractions<RequestWrapper> interactions;

    protected AbstractCloseableMockHttpServer() {
        behaviour = new MockBehaviour<>();
        interactions = new MockInteractions<>();
    }

    @Override
    public void reset() {
        behaviour.reset();
        interactions.clear();
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
        interactions.add(requestWrapper);
        return behaviour.applyBestMatchingRule(requestWrapper, responseWrapper);
    }

    private RequestWrapper wrapHttpRequest(final HttpServletRequest request) {
        return new RequestWrapper(request);
    }

    private ResponseWrapper wrapHttpResponse(final HttpServletResponse response) {
        return new ResponseWrapper(response);
    }
}
