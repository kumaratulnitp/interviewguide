package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CoinChangeTest {

    private final CoinChange solution = new CoinChange();

    @Test
    void findsTheMinimumNumberOfCoins() {
        assertEquals(3, solution.coinChange(new int[] {1, 2, 5}, 11));
    }

    @Test
    void returnsMinusOneForAnImpossibleAmount() {
        assertEquals(-1, solution.coinChange(new int[] {2}, 3));
    }

    @Test
    void returnsZeroForZeroAmount() {
        assertEquals(0, solution.coinChange(new int[] {1}, 0));
    }

    @Test
    void recursiveFindsTheMinimumNumberOfCoins() {
        assertEquals(3, solution.coinChangeRecursive(new int[] {1, 2, 5}, 11));
    }

    @Test
    void recursiveReturnsMinusOneForAnImpossibleAmount() {
        assertEquals(-1, solution.coinChangeRecursive(new int[] {2}, 3));
    }
}
