package eu.voho.jhttpmock.base.rule;

import eu.voho.jhttpmock.base.data.RequestWrapper;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by vojta on 14/04/2017.
 */
public class MockInteractions {
    private final List<RequestWrapper> history;

    public MockInteractions() {
        this.history = new LinkedList<>();
    }

    public List<RequestWrapper> findByRule(Predicate<RequestWrapper> rule) {
        return history.stream().filter(rule).collect(Collectors.toList());
    }

    public void addRequest(RequestWrapper request) {
        this.history.add(request);
    }
}
