package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.model.http.RequestWrapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * HTTP request matcher.
 * By default, it matches all requests.
 */
class RequestPredicate implements Predicate<RequestWrapper> {
    private Predicate<RequestWrapper> predicate;
    private Predicate<String> urlMatcher;
    private Predicate<String> methodMatcher;
    private Predicate<char[]> bodyMatcher;
    private final List<KeyValueMatchers> headerMatchers;
    private final List<KeyValueMatchers> queryParameterMatchers;

    RequestPredicate() {
        predicate = (request) -> true;
        urlMatcher = (request) -> true;
        methodMatcher = (request) -> true;
        bodyMatcher = (request) -> true;
        headerMatchers = new LinkedList<>();
        queryParameterMatchers = new LinkedList<>();
    }

    @Override
    public boolean test(final RequestWrapper request) {
        return predicate.test(request)
                && urlMatcher.test(request.getUrl())
                && methodMatcher.test(request.getMethod())
                && bodyMatcher.test(request.getBody())
                && matchKeyAndAllValues(headerMatchers, request.getHeaders())
                && matchKeyAndAllValues(queryParameterMatchers, request.getQueryParameters());
    }

    private boolean matchKeyAndAllValues(final List<KeyValueMatchers> matchers, final Map<String, String[]> values) {
        if (matchers.isEmpty()) {
            return true;
        } else {
            for (final KeyValueMatchers matcher : matchers) {
                for (final Map.Entry<String, String[]> entry : values.entrySet()) {
                    final boolean matchesKey = matcher.keyMatcher.test(entry.getKey());

                    if (matchesKey) {
                        final boolean matchesValue = matcher.valueMatcher.test(new HashSet<>(Arrays.asList(entry.getValue())));

                        if (!matchesValue) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    void setUrlMatcher(final Predicate<String> urlMatcher) {
        this.urlMatcher = urlMatcher;
    }

    void setMethodMatcher(final Predicate<String> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    void addHeaderMatcher(final Predicate<String> nameMatcher, final Predicate<Set<String>> valueMatcher) {
        this.headerMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    void addQueryParameterMatcher(final Predicate<String> nameMatcher, final Predicate<Set<String>> valueMatcher) {
        this.queryParameterMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    void setBodyMatcher(final Predicate<char[]> bodyMatcher) {
        this.bodyMatcher = bodyMatcher;
    }

    void setPredicate(Predicate<RequestWrapper> predicate) {
        this.predicate = predicate;
    }

    private static class KeyValueMatchers {
        Predicate<String> keyMatcher;
        Predicate<Set<String>> valueMatcher;

        KeyValueMatchers(final Predicate<String> keyMatcher, final Predicate<Set<String>> valueMatcher) {
            this.keyMatcher = keyMatcher;
            this.valueMatcher = valueMatcher;
        }
    }
}
