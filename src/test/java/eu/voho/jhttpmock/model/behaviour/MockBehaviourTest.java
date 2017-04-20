package eu.voho.jhttpmock.model.behaviour;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@RunWith(MockitoJUnitRunner.class)
public class MockBehaviourTest {
    private static final Object MATCHED = new Object();

    private TestCaptor ruleDivisibleByTwo;
    private TestCaptor ruleDivisibleByFour;
    private TestCaptor ruleDivisibleByEight;
    private MockBehaviour<Integer, Object> toTest;

    @Before
    public void setUp() {
        ruleDivisibleByTwo = new TestCaptor();
        ruleDivisibleByFour = new TestCaptor();
        ruleDivisibleByEight = new TestCaptor();

        toTest = new MockBehaviour<>();
        toTest.addRule((nr) -> nr % 2 == 0, ruleDivisibleByTwo);
        toTest.addRule((nr) -> nr % 4 == 0, ruleDivisibleByFour);
        toTest.addRule((nr) -> nr % 8 == 0, ruleDivisibleByEight);
    }

    @Test
    public void givenMockRules_whenValueMatchingOnlyOneRule_thenTheOnlyRuleIsApplied() {
        assertThat(toTest.applyBestMatchingRule(2, MATCHED)).isTrue();

        assertThat(ruleDivisibleByTwo.matched()).isTrue();
        assertThat(ruleDivisibleByFour.matched()).isFalse();
        assertThat(ruleDivisibleByEight.matched()).isFalse();
    }

    @Test
    public void givenMockRules_whenValueMatchingTwoRules_thenOnlyTheLatestRuleIsApplied() {
        assertThat(toTest.applyBestMatchingRule(4, MATCHED)).isTrue();

        assertThat(ruleDivisibleByTwo.matched()).isFalse();
        assertThat(ruleDivisibleByFour.matched()).isTrue();
        assertThat(ruleDivisibleByEight.matched()).isFalse();
    }

    @Test
    public void givenMockRules_whenValueMatchingThreeRules_thenOnlyTheLatestRuleIsApplied() {
        assertThat(toTest.applyBestMatchingRule(8, MATCHED)).isTrue();

        assertThat(ruleDivisibleByTwo.matched()).isFalse();
        assertThat(ruleDivisibleByFour.matched()).isFalse();
        assertThat(ruleDivisibleByEight.matched()).isTrue();
    }

    @Test
    public void givenMockRules_whenValueMatchingNoRules_thenNoRuleIsApplied() {
        assertThat(toTest.applyBestMatchingRule(7, MATCHED)).isFalse();

        assertThat(ruleDivisibleByTwo.matched()).isFalse();
        assertThat(ruleDivisibleByFour.matched()).isFalse();
        assertThat(ruleDivisibleByEight.matched()).isFalse();
    }

    private static class TestCaptor implements Consumer<Object> {
        private Object captured;

        boolean matched() {
            return captured == MATCHED;
        }

        @Override
        public void accept(final Object value) {
            if (captured == null) {
                captured = value;
            } else {
                fail("Already has a value captured!");
            }
        }
    }
}