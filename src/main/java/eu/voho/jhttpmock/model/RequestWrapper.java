package eu.voho.jhttpmock.model;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by vojta on 16/04/2017.
 */
public class RequestWrapper {
    private final String method;
    private final String url;
    private final Map<String, String[]> headers;
    private final Map<String, String[]> queryParameters;
    private final ByteArrayOutputStream body;

    public RequestWrapper(HttpServletRequest request) {
        this.method = request.getMethod();
        this.url = request.getRequestURI();
        this.headers = new LinkedHashMap<>();
        Collections.list(request.getHeaderNames()).forEach(hn -> {
            headers.put(hn, Collections.list(request.getHeaders(hn)).stream().toArray(String[]::new));
        });
        this.queryParameters = new TreeMap<>(request.getParameterMap());
        this.body = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n;
        try {
            while ((n = request.getInputStream().read(buf)) > 0) {
                body.write(buf, 0, n);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot read request input stream.", e);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String[]> getHeaders() {
        return headers;
    }

    public Map<String, String[]> getQueryParameters() {
        return queryParameters;
    }

    public byte[] getBody() {
        return body.toByteArray();
    }
}
