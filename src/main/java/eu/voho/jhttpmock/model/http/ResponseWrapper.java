package eu.voho.jhttpmock.model.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class encapsulates all operations that we need to do with a response.
 */
public class ResponseWrapper {
    private final HttpServletResponse response;

    public ResponseWrapper(final HttpServletResponse response) {
        this.response = response;
    }

    public void setStatus(final int code) {
        this.response.setStatus(code);
    }

    public void addHeader(final String name, final Iterable<String> values) {
        values.forEach(value -> response.addHeader(name, value));
    }

    public void addCookies(final Cookie cookie) {
        response.addCookie(cookie);
    }

    public void write(final char[] body) throws IOException {
        final PrintWriter writer = response.getWriter();
        writer.write(body);
        writer.flush();
    }
}
