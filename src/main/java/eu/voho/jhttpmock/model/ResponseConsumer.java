package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.model.http.ResponseWrapper;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * HTTP response updater.
 * By default, creates a response with the following properties:
 * <ul>
 * <li>status code: 200 OK</li>
 * <li>empty body</li>
 * <li>no headers</li>
 * <li>no cookies</li>
 * <li>no delay</li>
 * </ul>
 */
class ResponseConsumer implements Consumer<ResponseWrapper> {
    private int code;
    private char[] body;
    private final Map<String, Iterable<String>> headers;
    private final List<Cookie> cookies;
    private Supplier<Duration> delayGenerator;

    ResponseConsumer() {
        code = 200;
        body = new char[0];
        headers = new LinkedHashMap<>();
        cookies = new LinkedList<>();
    }

    @Override
    public void accept(final ResponseWrapper response) {
        applyDelay();
        applyStatus(response);
        applyHeaders(response);
        applyCookies(response);
        addBody(response);
    }

    private void applyDelay() {
        if (delayGenerator != null) {
            try {
                Thread.sleep(delayGenerator.get().toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException("Cannot sleep.", e);
            }
        }
    }

    private void applyStatus(final ResponseWrapper response) {
        response.setStatus(code);
    }

    private void applyHeaders(final ResponseWrapper response) {
        headers.forEach(response::addHeader);
    }

    private void applyCookies(final ResponseWrapper response) {
        cookies.forEach(response::addCookies);
    }

    private void addBody(final ResponseWrapper response) {
        try {
            response.write(body);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write response body.", e);
        }
    }

    void setCode(final int code) {
        this.code = code;
    }

    void setBody(final char[] body) {
        this.body = body;
    }

    void addHeader(final String name, final Iterable<String> values) {
        headers.put(name, values);
    }

    void addCookie(final Cookie cookie) {
        cookies.add(cookie);
    }

    void setDelayGenerator(final Supplier<Duration> delayGenerator) {
        this.delayGenerator = delayGenerator;
    }
}
