package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LongestSubstringWithoutRepeatingCharactersTest {

    private final LongestSubstringWithoutRepeatingCharacters solution =
            new LongestSubstringWithoutRepeatingCharacters();

    @Test
    void findsLongestWindowAfterRepeatedCharacters() {
        assertEquals(3, solution.lengthOfLongestSubstring("abcabcbb"));
    }

    @Test
    void handlesOneRepeatedCharacter() {
        assertEquals(1, solution.lengthOfLongestSubstring("bbbbb"));
    }

    @Test
    void handlesAnEmptyString() {
        assertEquals(0, solution.lengthOfLongestSubstring(""));
    }
}
