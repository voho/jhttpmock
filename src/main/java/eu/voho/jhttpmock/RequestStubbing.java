package eu.voho.jhttpmock;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.number.OrderingComparison;

public interface RequestStubbing {
    RequestStubbing withMethod(Matcher<String> methodMatcher);

    default RequestStubbing withHeadMethod() {
        return withMethod(CoreMatchers.equalTo("HEAD"));
    }

    default RequestStubbing withPutMethod() {
        return withMethod(CoreMatchers.equalTo("PUT"));
    }

    default RequestStubbing withPostMethod() {
        return withMethod(CoreMatchers.equalTo("POST"));
    }

    default RequestStubbing withDeleteMethod() {
        return withMethod(CoreMatchers.equalTo("DELETE"));
    }

    default RequestStubbing withGetMethod() {
        return withMethod(CoreMatchers.equalTo("GET"));
    }

    RequestStubbing withHeader(Matcher<String> nameMatcher, Matcher<Iterable<String>> valueMatcher);

    default RequestStubbing withHeader(String name, Matcher<Iterable<String>> valueMatcher) {
        return withHeader(CoreMatchers.equalTo(name), valueMatcher);
    }

    default RequestStubbing withSingleHeaderEqualTo(String name, String value) {
        final Matcher<Iterable<String>> valueMatcher = Matchers.<Iterable<String>>allOf(Matchers.iterableWithSize(1), Matchers.contains(value));
        return withHeader(CoreMatchers.equalTo(name), valueMatcher);
    }

    RequestStubbing withQueryParameter(Matcher<String> nameMatcher, Matcher<Iterable<String>> valueMatcher);

    default RequestStubbing withQueryParameter(String name, Matcher<Iterable<String>> valueMatcher) {
        return withQueryParameter(CoreMatchers.equalTo(name), valueMatcher);
    }

    default RequestStubbing withSingleQueryParameterEqualTo(String name, String value) {
        final Matcher<Iterable<String>> valueMatcher = Matchers.<Iterable<String>>allOf(Matchers.iterableWithSize(1), Matchers.contains(value));
        return withQueryParameter(CoreMatchers.equalTo(name), valueMatcher);
    }

    RequestStubbing withBody(Matcher<char[]> bodyMatcher);

    default RequestStubbing withBodyEqualTo(String body) {
        return withBody(CoreMatchers.equalTo(body.toCharArray()));
    }

    default RequestStubbing withBodyEqualTo(char[] body) {
        return withBody(CoreMatchers.equalTo(body));
    }

    RequestStubbing withUrl(Matcher<String> urlMatcher);

    default RequestStubbing withUrlEqualTo(String url) {
        return withUrl(CoreMatchers.equalTo(url));
    }

    ResponseStubbing thenRespond();

    void wasReceivedTimes(Matcher<Integer> timesMatcher);

    default void wasReceivedOnce() {
        wasReceivedTimes(CoreMatchers.equalTo(1));
    }

    default void wasNeverReceived() {
        wasReceivedTimes(CoreMatchers.equalTo(0));
    }

    default void wasReceivedAtMost(int maxNumber) {
        wasReceivedTimes(OrderingComparison.lessThanOrEqualTo(maxNumber));
    }

    default void wasReceivedAtLeast(int minNumber) {
        wasReceivedTimes(OrderingComparison.greaterThanOrEqualTo(minNumber));
    }
}
