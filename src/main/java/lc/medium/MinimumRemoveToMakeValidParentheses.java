package lc.medium;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 1249 - Minimum Remove to Make Valid Parentheses
 *
 * Problem statement:
 * Given a string of '(' , ')' and lowercase letters, remove the fewest
 * parentheses (at any positions) so the remaining string is valid, and return
 * any such result. A string is valid when every '(' has a matching later ')'
 * and every ')' has a matching earlier '('.
 *
 * Examples:
 * - Input: "lee(t(c)o)de)"; Output: "lee(t(c)o)de" (the last ')' is unmatched).
 * - Input: "a)b(c)d";       Output: "ab(c)d".
 * - Input: "))((";          Output: "".
 *
 * Algorithm (stack of open-parenthesis indices):
 * 1. Scan left to right, pushing the index of every '(' onto a stack.
 * 2. On a ')', pop a matching '(' if one is available; otherwise the ')' is
 *    unmatched, so record its index for removal.
 * 3. Any indices left on the stack are unmatched '(' - record them too.
 * 4. Rebuild the string, skipping every recorded index.
 *
 * Time complexity: O(n). Space complexity: O(n).
 */
public class MinimumRemoveToMakeValidParentheses {

    public String minRemoveToMakeValid(String s) {
        Set<Integer> indexesToRemove = new HashSet<>();
        Deque<Integer> openParentheses = new ArrayDeque<>();

        for (int index = 0; index < s.length(); index++) {
            char character = s.charAt(index);
            if (character == '(') {
                openParentheses.push(index);
            } else if (character == ')') {
                if (openParentheses.isEmpty()) {
                    indexesToRemove.add(index);
                } else {
                    openParentheses.pop();
                }
            }
        }
        indexesToRemove.addAll(openParentheses);

        StringBuilder result = new StringBuilder();
        for (int index = 0; index < s.length(); index++) {
            if (!indexesToRemove.contains(index)) {
                result.append(s.charAt(index));
            }
        }
        return result.toString();
    }
}

/*
 * Discussion:
 * Repeatedly deleting one bad parenthesis and rescanning is correct but does
 * redundant passes. The insight is that validity is a matching problem: a ')'
 * is bad only when no earlier unmatched '(' exists, and a '(' is bad only when
 * no later ')' ever matches it. A stack tracks the currently unmatched '(' as
 * we scan, so both kinds of offenders fall out in a single pass.
 *
 * A two-counter variant avoids the extra set: one forward pass drops ')' that
 * would make the balance negative, one backward pass drops surplus '('. The
 * stack version is easier to reason about and generalizes to multiple bracket
 * types, at the cost of O(n) extra space.
 *
 * Follow-ups and edge cases: an empty string, a string of only letters, and an
 * already-valid string all return unchanged; strings like "(((" or ")))" reduce
 * to the letters alone. A natural extension is supporting several bracket types
 * ([], {}, ()), which needs a stack of the actual characters so a closer can be
 * matched against the right opener. Related problems flip the goal to counting
 * or making the minimum insertions to balance (LeetCode 921 and 1541), and
 * validity with a wildcard '*' (LeetCode 678) needs a range of possible balances
 * rather than a single stack.
 */
