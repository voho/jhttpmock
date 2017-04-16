package eu.voho.jhttpmock.model.stub;

import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by vojta on 14/04/2017.
 */
public class RequestStubbingData {
    private Matcher<String> urlMatcher = CoreMatchers.any(String.class);
    private Matcher<String> methodMatcher = CoreMatchers.any(String.class);
    private Matcher<String> bodyMatcher = CoreMatchers.any(String.class);
    private List<KeyValueMatchers> headerMatchers = new LinkedList<>();
    private List<KeyValueMatchers> queryParameterMatchers = new LinkedList<>();

    public Predicate<RequestWrapper> asPredicate() {
        return (request -> urlMatcher.matches(request.getUrl())
                && methodMatcher.matches(request.getMethod())
//                && bodyMatcher.matches(request.getBody()) // TODO type
                && allMatch(headerMatchers, request.getHeaders())
                && allMatch(queryParameterMatchers, request.getQueryParameters()));
    }

    private boolean allMatch(List<KeyValueMatchers> matchers, Map<String, String[]> values) {
        for (KeyValueMatchers matcher : matchers) {
            for (Map.Entry<String, String[]> entry : values.entrySet()) {
                boolean matchesKey = matcher.keyMatcher.matches(entry.getKey());
                boolean matchesValue = matcher.valueMatcher.matches(Arrays.asList(entry.getValue()));

                if (!matchesKey || !matchesValue) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setUrlMatcher(Matcher<String> urlMatcher) {
        this.urlMatcher = urlMatcher;
    }

    public void setMethodMatcher(Matcher<String> methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public void addHeaderMatcher(Matcher<String> nameMatcher, Matcher<Iterable<String>> valueMatcher) {
        this.headerMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    public void addQueryParameterMatcher(Matcher<String> nameMatcher, Matcher<Iterable<String>> valueMatcher) {
        this.queryParameterMatchers.add(new KeyValueMatchers(nameMatcher, valueMatcher));
    }

    public void setBodyMatcher(Matcher<String> bodyMatcher) {
        this.bodyMatcher = bodyMatcher;
    }

    private static class KeyValueMatchers {
        Matcher<String> keyMatcher;
        Matcher<Iterable<String>> valueMatcher;

        KeyValueMatchers(Matcher<String> keyMatcher, Matcher<Iterable<String>> valueMatcher) {
            this.keyMatcher = keyMatcher;
            this.valueMatcher = valueMatcher;
        }
    }
}
