package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.interaction.ResponseWrapper;

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

    public boolean apply(RequestWrapper request, ResponseWrapper response) {
        for (MockBehaviourRule rule : rules) {
            if (rule.apply(request, response)) {
                return true;
            }
        }

        return false;
    }

    public void reset() {
        this.rules.clear();
    }
}
