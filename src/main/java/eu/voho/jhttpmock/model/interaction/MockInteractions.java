package eu.voho.jhttpmock.model.interaction;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MockInteractions<T> {
    private final List<T> history;

    public MockInteractions() {
        this.history = new LinkedList<>();
    }

    public List<T> findInteraction(final Predicate<T> rule) {
        return history.stream().filter(rule).collect(Collectors.toList());
    }

    public void addInteraction(final T request) {
        history.add(request);
    }

    public void reset() {
        history.clear();
    }
}
