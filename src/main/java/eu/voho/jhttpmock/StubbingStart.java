package eu.voho.jhttpmock;

/**
 * Stubbing start point.
 */
public interface StubbingStart {
    /**
     * Starts a stub for behaviour definition.
     * @return request stubbing (builder)
     */
    RequestStubbing onRequest();

    /**
     * Starts a stub for behaviour verification.
     * @return request stubbing (builder)
     */
    RequestStubbing verifyThatRequest();
}
