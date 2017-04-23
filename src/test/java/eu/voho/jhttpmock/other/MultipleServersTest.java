package eu.voho.jhttpmock.other;

import eu.voho.jhttpmock.jetty.JettyMockHttpServer;
import eu.voho.jhttpmock.junit.MockHttpServerRule;
import eu.voho.jhttpmock.utility.TestUtility;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MultipleServersTest {
    @Rule
    public MockHttpServerRule mock1 = new MockHttpServerRule(new JettyMockHttpServer(8090));
    @Rule
    public MockHttpServerRule mock2 = new MockHttpServerRule(new JettyMockHttpServer(8091));

    @Test
    public void testSingleRound() throws Exception {
        mock1
                .onRequest()
                .withGetMethod()
                .thenRespond()
                .withBody("Hello, this is 1.");

        mock2
                .onRequest()
                .withGetMethod()
                .thenRespond()
                .withBody("Hello, this is 2.");

        TestUtility.executeGetAndVerify(
                a -> a.setURI(mock1.getLocalhostHttpUri("/")),
                r -> assertThat(TestUtility.toString(r)).isEqualTo("Hello, this is 1.")
        );

        TestUtility.executeGetAndVerify(
                a -> a.setURI(mock2.getLocalhostHttpUri("/")),
                r -> assertThat(TestUtility.toString(r)).isEqualTo("Hello, this is 2.")
        );

        mock1
                .verifyThatRequest()
                .withGetMethod()
                .wasReceivedOnce();

        mock2
                .verifyThatRequest()
                .withGetMethod()
                .wasReceivedOnce();
    }

    @Test
    public void testMultipleRounds() throws Exception {
        for (int i = 0; i < 5; i++) {
            testSingleRound();
            mock1.reset();
            mock2.reset();
        }
    }
}
