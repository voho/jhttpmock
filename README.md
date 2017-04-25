# jHTTPMock

[![Travis](https://travis-ci.org/voho/jhttpmock.svg?branch=master)](https://travis-ci.org/voho/jhttpmock) 
[![codecov.io](https://codecov.io/github/voho/jhttpmock/coverage.svg?branch=master)](https://codecov.io/github/voho/jhttpmock?branch=master)
[![JitPack](https://jitpack.io/v/voho/jhttpmock.svg)](https://jitpack.io/#voho/jhttpmock)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/058408fcfc2442729c87ea2889a33668)](https://www.codacy.com/app/vojtech-hordejcuk/jhttpmock?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=voho/jhttpmock&amp;utm_campaign=Badge_Grade)

Very simple mock HTTP Server for Java built with Jetty and JUnit. Inspired by [WireMock](https://github.com/tomakehurst/wiremock) and [Jadler](https://github.com/jadler-mocking/jadler).

Key features:

* fast onboarding
* fluent API
* latency and failure simulation
* minimal dependencies, small footprint
* supports multiple concurrent instances

## Quick Example

The example is using JHttpMock as a JUnit rule.

```java
public class BasicUseCase {
    @Rule
    public MockHttpServerRule mock = new MockHttpServerRule(new JettyMockHttpServer(8081));
    
    @Test
    public void test() {
        // define mock request behaviour
        
        mock
            .onRequest()
            .withUrlEqualTo("/ping")
            .thenRespond()
            .withCode(200)
            .withRandomDelay(Duration.ofMillis(30), Duration.ofMillis(50))
            .withBody("Hello!")
            .orRespondWithProbability(0.01)
            .withCode(503)
            .withFixedDelay(Duration.ofSeconds(5));
           
        // TODO: send HTTP request
        
        // verify mock server interactions
        
        mock
            .verifyThatRequest()
            .withUrlEqualTo("/ping")
            .wasReceivedOnce();
    }
}
```

## Usage

Add this to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.voho</groupId>
    <artifactId>jhttpmock</artifactId>
    <version>0.9-RC1</version>
</dependency>
```

## Release Notes

### 1.0 (not yet released)

- added alternatives (e.g. simulating errors with a certain probability)
- updated readme

### [0.9-RC1](https://jitpack.io/#voho/jhttpmock/0.9-RC1)

- initial release
