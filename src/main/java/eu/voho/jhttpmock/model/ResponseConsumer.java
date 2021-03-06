package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.model.http.ResponseWrapper;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
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
    private final Map<ResponseConsumer, Double> alternatives;

    ResponseConsumer() {
        code = 200;
        body = new char[0];
        headers = new LinkedHashMap<>();
        cookies = new LinkedList<>();
        alternatives = new LinkedHashMap<>();
    }

    @Override
    public void accept(final ResponseWrapper response) {
        if (!applyAlternative(response)) {
            applyDelay();
            applyStatus(response);
            applyHeaders(response);
            applyCookies(response);
            writeBody(response);
        }
    }

    private boolean applyAlternative(final ResponseWrapper response) {
        if (!alternatives.isEmpty()) {
            final double randomProbability = ThreadLocalRandom.current().nextDouble();
            double cumulativeProbability = 0.0;

            for (final Map.Entry<ResponseConsumer, Double> alternativeEntry : alternatives.entrySet()) {
                final ResponseConsumer alternative = alternativeEntry.getKey();
                final Double probability = alternativeEntry.getValue();

                cumulativeProbability += probability;

                if (randomProbability <= cumulativeProbability) {
                    alternative.accept(response);
                    return true;
                }
            }
        }

        return false;
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

    private void writeBody(final ResponseWrapper response) {
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

    void addAlternative(final ResponseConsumer alternative, final double probability) {
        final double currentProbabilityOfAlternatives = getTotalProbabilityOfAlternatives();

        if (currentProbabilityOfAlternatives + probability >= 1.0) {
            throw new IllegalStateException("The sum of all alternative probabilities must be less than 1.");
        }

        alternatives.put(alternative, probability);
    }

    private double getTotalProbabilityOfAlternatives() {
        return alternatives.values().stream().reduce((a, b) -> a + b).orElse(0.0);
    }
}
