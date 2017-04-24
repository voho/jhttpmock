package eu.voho.jhttpmock.other;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import org.junit.Rule;
import org.junit.Test;

public class TestWrongStubbing {
    @Rule
    public MockHttpServerRule toTest = new MockHttpServerRule(new JettyMockHttpServer(8080));

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotDefineWhileValidating() {
        toTest
                .verifyThatRequest()
                .thenAlwaysRespond();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotVerifyWhileDefining() {
        toTest
                .onRequest()
                .wasNeverReceived();
    }
}
