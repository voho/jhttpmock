package eu.voho.jhttpmock.base.data;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by vojta on 14/04/2017.
 */
public class ResponseStubbingData {
    private int code;
    private String body;

    public void setCode(int code) {
        this.code = code;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Consumer<ResponseWrapper> asConsumer() {
        return (response -> {
            response.setStatus(code);
            try {
                response.write(body);
            } catch (IOException e) {
                // TODO
            }
        });
    }
}
