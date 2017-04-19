package eu.voho.jhttpmock.model.behaviour;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MockBehaviourTest {
    @InjectMocks
    private MockBehaviour<Integer, Integer> toTest;

    @Test
    public void givenAllMatchingRule_whenResetIsPerformed_thenNoRequestIsMatched() {

    }
}