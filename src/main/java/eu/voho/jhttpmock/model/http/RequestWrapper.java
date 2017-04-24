package eu.voho.jhttpmock.model.http;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class encapsulates all operations that we need to do with a request.
 */
public class RequestWrapper {
    private final String method;
    private final String url;
    private final Map<String, String[]> headers;
    private final Map<String, String[]> queryParameters;
    private final char[] body;

    public RequestWrapper(final HttpServletRequest request) {
        method = request.getMethod();
        url = request.getRequestURI();
        headers = copyHeaders(request);
        queryParameters = copyQueryParameters(request);
        body = copyBody(request);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String[]> getQueryParameters() {
        return queryParameters;
    }

    private Map<String, String[]> copyHeaders(final HttpServletRequest request) {
        final Map<String, String[]> headers = new LinkedHashMap<>();
        if (request.getHeaderNames() != null) {
            Collections.list(request.getHeaderNames()).forEach(hn -> headers.put(hn, Collections.list(request.getHeaders(hn)).stream().toArray(String[]::new)));
        }
        return headers;
    }

    public Map<String, String[]> getHeaders() {
        return headers;
    }

    public char[] getBody() {
        return body;
    }

    private Map<String, String[]> copyQueryParameters(final HttpServletRequest request) {
        return new HashMap<>(request.getParameterMap());
    }

    private char[] copyBody(final HttpServletRequest request) {
        try {
            final StringBuilder buffer = new StringBuilder(512);
            try (BufferedReader br = request.getReader()) {
                int b;
                while ((b = br.read()) != -1) {
                    buffer.append((char) b);
                }
            }
            return buffer.toString().toCharArray();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read request body.", e);
        }
    }
}
