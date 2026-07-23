package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class CombinationSumIIITest {

    private final CombinationSumIII solution = new CombinationSumIII();

    @Test
    void findsTheSingleCombination() {
        assertEquals(List.of(List.of(1, 2, 4)), solution.combinationSum3(3, 7));
    }

    @Test
    void findsAllCombinationsInAscendingOrder() {
        assertEquals(
                List.of(List.of(1, 2, 6), List.of(1, 3, 5), List.of(2, 3, 4)),
                solution.combinationSum3(3, 9));
    }

    @Test
    void returnsEmptyWhenTargetIsUnreachable() {
        assertEquals(List.of(), solution.combinationSum3(4, 1));
    }

    @Test
    void returnsEmptyWhenTargetExceedsTheMaximumSum() {
        assertEquals(List.of(), solution.combinationSum3(2, 18));
    }
}
