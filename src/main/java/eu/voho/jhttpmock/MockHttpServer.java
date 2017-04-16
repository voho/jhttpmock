package eu.voho.jhttpmock;

/**
 * Created by vojta on 14/04/2017.
 */
public interface MockHttpServer extends AutoCloseable, StubbingStart {
    int getPort();
}
