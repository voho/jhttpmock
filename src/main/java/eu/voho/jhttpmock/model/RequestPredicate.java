package eu.voho.jhttpmock.model;

import eu.voho.jhttpmock.model.http.RequestWrapper;
import eu.voho.jhttpmock.model.misc.MultiValueStringMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * HTTP request matcher.
 * By default, it matches all requests.
 * Only one matcher is enabled for: URL, HTTP method, request body, and only one custom predicate.
 */
class RequestPredicate implements Predicate<RequestWrapper> {
    private Predicate<String> urlMatcher;
    private Predicate<String> methodMatcher;
    private Predicate<char[]> bodyMatcher;
    private Predicate<RequestWrapper> customPredicate;
    private final List<Predicate<MultiValueStringMap>> headerMatchers;
    private final List<Predicate<MultiValueStringMap>> queryParameterMatchers;

    RequestPredicate() {
        headerMatchers = new LinkedList<>();
        queryParameterMatchers = new LinkedList<>();
    }

    @Override
    public boolean test(final RequestWrapper request) {
        return nullOrTest(urlMatcher, request.getUrl())
                && nullOrTest(methodMatcher, request.getMethod())
                && nullOrTest(bodyMatcher, request.getBody())
                && nullOrTest(customPredicate, request)
                && nullOrTest(headerMatchers, new MultiValueStringMap(request.getHeaders()))
                && nullOrTest(queryParameterMatchers, new MultiValueStringMap(request.getQueryParameters()));
    }

    void setUrlMatcher(final Predicate<String> urlMatcher) {
        checkNull(this.urlMatcher, "Only one matcher for URL is allowed.");
        this.urlMatcher = urlMatcher;
    }

    void setMethodMatcher(final Predicate<String> methodMatcher) {
        checkNull(this.methodMatcher, "Only one matcher for HTTP method is allowed.");
        this.methodMatcher = methodMatcher;
    }

    void setBodyMatcher(final Predicate<char[]> bodyMatcher) {
        checkNull(this.bodyMatcher, "Only one matcher for HTTP request body is allowed.");
        this.bodyMatcher = bodyMatcher;
    }

    void setCustomPredicate(final Predicate<RequestWrapper> customPredicate) {
        checkNull(this.methodMatcher, "Only one custom predicate is allowed.");
        this.customPredicate = customPredicate;
    }

    void addHeaderMatcher(final Predicate<String> nameMatcher, final Predicate<Set<String>> valueMatcher) {
        this.headerMatchers.add(map -> map.contains(nameMatcher, valueMatcher));
    }

    void addQueryParameterMatcher(final Predicate<String> nameMatcher, final Predicate<Set<String>> valueMatcher) {
        this.queryParameterMatchers.add(map -> map.contains(nameMatcher, valueMatcher));
    }

    private static void checkNull(final Predicate<?> matcher, final String messageIfNull) {
        if (matcher != null) {
            throw new UnsupportedOperationException(messageIfNull);
        }
    }

    private static <T> boolean nullOrTest(final List<Predicate<T>> predicates, final T value) {
        return predicates == null || predicates.isEmpty() || predicates.stream().allMatch(predicate -> predicate.test(value));
    }

    private static <T> boolean nullOrTest(final Predicate<T> predicate, final T value) {
        return predicate == null || predicate.test(value);
    }
}
