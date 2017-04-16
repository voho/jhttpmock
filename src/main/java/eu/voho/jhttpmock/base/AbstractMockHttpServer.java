package eu.voho.jhttpmock.base;

import eu.voho.jhttpmock.MockHttpServer;
import eu.voho.jhttpmock.base.data.MockState;
import eu.voho.jhttpmock.base.data.RequestStubbingData;
import eu.voho.jhttpmock.base.data.RequestWrapper;
import eu.voho.jhttpmock.base.data.ResponseStubbingData;
import eu.voho.jhttpmock.base.data.ResponseWrapper;
import eu.voho.jhttpmock.base.rule.MockBehaviour;
import eu.voho.jhttpmock.base.rule.MockBehaviourRule;
import eu.voho.jhttpmock.base.rule.MockInteractions;
import eu.voho.jhttpmock.base.stub.RequestStubbing;
import eu.voho.jhttpmock.base.stub.ResponseStubbing;
import eu.voho.jhttpmock.base.stub.VerifyStubbing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by vojta on 14/04/2017.
 */
public abstract class AbstractMockHttpServer implements MockHttpServer {
    private final MockState state;

    public AbstractMockHttpServer() {
        this.state = new MockState();
    }

    @Override
    public RequestStubbing onRequest() {
        RequestStubbingData requestStubbingData = new RequestStubbingData();
        ResponseStubbingData responseStubbingData = new ResponseStubbingData();
        MockBehaviourRule rule = new MockBehaviourRule(requestStubbingData, responseStubbingData);
        this.state.getBehaviour().addRule(rule);

        return new RequestStubbing() {
            @Override
            public RequestStubbing withUrl(String url) {
                requestStubbingData.setUrl(url);
                return this;
            }

            @Override
            public ResponseStubbing thenRespond() {
                return new ResponseStubbing() {
                    @Override
                    public ResponseStubbing withCode(int code) {
                        responseStubbingData.setCode(code);
                        return this;
                    }

                    @Override
                    public ResponseStubbing withBody(String body) {
                        responseStubbingData.setBody(body);
                        return this;
                    }
                };
            }
        };
    }

    @Override
    public VerifyStubbing verifyThatRequest() {
        RequestStubbingData requestStubbingData = new RequestStubbingData();

        return new VerifyStubbing() {
            @Override
            public VerifyStubbing withUrl(String url) {
                requestStubbingData.setUrl(url);
                return this;
            }

            @Override
            public void receivedTimes(int times) {
                List<RequestWrapper> requests = state.getInteractions().findByRule(requestStubbingData.asPredicate());
                // ASSERT
                assertEquals(times, requests.size());
            }
        };
    }

    protected boolean handleByMock(HttpServletRequest request, HttpServletResponse response) {
        MockInteractions interactions = state.getInteractions();
        RequestWrapper requestWrapper = new RequestWrapper(request);
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        interactions.addRequest(requestWrapper);
        MockBehaviour behaviour = state.getBehaviour();
        return behaviour.applyRules(requestWrapper, responseWrapper);
    }
}
