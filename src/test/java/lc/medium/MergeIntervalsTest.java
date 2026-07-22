package lc.medium;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class MergeIntervalsTest {

    private final MergeIntervals solution = new MergeIntervals();

    @Test
    void mergesOverlappingIntervals() {
        assertArrayEquals(new int[][] {{1, 6}, {8, 10}, {15, 18}},
                solution.merge(new int[][] {{1, 3}, {2, 6}, {8, 10}, {15, 18}}));
    }

    @Test
    void mergesIntervalsThatTouch() {
        assertArrayEquals(new int[][] {{1, 5}},
                solution.merge(new int[][] {{1, 4}, {4, 5}}));
    }

    @Test
    void leavesDisjointIntervalsSeparate() {
        assertArrayEquals(new int[][] {{1, 2}, {5, 7}},
                solution.merge(new int[][] {{5, 7}, {1, 2}}));
    }
}
