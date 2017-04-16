package eu.voho.jhttpmock.model;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by vojta on 16/04/2017.
 */
public class ResponseWrapper {
    private final HttpServletResponse response;

    public ResponseWrapper(HttpServletResponse response) {
        this.response = response;
    }

    public void setStatus(int code) {
        this.response.setStatus(code);
    }

    public void write(String body) throws IOException {
        this.response.getWriter().write(body);
    }
}
