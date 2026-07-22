package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NumberOfIslandsTest {

    private final NumberOfIslands solution = new NumberOfIslands();

    @Test
    void countsSeparatedIslands() {
        assertEquals(3, solution.numIslands(grid(
                "11000",
                "11000",
                "00100",
                "00011")));
    }

    @Test
    void countsOneConnectedIsland() {
        assertEquals(1, solution.numIslands(grid(
                "11110",
                "11010",
                "11000",
                "00000")));
    }

    @Test
    void handlesOnlyWater() {
        assertEquals(0, solution.numIslands(grid("00", "00")));
    }

    private char[][] grid(String... rows) {
        char[][] grid = new char[rows.length][];
        for (int index = 0; index < rows.length; index++) {
            grid[index] = rows[index].toCharArray();
        }
        return grid;
    }
}
