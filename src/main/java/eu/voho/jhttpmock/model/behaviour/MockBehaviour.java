package eu.voho.jhttpmock.model.behaviour;

import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Class that stores the mock behaviour as a list of rules.
 * The rules are evaluated in a reversed order, starting from the most recently added one.
 */
public class MockBehaviour<P, A> {
    private final Stack<MockBehaviourRule> rules;

    public MockBehaviour() {
        this.rules = new Stack<>();
    }

    public void addRule(final Predicate<P> requestStubbingData, final Consumer<A> responseStubbingData) {
        this.rules.add(new MockBehaviourRule(requestStubbingData, responseStubbingData));
    }

    public boolean applyBestMatchingRule(final P request, final A response) {
        for (final MockBehaviourRule rule : rules) {
            if (rule.isApplicableOn(request)) {
                rule.applyOn(response);
                return true;
            }
        }

        return false;
    }

    public void reset() {
        this.rules.clear();
    }

    private final class MockBehaviourRule {
        private final Predicate<P> precondition;
        private final Consumer<A> action;

        private MockBehaviourRule(final Predicate<P> precondition, final Consumer<A> action) {
            this.precondition = precondition;
            this.action = action;
        }

        private boolean isApplicableOn(final P item) {
            return precondition.test(item);
        }

        private void applyOn(final A item) {
            action.accept(item);
        }
    }
}
