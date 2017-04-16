package eu.voho.jhttpmock.junit;

import eu.voho.jhttpmock.MockHttpServer;
import eu.voho.jhttpmock.base.stub.RequestStubbing;
import eu.voho.jhttpmock.base.stub.VerifyStubbing;
import org.junit.rules.ExternalResource;

public class MockHttpServerRule extends ExternalResource implements MockHttpServer {
    private final MockHttpServer mockHttpServer;

    public MockHttpServerRule(MockHttpServer mockHttpServer) {
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
    public void close() throws Exception {
        mockHttpServer.close();
    }

    @Override
    public RequestStubbing onRequest() {
        return mockHttpServer.onRequest();
    }

    @Override
    public VerifyStubbing verifyThatRequest() {
        return mockHttpServer.verifyThatRequest();
    }
}
