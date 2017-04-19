package eu.voho.jhttpmock.model.stub;

import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RequestStubbingData {
    private Matcher<String> urlMatcher;
    private Matcher<String> methodMatcher;
    private Matcher<char[]> bodyMatcher;
    private final List<KeyValueMatchers> headerMatchers;
    private final List<KeyValueMatchers> queryParameterMatchers;

    public RequestStubbingData() {
        urlMatcher = CoreMatchers.any(String.class);
        methodMatcher = CoreMatchers.any(String.class);
        bodyMatcher = CoreMatchers.any(char[].class);
        headerMatchers = new LinkedList<>();
        queryParameterMatchers = new LinkedList<>();
    }

    public Predicate<RequestWrapper> asPredicate() {
        return (
                request -> urlMatcher.matches(request.getUrl())
                        && methodMatcher.matches(request.getMethod())
                        && bodyMatcher.matches(request.getBody())
                        && allMatch(headerMatchers, request.getHeaders())
                        && allMatch(queryParameterMatchers, request.getQueryParameters())
        );
    }

    private boolean allMatch(final List<KeyValueMatchers> matchers, final Map<String, String[]> values) {
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

    public void setUrlMatcher(final Matcher<String> urlMatcher) {
        this.urlMatcher = urlMatcher;
    }

    public void setMethodMatcher(final Matcher<String> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public void addHeaderMatcher(final Matcher<String> nameMatcher, final Matcher<Iterable<String>> valueMatcher) {
        this.headerMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    public void addQueryParameterMatcher(final Matcher<String> nameMatcher, final Matcher<Iterable<String>> valueMatcher) {
        this.queryParameterMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    public void setBodyMatcher(final Matcher<char[]> bodyMatcher) {
        this.bodyMatcher = bodyMatcher;
    }

    private static class KeyValueMatchers {
        Matcher<String> keyMatcher;
        Matcher<Iterable<String>> valueMatcher;

        KeyValueMatchers(final Matcher<String> keyMatcher, final Matcher<Iterable<String>> valueMatcher) {
            this.keyMatcher = keyMatcher;
            this.valueMatcher = valueMatcher;
        }
    }
}
