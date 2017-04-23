package eu.voho.jhttpmock.model.misc;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * Utility class holding a multi-map of strings.
 */
public class MultiValueStringMap {
    public static final Predicate<String> ANY_KEY = (p) -> true;
    public static final Predicate<Set<String>> ANY_VALUES = (p) -> true;

    private final Map<String, String[]> values;

    public MultiValueStringMap(final Map<String, String[]> values) {
        this.values = values;
    }

    /**
     * Tests values of a given key.
     * First, the key is matched, then all values of that key are matched.
     * Returns TRUE only if both matches are found.
     * @param keyMatcher key predicate
     * @param valueMatcher value predicate (only matched against the values of the matching key)
     * @return TRUE if the match is found, FALSE otherwise
     */
    public boolean contains(final Predicate<String> keyMatcher, final Predicate<Set<String>> valueMatcher) {
        for (final Map.Entry<String, String[]> entry : values.entrySet()) {
            if (keyMatcher.test(entry.getKey())) {
                if (valueMatcher.test(new TreeSet<>(Arrays.asList(entry.getValue())))) {
                    return true;
                }
            }
        }

        return false;
    }
}
