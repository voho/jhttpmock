package eu.voho.jhttpmock;

/**
 * Created by vojta on 14/04/2017.
 */
public interface RequestStubbing {
    RequestStubbing withUrl(String url);

    ResponseStubbing thenRespond();
}
