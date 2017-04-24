package eu.voho.jhttpmock.model;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class that stores the mock server interactions.
 * @param <T> interaction type
 */
public class MockInteractions<T> {
    private final List<T> history;

    public MockInteractions() {
        history = new LinkedList<>();
    }

    public List<T> find(final Predicate<T> rule) {
        return history.stream().filter(rule).collect(Collectors.toList());
    }

    public void add(final T request) {
        history.add(request);
    }

    public void clear() {
        history.clear();
    }
}
