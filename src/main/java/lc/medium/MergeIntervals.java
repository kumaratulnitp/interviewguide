package lc.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * LeetCode 56 - Merge Intervals
 *
 * Problem statement:
 * Given an array of intervals where intervals[i] = [start_i, end_i], merge all
 * overlapping intervals and return the non-overlapping intervals that cover
 * all input intervals.
 *
 * Example:
 * Input: [[1,3],[2,6],[8,10],[15,18]]; Output: [[1,6],[8,10],[15,18]].
 *
 * Algorithm:
 * 1. Sort intervals by their starting value.
 * 2. Start the merged result with the first interval.
 * 3. For each following interval, compare its start with the end of the last
 *    merged interval. If they overlap, extend that end if necessary.
 * 4. Otherwise, it starts a new merged interval.
 *
 * Time complexity: O(n log n) for sorting. Space complexity: O(n) for output.
 */
public class MergeIntervals {

    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][0];
        }

        Arrays.sort(intervals, Comparator.comparingInt(interval -> interval[0]));
        List<int[]> merged = new ArrayList<>();
        merged.add(intervals[0]);

        for (int index = 1; index < intervals.length; index++) {
            int[] current = intervals[index];
            int[] previous = merged.get(merged.size() - 1);
            if (current[0] <= previous[1]) {
                previous[1] = Math.max(previous[1], current[1]);
            } else {
                merged.add(current);
            }
        }
        return merged.toArray(new int[0][]);
    }
}

/*
 * Discussion:
 * A brute-force idea is to compare every interval with every other interval and
 * repeatedly merge overlaps. Besides being O(n^2), it becomes awkward because a
 * newly merged interval can create further overlaps. Sorting by start time gives
 * a useful order: once an interval does not overlap the current merged interval,
 * it cannot overlap any earlier merged interval either.
 *
 * The same sorted sweep can be expressed with a stack or by writing directly to
 * an output array. For streaming intervals that are already sorted, sorting is
 * unnecessary; for arbitrary input, the O(n log n) sort is the main cost.
 */
