package eu.voho.jhttpmock.model.interaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RequestWrapperTest {
    @Mock
    private HttpServletRequest requestMock;
    @InjectMocks
    private RequestWrapper toTest;

    @Test
    public void givenDummyRequest_whenReadingRequestBody_thenBodyIsRead() throws Exception {
        String body = "Hello world!";

        Mockito.when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(body)));

        assertThat(toTest.getBody()).isEqualTo(body.toCharArray());
    }
}