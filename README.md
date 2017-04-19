# jHTTPMock

[![Travis](https://travis-ci.org/voho/jhttpmock.svg?branch=master)](https://travis-ci.org/voho/jhttpmock) 
[![codecov.io](https://codecov.io/github/voho/jhttpmock/coverage.svg?branch=master)](https://codecov.io/github/voho/jhttpmock?branch=master)
[![JitPack](https://jitpack.io/v/voho/jhttpmock.svg)](https://jitpack.io/#voho/jhttpmock)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/058408fcfc2442729c87ea2889a33668)](https://www.codacy.com/app/vojtech-hordejcuk/jhttpmock?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=voho/jhttpmock&amp;utm_campaign=Badge_Grade)

Mock HTTP Server for Java built with Jetty and JUnit.

## Quick Example

The example is using JHttpMock as a JUnit rule.

```java
public class BasicUseCase {
    @Rule
    public MockHttpServerRule mockHttpServerRule = new MockHttpServerRule(new JettyMockHttpServer(8081));
    
    @Test
    public void test() {
        // mock behaviour
        
        mockHttpServerRule
            .onRequest()
            .withUrlEqualTo("/ping")
            .thenRespond()
            .withCode(200)
            .withGaussianRandomDelay(Duration.ofMillis(50), Duration.ofMillis(30))
            .withBody("Hello!"); 
                   
        // ...send HTTP GET request...
        
        // verification
        
        mockHttpServerRule
            .verifyThatRequest()
            .withUrlEqualTo("/ping")
            .wasReceivedOnce();
    }
}
```

## Properties you can match the HTTP request on

- method (GET, POST, etc.)
- URL
- header(s)
- query parameter(s)
- body 

## Response properties you can set

- status code
- header(s)
- body
- delay (fixed, random, gaussian random)