package eu.voho.jhttpmock.model.stub;

import eu.voho.jhttpmock.model.interaction.ResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by vojta on 14/04/2017.
 */
public class ResponseStubbingData {
    private int code;
    private char[] body;
    private Optional<Supplier<Duration>> delayGenerator = Optional.empty();
    // TODO headers

    public void setCode(int code) {
        this.code = code;
    }

    public void setBody(char[] body) {
        this.body = body;
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
            try {
                response.write(body);
            } catch (IOException e) {
                // TODO
            }
        });
    }

    public void addHeader(String name, Iterable<String> values) {
        // TODO
    }

    public void setDelayGenerator(Supplier<Duration> delayGenerator) {
        this.delayGenerator = Optional.of(delayGenerator);
    }
}
