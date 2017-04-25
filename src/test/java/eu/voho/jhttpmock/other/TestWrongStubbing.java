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
                .thenRespond();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotVerifyWhileDefining() {
        toTest
                .onRequest()
                .wasNeverReceived();
    }

    @Test(expected = IllegalStateException.class)
    public void testWrongProbabilities() {
        toTest
                .onRequest()
                .thenRespond()
                .orRespondWithProbability(0.2)
                .orRespondWithProbability(0.3)
                .orRespondWithProbability(0.5);
    }
}
