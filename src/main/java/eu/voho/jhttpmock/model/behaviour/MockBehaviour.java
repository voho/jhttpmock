package eu.voho.jhttpmock.model.behaviour;

import eu.voho.jhttpmock.model.interaction.RequestWrapper;
import eu.voho.jhttpmock.model.interaction.ResponseWrapper;
import eu.voho.jhttpmock.model.stub.RequestStubbingData;
import eu.voho.jhttpmock.model.stub.ResponseStubbingData;

import java.util.Stack;

/**
 * Class that stores the mock behaviour as a list of rules.
 * The rules are evaluated in a reversed order, starting from the most recently added one.
 */
public class MockBehaviour {
    private final Stack<MockBehaviourRule> rules;

    public MockBehaviour() {
        this.rules = new Stack<>();
    }

    public void addRule(final RequestStubbingData requestStubbingData, final ResponseStubbingData responseStubbingData) {
        this.rules.add(new MockBehaviourRule(requestStubbingData, responseStubbingData));
    }

    public boolean applyTo(final RequestWrapper request, final ResponseWrapper response) {
        for (final MockBehaviourRule rule : rules) {
            if (rule.isApplicableTo(request)) {
                rule.applyTo(response);
                return true;
            }
        }

        return false;
    }

    public void reset() {
        this.rules.clear();
    }
}
