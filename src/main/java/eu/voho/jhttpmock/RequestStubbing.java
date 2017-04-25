package eu.voho.jhttpmock;

import eu.voho.jhttpmock.model.http.RequestWrapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Stubbing that allows you to setup the request matcher.
 */
public interface RequestStubbing {
    RequestStubbing matching(Predicate<RequestWrapper> predicate);

    RequestStubbing withMethod(Predicate<String> methodMatcher);

    default RequestStubbing withHeadMethod() {
        return withMethod("HEAD"::equalsIgnoreCase);
    }

    default RequestStubbing withPutMethod() {
        return withMethod("PUT"::equalsIgnoreCase);
    }

    default RequestStubbing withPostMethod() {
        return withMethod("POST"::equalsIgnoreCase);
    }

    default RequestStubbing withDeleteMethod() {
        return withMethod("DELETE"::equalsIgnoreCase);
    }

    default RequestStubbing withGetMethod() {
        return withMethod("GET"::equalsIgnoreCase);
    }

    RequestStubbing withHeader(Predicate<String> nameMatcher, Predicate<Set<String>> valueMatcher);

    default RequestStubbing withHeaderEqualTo(final String name, final Set<String> values) {
        return withHeader(name::equalsIgnoreCase, values::equals);
    }

    default RequestStubbing withHeaderEqualTo(final String name, final String value) {
        return withHeader(name::equalsIgnoreCase, Collections.singleton(value)::equals);
    }

    RequestStubbing withQueryParameter(Predicate<String> nameMatcher, Predicate<Set<String>> valueMatcher);

    default RequestStubbing withQueryParameterEqualTo(final String name, final Set<String> values) {
        return withQueryParameter(name::equalsIgnoreCase, values::equals);
    }

    default RequestStubbing withQueryParameterEqualTo(final String name, final String value) {
        return withQueryParameter(name::equalsIgnoreCase, Collections.singleton(value)::equals);
    }

    RequestStubbing withBody(Predicate<char[]> bodyMatcher);

    default RequestStubbing withBodyEqualTo(final String body) {
        return withBodyEqualTo(body.toCharArray());
    }

    default RequestStubbing withBodyEqualTo(final char[] body) {
        return withBody(value -> Arrays.equals(body, value));
    }

    RequestStubbing withUrl(Predicate<String> urlMatcher);

    default RequestStubbing withUrlEqualTo(final String url) {
        return withUrl(url::equalsIgnoreCase);
    }

    ResponseStubbing thenRespond();

    void wasReceivedTimes(Predicate<Integer> timesMatcher);

    default void wasReceivedOnce() {
        wasReceivedTimes(Integer.valueOf(1)::equals);
    }

    default void wasNeverReceived() {
        wasReceivedTimes(Integer.valueOf(0)::equals);
    }

    default void wasReceivedAtMost(final int maxNumber) {
        wasReceivedTimes(times -> times <= maxNumber);
    }

    default void wasReceivedAtLeast(final int minNumber) {
        wasReceivedTimes(times -> times >= minNumber);
    }
}
