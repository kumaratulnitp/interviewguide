package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CapacityToShipPackagesWithinDDaysTest {

    private final CapacityToShipPackagesWithinDDays solution =
            new CapacityToShipPackagesWithinDDays();

    @Test
    void findsTheLeastCapacityAcrossFiveDays() {
        assertEquals(15, solution.shipWithinDays(
                new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5));
    }

    @Test
    void findsTheLeastCapacityAcrossThreeDays() {
        assertEquals(6, solution.shipWithinDays(new int[] {3, 2, 2, 4, 1, 4}, 3));
    }

    @Test
    void handlesMoreDaysThanPackages() {
        assertEquals(3, solution.shipWithinDays(new int[] {1, 2, 3, 1, 1}, 4));
    }

    @Test
    void shipsEverythingInASingleDay() {
        assertEquals(15, solution.shipWithinDays(new int[] {1, 2, 3, 4, 5}, 1));
    }
}
