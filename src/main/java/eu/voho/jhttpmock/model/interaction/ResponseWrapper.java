package eu.voho.jhttpmock.model.interaction;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class ResponseWrapper {
    private final HttpServletResponse response;

    public ResponseWrapper(final HttpServletResponse response) {
        this.response = response;
    }

    public void setStatus(final int code) {
        this.response.setStatus(code);
    }

    public void write(final char[] body) throws IOException {
        response.getWriter().write(body);
        response.getWriter().flush();
    }

    public void addHeader(final Map<String, Iterable<String>> headers) {
        headers.forEach((name, values) -> {
            values.forEach(value -> response.addHeader(name, value));
        });
    }
}
