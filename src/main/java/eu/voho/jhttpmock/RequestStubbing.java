package eu.voho.jhttpmock;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.number.OrderingComparison;

import java.util.Collection;

/**
 * Stubbing that allows you to setup the request matcher.
 */
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

    RequestStubbing withHeader(Matcher<String> nameMatcher, Matcher<Collection<String>> valueMatcher);

    default RequestStubbing withHeader(final String name, final Matcher<Collection<String>> valueMatcher) {
        return withHeader(CoreMatchers.equalTo(name), valueMatcher);
    }

    default RequestStubbing withSingleHeaderEqualTo(final String name, final String value) {
        final Matcher<Collection<String>> valueMatcher = Matchers.<Collection<String>>allOf(
                Matchers.iterableWithSize(1),
                Matchers.contains(value)
        );
        return withHeader(CoreMatchers.equalTo(name), valueMatcher);
    }

    RequestStubbing withQueryParameter(Matcher<String> nameMatcher, Matcher<Collection<String>> valueMatcher);

    default RequestStubbing withQueryParameter(final String name, final Matcher<Collection<String>> valuesMatcher) {
        return withQueryParameter(CoreMatchers.equalTo(name), valuesMatcher);
    }

    default RequestStubbing withSingleQueryParameterEqualTo(final String name, final String value) {
        return withSingleQueryParameter(name, CoreMatchers.equalTo(value));
    }

    default RequestStubbing withSingleQueryParameter(final String name, final Matcher<String> valueMatcher) {
        return withSingleQueryParameter(CoreMatchers.equalTo(name), valueMatcher);
    }

    default RequestStubbing withSingleQueryParameter(final Matcher<String> nameMatcher, final Matcher<String> valueMatcher) {
        final Matcher<Collection<String>> valuesMatcher = Matchers.<Collection<String>>allOf(
                Matchers.iterableWithSize(1),
                Matchers.contains(valueMatcher)
        );
        return withQueryParameter(nameMatcher, valuesMatcher);
    }

    RequestStubbing withBody(Matcher<char[]> bodyMatcher);

    default RequestStubbing withBodyEqualTo(final String body) {
        return withBody(CoreMatchers.equalTo(body.toCharArray()));
    }

    default RequestStubbing withBodyEqualTo(final char[] body) {
        return withBody(CoreMatchers.equalTo(body));
    }

    RequestStubbing withUrl(Matcher<String> urlMatcher);

    default RequestStubbing withUrlEqualTo(final String url) {
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

    default void wasReceivedAtMost(final int maxNumber) {
        wasReceivedTimes(OrderingComparison.lessThanOrEqualTo(maxNumber));
    }

    default void wasReceivedAtLeast(final int minNumber) {
        wasReceivedTimes(OrderingComparison.greaterThanOrEqualTo(minNumber));
    }
}
