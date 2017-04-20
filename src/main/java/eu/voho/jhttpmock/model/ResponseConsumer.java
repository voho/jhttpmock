package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.model.http.ResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * HTTP response updater.
 * By default, creates a response with the following properties:
 * <ul>
 * <li>status code: 200 OK</li>
 * <li>empty body</li>
 * <li>no headers</li>
 * <li>no delay</li>
 * </ul>
 */
class ResponseConsumer implements Consumer<ResponseWrapper> {
    private int code;
    private char[] body;
    private final Map<String, Iterable<String>> headers;
    private Optional<Supplier<Duration>> delayGenerator;

    ResponseConsumer() {
        this.code = 200;
        this.body = new char[0];
        this.headers = new LinkedHashMap<>();
        this.delayGenerator = Optional.empty();
    }

    @Override
    public void accept(final ResponseWrapper response) {
        delayGenerator.ifPresent(delaySupplier -> {
            try {
                Thread.sleep(delaySupplier.get().toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException("Cannot sleep.", e);
            }
        });

        response.setStatus(code);
        response.addHeader(headers);

        try {
            response.write(body);
        } catch (IOException e) {
            // TODO
        }
    }

    void setCode(final int code) {
        this.code = code;
    }

    void setBody(final char[] body) {
        this.body = body;
    }

    void addHeader(final String name, final Iterable<String> values) {
        this.headers.put(name, values);
    }

    void setDelayGenerator(final Supplier<Duration> delayGenerator) {
        this.delayGenerator = Optional.of(delayGenerator);
    }
}
