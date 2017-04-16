package eu.voho.jhttpmock.base.stub;

/**
 * Created by vojta on 16/04/2017.
 */
public interface VerifyStubbing {
    VerifyStubbing withUrl(String url);

    void receivedTimes(int times);
}
