package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class TopKFrequentWordsTest {

    private final TopKFrequentWords solution = new TopKFrequentWords();

    @Test
    void breaksFrequencyTiesAlphabetically() {
        assertEquals(List.of("i", "love"), solution.topKFrequent(
                new String[] {"i", "love", "leetcode", "i", "love", "coding"}, 2));
    }

    @Test
    void ordersByDescendingFrequencyThenAlphabetically() {
        assertEquals(List.of("the", "is", "sunny", "day"), solution.topKFrequent(
                new String[] {"the", "day", "is", "sunny", "the", "the", "the",
                        "sunny", "is", "is"}, 4));
    }

    @Test
    void returnsSingleMostFrequentWord() {
        assertEquals(List.of("apple"), solution.topKFrequent(
                new String[] {"apple", "apple", "banana"}, 1));
    }
}
