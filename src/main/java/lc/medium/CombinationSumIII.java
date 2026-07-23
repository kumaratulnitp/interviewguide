package lc.medium;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 216 - Combination Sum III
 *
 * Problem statement:
 * Find all combinations of exactly k distinct numbers taken from 1 through 9
 * that add up to n. Each number may be used at most once, and each combination
 * must be unique. Return the combinations in any order.
 *
 * Examples:
 * - Input: k = 3, n = 7; Output: [[1,2,4]].
 * - Input: k = 3, n = 9; Output: [[1,2,6],[1,3,5],[2,3,4]].
 * - Input: k = 4, n = 1; Output: [] (cannot reach 1 with four distinct digits).
 *
 * Algorithm (backtracking):
 * 1. Build a combination digit by digit, always choosing from numbers greater
 *    than the last one picked so combinations stay sorted and unique.
 * 2. Track the remaining sum; a chosen number larger than it can be pruned, and
 *    because candidates are ascending the whole branch can stop.
 * 3. When k numbers are chosen, keep the combination only if the remaining sum
 *    is exactly 0.
 * 4. Undo each choice before trying the next candidate.
 *
 * Time complexity: O(C(9, k) * k) to build the results. Space complexity: O(k)
 * for the recursion stack, ignoring the output.
 */
public class CombinationSumIII {

    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> combinations = new ArrayList<>();
        backtrack(k, n, 1, new ArrayList<>(), combinations);
        return combinations;
    }

    private void backtrack(int k, int remaining, int start, List<Integer> current,
            List<List<Integer>> combinations) {
        if (current.size() == k) {
            if (remaining == 0) {
                combinations.add(new ArrayList<>(current));
            }
            return;
        }
        for (int number = start; number <= 9; number++) {
            if (number > remaining) {
                break;
            }
            current.add(number);
            backtrack(k, remaining - number, number + 1, current, combinations);
            current.remove(current.size() - 1);
        }
    }
}

/*
 * Discussion:
 * A brute-force approach would enumerate every subset of the nine digits and
 * filter by size and sum, doing wasted work on subsets that are already too big
 * or too heavy. Backtracking instead grows one partial combination and abandons
 * a branch the moment it cannot succeed.
 *
 * Two pruning rules keep the search tight: forcing candidates to ascend removes
 * duplicate orderings for free, and breaking once a candidate exceeds the
 * remaining sum cuts entire subtrees. The search space is tiny (digits 1-9), so
 * this is effectively constant work, but the same structure scales to the
 * general Combination Sum problems where pruning matters far more.
 *
 * Follow-ups and edge cases: impossible inputs return an empty list - k greater
 * than 9, an n below the smallest k-digit sum, or an n above the largest (for
 * k = 2 the reachable range is 3 to 17). The whole Combination Sum family makes
 * good follow-ups and each changes one rule: LeetCode 39 reuses candidates
 * without limit, LeetCode 40 has duplicate candidates and must skip repeats at
 * the same depth, and LeetCode 377 counts ordered permutations, which turns into
 * a counting DP rather than backtracking. Dropping the "digits 1-9 only" limit
 * generalizes this to an arbitrary candidate set.
 */
