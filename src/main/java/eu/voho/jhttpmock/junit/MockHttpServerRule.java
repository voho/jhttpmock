package eu.voho.jhttpmock.junit;

import eu.voho.jhttpmock.CloseableMockHttpServer;
import eu.voho.jhttpmock.MockHttpServer;
import eu.voho.jhttpmock.RequestStubbing;
import org.junit.rules.ExternalResource;

public class MockHttpServerRule extends ExternalResource implements MockHttpServer {
    private final CloseableMockHttpServer mockHttpServer;

    public MockHttpServerRule(CloseableMockHttpServer mockHttpServer) {
        this.mockHttpServer = mockHttpServer;
    }

    @Override
    protected void after() {
        try {
            mockHttpServer.close();
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public int getPort() {
        return mockHttpServer.getPort();
    }

    @Override
    public RequestStubbing onRequest() {
        return mockHttpServer.onRequest();
    }

    @Override
    public RequestStubbing verifyThatRequest() {
        return mockHttpServer.verifyThatRequest();
    }
}
