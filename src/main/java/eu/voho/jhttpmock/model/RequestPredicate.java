package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.model.http.RequestWrapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * HTTP request matcher.
 * By default, it matches all requests.
 */
public class RequestPredicate implements Predicate<RequestWrapper> {
    private Matcher<String> urlMatcher;
    private Matcher<String> methodMatcher;
    private Matcher<char[]> bodyMatcher;
    private final List<KeyValueMatchers> headerMatchers;
    private final List<KeyValueMatchers> queryParameterMatchers;

    RequestPredicate() {
        urlMatcher = CoreMatchers.any(String.class);
        methodMatcher = CoreMatchers.any(String.class);
        bodyMatcher = CoreMatchers.any(char[].class);
        headerMatchers = new LinkedList<>();
        queryParameterMatchers = new LinkedList<>();
    }

    @Override
    public boolean test(final RequestWrapper request) {
        return urlMatcher.matches(request.getUrl())
                && methodMatcher.matches(request.getMethod())
                && bodyMatcher.matches(request.getBody())
                && matchKeyAndAllValues(headerMatchers, request.getHeaders())
                && matchKeyAndAllValues(queryParameterMatchers, request.getQueryParameters());
    }

    private boolean matchKeyAndAllValues(final List<KeyValueMatchers> matchers, final Map<String, String[]> values) {
        for (final KeyValueMatchers matcher : matchers) {
            for (final Map.Entry<String, String[]> entry : values.entrySet()) {
                final boolean matchesKey = matcher.keyMatcher.matches(entry.getKey());
                final boolean matchesValue = matcher.valueMatcher.matches(Arrays.asList(entry.getValue()));

                if (!matchesKey || !matchesValue) {
                    return false;
                }
            }
        }

        return true;
    }

    void setUrlMatcher(final Matcher<String> urlMatcher) {
        this.urlMatcher = urlMatcher;
    }

    void setMethodMatcher(final Matcher<String> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    void addHeaderMatcher(final Matcher<String> nameMatcher, final Matcher<Collection<String>> valueMatcher) {
        this.headerMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    void addQueryParameterMatcher(final Matcher<String> nameMatcher, final Matcher<Collection<String>> valueMatcher) {
        this.queryParameterMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    void setBodyMatcher(final Matcher<char[]> bodyMatcher) {
        this.bodyMatcher = bodyMatcher;
    }

    private static class KeyValueMatchers {
        Matcher<String> keyMatcher;
        Matcher<Collection<String>> valueMatcher;

        KeyValueMatchers(final Matcher<String> keyMatcher, final Matcher<Collection<String>> valueMatcher) {
            this.keyMatcher = keyMatcher;
            this.valueMatcher = valueMatcher;
        }
    }
}
