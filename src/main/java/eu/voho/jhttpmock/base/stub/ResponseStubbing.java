package eu.voho.jhttpmock.base.stub;

/**
 * Created by vojta on 14/04/2017.
 */
public interface ResponseStubbing {
    ResponseStubbing withCode(int code);

    ResponseStubbing withBody(String body);
}
