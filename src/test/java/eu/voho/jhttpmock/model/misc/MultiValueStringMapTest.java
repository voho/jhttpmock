package eu.voho.jhttpmock.model.misc;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiValueStringMapTest {
    @Test
    public void givenEmptyMap_whenMatching_thenNoMatchIsFound() {
        final MultiValueStringMap toTest = new MultiValueStringMap(Collections.emptyMap());

        assertThat(toTest.contains(MultiValueStringMap.ANY_KEY, MultiValueStringMap.ANY_VALUES)).isFalse();
    }

    @Test
    public void givenMapWithMultipleEntries_whenMatchingSingleKeyAnyValue_thenSingleMatchIsFound() {
        final Map<String, String[]> map = new HashMap<>();
        map.put("odd", new String[]{"one", "three", "five"});
        map.put("even", new String[]{"two", "four", "six"});
        final MultiValueStringMap toTest = new MultiValueStringMap(map);

        assertThat(toTest.contains("odd"::equals, MultiValueStringMap.ANY_VALUES)).isTrue();
        assertThat(toTest.contains("even"::equals, MultiValueStringMap.ANY_VALUES)).isTrue();
    }

    @Test
    public void givenMapWithMultipleEntries_whenMatchingSingleKeyAndSingleValue_thenOnlyMatchingEntriesAreFound() {
        final Map<String, String[]> map = new HashMap<>();
        map.put("odd", new String[]{"one", "three", "five"});
        map.put("even", new String[]{"two", "four", "six"});
        final MultiValueStringMap toTest = new MultiValueStringMap(map);

        assertThat(toTest.contains("odd"::equals, s -> s.contains("one"))).isTrue();
        assertThat(toTest.contains("odd"::equals, s -> s.contains("two"))).isFalse();
        assertThat(toTest.contains("even"::equals, s -> s.contains("two"))).isTrue();
        assertThat(toTest.contains("even"::equals, s -> s.contains("three"))).isFalse();
    }

    @Test
    public void givenMapWithMultipleEntries_whenMatchingSingleKey_thenAllKeysWithThatValueAreFound() {
        final Map<String, String[]> map = new HashMap<>();
        map.put("first", new String[]{"a", "b"});
        map.put("second", new String[]{"a", "c"});
        final MultiValueStringMap toTest = new MultiValueStringMap(map);

        assertThat(toTest.contains(MultiValueStringMap.ANY_KEY, s -> s.contains("a"))).isTrue();
        assertThat(toTest.contains(MultiValueStringMap.ANY_KEY, s -> s.contains("b"))).isTrue();
        assertThat(toTest.contains(MultiValueStringMap.ANY_KEY, s -> s.contains("c"))).isTrue();
    }
}