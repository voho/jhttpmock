package eu.voho.jhttpmock.model.interaction;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class encapsulates all operations that we need to do with a request.
 */
public class RequestWrapper {
    private final HttpServletRequest request;

    public RequestWrapper(final HttpServletRequest request) {
        this.request = request;
    }

    public String getMethod() {
        return request.getMethod();
    }

    public String getUrl() {
        return request.getRequestURI();
    }

    public Map<String, String[]> getHeaders() {
        Map<String, String[]> headers = new LinkedHashMap<>();
        if (request.getHeaderNames() != null) {
            Collections.list(request.getHeaderNames()).forEach(hn -> {
                headers.put(hn, Collections.list(request.getHeaders(hn)).stream().toArray(String[]::new));
            });
        }
        return headers;
    }

    public Map<String, String[]> getQueryParameters() {
        return request.getParameterMap();
    }

    public char[] getBody() {
        try {
            StringBuilder buf = new StringBuilder(512);
            try (BufferedReader br = request.getReader()) {
                int b;
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }
            }
            return buf.toString().toCharArray();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read request body.", e);
        }
    }
}
