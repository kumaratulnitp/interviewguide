package lc.medium;

import java.util.Arrays;

/**
 * LeetCode 322 - Coin Change
 *
 * Problem statement:
 * Given an integer array coins representing coin denominations and an integer
 * amount, return the fewest coins needed to make exactly that amount. Return
 * -1 when the amount cannot be made. Each denomination may be used any number
 * of times.
 *
 * Examples:
 * - Input: coins = [1,2,5], amount = 11; Output: 3 (5 + 5 + 1).
 * - Input: coins = [2], amount = 3; Output: -1.
 *
 * Algorithm (bottom-up dynamic programming):
 * 1. Make an array where entry a means the fewest coins needed for amount a.
 * 2. Set amount 0 to 0 coins; set all other entries to an impossible value.
 * 3. For every amount from 1 through the target, try each usable coin and use
 *    the already-computed answer for amount minus that coin.
 * 4. Return the target entry if it was reached; otherwise return -1.
 *
 * Time complexity: O(amount * number of coins). Space complexity: O(amount).
 */
public class CoinChange {

    public int coinChange(int[] coins, int amount) {
        int[] minimumCoins = new int[amount + 1];
        Arrays.fill(minimumCoins, amount + 1);
        minimumCoins[0] = 0;

        for (int currentAmount = 1; currentAmount <= amount; currentAmount++) {
            for (int coin : coins) {
                if (coin <= currentAmount) {
                    minimumCoins[currentAmount] = Math.min(minimumCoins[currentAmount],
                            minimumCoins[currentAmount - coin] + 1);
                }
            }
        }
        return minimumCoins[amount] > amount ? -1 : minimumCoins[amount];
    }
}

/*
 * Discussion:
 * Brute force tries every possible next coin recursively. It is correct, but
 * many paths ask for the best answer to the same remaining amount, producing an
 * exponential number of repeated calculations. The dynamic-programming insight
 * is that the answer for an amount depends on already-solved smaller amounts.
 *
 * Top-down recursion with memoization solves the same recurrence and can be more
 * natural to derive in an interview. Bottom-up DP avoids recursion depth and
 * makes the O(amount) table explicit. A greedy strategy works for some coin
 * systems, but not all: coins [1, 3, 4] and amount 6 need 3 + 3, not 4 + 1 + 1.
 */
