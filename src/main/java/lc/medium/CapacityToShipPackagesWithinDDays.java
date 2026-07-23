package lc.medium;

/**
 * LeetCode 1011 - Capacity To Ship Packages Within D Days
 *
 * Problem statement:
 * Packages on a conveyor belt must be shipped in order within the given number
 * of days. Each day the ship loads packages, in belt order, without exceeding
 * its weight capacity. Return the least capacity that still ships everything
 * within the allotted days.
 *
 * Examples:
 * - Input: weights = [1,2,3,4,5,6,7,8,9,10], days = 5; Output: 15.
 * - Input: weights = [3,2,2,4,1,4], days = 3; Output: 6.
 * - Input: weights = [1,2,3,1,1], days = 4; Output: 3.
 *
 * Algorithm (binary search on the answer):
 * 1. The answer lies between the heaviest single package (a day must carry it)
 *    and the total weight (everything in one day).
 * 2. For a candidate capacity, greedily fill each day until the next package
 *    would overflow, counting the days required.
 * 3. If the required days fit the limit the capacity works, so search lower;
 *    otherwise search higher.
 * 4. Converge on the smallest feasible capacity.
 *
 * Time complexity: O(n * log(sum of weights)). Space complexity: O(1).
 */
public class CapacityToShipPackagesWithinDDays {

    public int shipWithinDays(int[] weights, int days) {
        int low = 0;
        int high = 0;
        for (int weight : weights) {
            low = Math.max(low, weight);
            high += weight;
        }

        while (low < high) {
            int capacity = low + (high - low) / 2;
            if (canShip(weights, days, capacity)) {
                high = capacity;
            } else {
                low = capacity + 1;
            }
        }
        return low;
    }

    private boolean canShip(int[] weights, int days, int capacity) {
        int daysNeeded = 1;
        int load = 0;
        for (int weight : weights) {
            if (load + weight > capacity) {
                daysNeeded++;
                load = 0;
            }
            load += weight;
        }
        return daysNeeded <= days;
    }
}

/*
 * Discussion:
 * Trying every capacity from the heaviest package upward and simulating the
 * shipment is correct but linear in the range of capacities. The key property
 * is monotonicity: if some capacity ships in time, every larger capacity does
 * too. That yes/no boundary is exactly what binary search locates, collapsing
 * the range scan into a logarithmic number of feasibility checks.
 *
 * The feasibility check itself is greedy - packing each day as full as possible
 * is optimal because packages must ship in fixed order, so delaying a package
 * never reduces the day count. This "binary search on the answer" pattern
 * recurs whenever a monotonic predicate sits over a numeric range, such as
 * Koko Eating Bananas or splitting an array to minimize the largest sum.
 *
 * Follow-ups and edge cases: the two extremes anchor the search - when days
 * equals the package count the answer is the heaviest package, and when days is
 * 1 it is the total weight. The low bound must be the maximum weight, not 0, or
 * a single package could never fit. This is literally the same problem as Split
 * Array Largest Sum (LeetCode 410) and shares the pattern with Koko Eating
 * Bananas (LeetCode 875) and book/painter allocation. A tempting twist - "what
 * if packages may be reordered" - removes the fixed-order guarantee and becomes
 * bin packing, which is NP-hard, so the greedy check no longer applies.
 */
