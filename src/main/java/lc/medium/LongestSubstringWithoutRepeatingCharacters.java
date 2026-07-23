package lc.medium;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 3 - Longest Substring Without Repeating Characters
 *
 * Problem statement:
 * Given a string s, find the length of the longest substring that contains no
 * repeating characters. A substring is a contiguous sequence of characters.
 *
 * Examples:
 * - Input: s = "abcabcbb"; Output: 3; Explanation: "abc" has length 3.
 * - Input: s = "bbbbb"; Output: 1; Explanation: "b" has length 1.
 *
 * Algorithm (sliding window):
 * 1. Keep a window from left to right whose characters are all unique.
 * 2. Remember the latest index at which each character appeared.
 * 3. When the current character already belongs to the window, move left to
 *    one position after its previous occurrence.
 * 4. Record the largest window length seen while scanning the string once.
 *
 * Time complexity: O(n). Space complexity: O(min(n, alphabet size)).
 */
public class LongestSubstringWithoutRepeatingCharacters {

    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> latestIndex = new HashMap<>();
        int longest = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char character = s.charAt(right);
            if (latestIndex.containsKey(character)) {
                left = Math.max(left, latestIndex.get(character) + 1);
            }
            latestIndex.put(character, right);
            longest = Math.max(longest, right - left + 1);
        }
        return longest;
    }
}

/*
 * Discussion:
 * Brute force starts every possible substring and checks whether its characters
 * are unique. That can take O(n^3) time when uniqueness is checked from scratch.
 * The key observation is that adjacent candidate substrings share most of their
 * characters, so a sliding window reuses work instead of rebuilding it.
 *
 * A HashSet-based window is another common O(n) approach: repeatedly remove
 * characters from the left until the duplicate disappears. This version stores
 * last-seen indexes, allowing the left boundary to jump directly forward.
 *
 * Follow-ups and edge cases: an empty string returns 0, a single character
 * returns 1, and an all-identical string like "bbbb" returns 1. The input may
 * contain spaces, digits, or symbols, so avoid assuming a lowercase-only
 * alphabet. Interviewers often ask to return the substring itself rather than
 * its length, or to relax the constraint to "at most k distinct characters"
 * (LeetCode 340) or "at most two distinct characters" (LeetCode 159), all of
 * which are the same window with a different shrink condition.
 */
