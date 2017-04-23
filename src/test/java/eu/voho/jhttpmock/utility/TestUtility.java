package eu.voho.jhttpmock.utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class TestUtility {
    public static void executeGetAndVerify(final Consumer<HttpGet> getUpdater) throws IOException {
        executeGetAndVerify(getUpdater, r -> {
        });
    }

    public static void executePostAndVerify(final Consumer<HttpPost> postUpdater, final Consumer<HttpResponse> responseValidator) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpPost get = new HttpPost();
            postUpdater.accept(get);
            try (CloseableHttpResponse response = client.execute(get)) {
                responseValidator.accept(response);
            }
        }
    }

    public static void executeGetAndVerify(final Consumer<HttpGet> getUpdater, final Consumer<HttpResponse> responseValidator) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpGet get = new HttpGet();
            getUpdater.accept(get);
            try (CloseableHttpResponse response = client.execute(get)) {
                responseValidator.accept(response);
            }
        }
    }

    public static String toString(final HttpResponse response) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            response.getEntity().writeTo(baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    public static HttpEntity toEntity(final String contents) {
        final BasicHttpEntity result = new BasicHttpEntity();
        result.setContent(new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8)));
        result.setContentType("text/plain");
        return result;
    }
}
