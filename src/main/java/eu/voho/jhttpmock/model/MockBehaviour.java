package eu.voho.jhttpmock.model;

import java.util.LinkedList;
import java.util.List;

public class MockBehaviour {
    private final List<MockBehaviourRule> rules;

    public MockBehaviour() {
        this.rules = new LinkedList<>();
    }

    public void addRule(MockBehaviourRule rule) {
        this.rules.add(rule);
    }

    public boolean applyRules(RequestWrapper request, ResponseWrapper response) {
        for (MockBehaviourRule rule : rules) {
            if (rule.apply(request, response)) {
                return true;
            }
        }

        return false;
    }
}
