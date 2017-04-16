package eu.voho.jhttpmock.junit;

import eu.voho.jhttpmock.CloseableMockHttpServer;
import eu.voho.jhttpmock.MockHttpServer;
import eu.voho.jhttpmock.RequestStubbing;
import org.junit.rules.ExternalResource;

/**
 * Adapter for running mock HTTP server as a <a href="https://github.com/junit-team/junit4/wiki/rules">JUnit Rules</a>.
 * You can use it like this:
 * <pre>
 * class MyTest {
 *     @Rule
 *     public MockHttpServerRule mockHttpServer = new MockHttpServerRule();
 *
 *     @Test
 *     public void test() {
 *         // ...
 *     }
 * }
 * </pre>
 */
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
    public void reset() {
        mockHttpServer.reset();
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
