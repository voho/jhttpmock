package eu.voho.jhttpmock;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class PrimitiveHttpClient {
    public static void executeGetAndVerify(final String url) throws IOException {
        executeGetAndVerify(url, r -> {
        });
    }

    public static void executeGetAndVerify(final String url, final Consumer<HttpResponse> responseValidator) throws IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final HttpGet get = new HttpGet(url);
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
}
