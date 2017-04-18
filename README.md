# jHTTPMock

[![Travis](https://travis-ci.org/voho/jhttpmock.svg?branch=master)](https://travis-ci.org/voho/jhttpmock) 
[![codecov.io](https://codecov.io/github/voho/jhttpmock/coverage.svg?branch=master)](https://codecov.io/github/voho/jhttpmock?branch=master)
[![JitPack](https://jitpack.io/v/voho/jhttpmock.svg)](https://jitpack.io/#voho/jhttpmock)

Mock HTTP Server for Java built with Jetty and JUnit.

## Example

The example is using JHttpock as a JUnit rule.

```java
public class BasicUseCase {
    @Rule
    public MockHttpServerRule mockHttpServerRule = new MockHttpServerRule(new JettyMockHttpServer(8081));
    
    @Test
    public void test() {
        mockHttpServerRule
            .onRequest()
            .withUrlEqualTo("/ping")
            .thenRespond()
            .withCode(200)
            .withGaussianRandomDelay(Duration.ofMillis(50), Duration.ofMillis(30))
            .withBody("Hello!"); 
                   
        // send GET request
        
        mockHttpServerRule
            .verifyThatRequest()
            .withUrlEqualTo("/ping")
            .wasReceivedOnce();
    }
}
```

