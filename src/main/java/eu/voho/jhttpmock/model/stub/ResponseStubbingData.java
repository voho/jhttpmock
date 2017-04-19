package eu.voho.jhttpmock.model.stub;

import eu.voho.jhttpmock.model.interaction.ResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ResponseStubbingData {
    private int code;
    private char[] body;
    private final Map<String, Iterable<String>> headers = new LinkedHashMap<>();
    private Optional<Supplier<Duration>> delayGenerator = Optional.empty();

    public void setCode(final int code) {
        this.code = code;
    }

    public void setBody(final char[] body) {
        this.body = body;
    }

    public void addHeader(final String name, final Iterable<String> values) {
        this.headers.put(name, values);
    }

    public void setDelayGenerator(final Supplier<Duration> delayGenerator) {
        this.delayGenerator = Optional.of(delayGenerator);
    }

    public Consumer<ResponseWrapper> asConsumer() {
        return (response -> {
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
        });
    }
}
