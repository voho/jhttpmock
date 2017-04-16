package eu.voho.jhttpmock;

public interface ResponseStubbing {
    ResponseStubbing withCode(int code);

    ResponseStubbing withBody(char[] body);

    default ResponseStubbing withBody(String body) {
        return withBody(body.toCharArray());
    }
}
