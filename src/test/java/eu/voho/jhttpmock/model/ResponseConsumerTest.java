package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.model.http.ResponseWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResponseConsumerTest {
    @Mock
    private HttpServletResponse responseMock;
    @InjectMocks
    private ResponseWrapper responseWrapper;

    private final ResponseConsumer toTest = new ResponseConsumer();

    @Before
    public void setUp() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintWriter pw = new PrintWriter(baos);
        when(responseMock.getWriter()).thenReturn(pw);
    }

    @Test
    public void testCodeBody() throws Exception {
        toTest.setBody("Hello world!".toCharArray());
        toTest.setCode(200);

        toTest.accept(responseWrapper);

        verify(responseMock).getWriter();
        verify(responseMock).setStatus(eq(200));
    }

    @Test
    public void testHeaderCookie() throws Exception {
        toTest.addHeader("h1", Arrays.asList("hv1", "hv2"));
        toTest.addCookie(new Cookie("c1", "cv1"));

        toTest.accept(responseWrapper);

        verify(responseMock).addHeader(eq("h1"), eq("hv1"));
        verify(responseMock).addHeader(eq("h1"), eq("hv2"));
        verify(responseMock).addCookie(any());
    }

    @Test
    public void testDelay() throws Exception {
        toTest.setDelayGenerator(() -> Duration.ofMillis(500));

        final long start = System.currentTimeMillis();
        toTest.accept(responseWrapper);
        final long end = System.currentTimeMillis();

        assertThat(end - start).isGreaterThanOrEqualTo(500);
    }
}
