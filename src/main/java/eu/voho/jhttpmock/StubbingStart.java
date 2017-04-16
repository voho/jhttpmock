package eu.voho.jhttpmock;

/**
 * Created by vojta on 16/04/2017.
 */
public interface StubbingStart {
    RequestStubbing onRequest();

    RequestStubbing verifyThatRequest();
}
