package lc.medium;

/**
 * LeetCode 200 - Number of Islands
 *
 * Problem statement:
 * Given an m x n grid of '1' values (land) and '0' values (water), return the
 * number of islands. An island consists of horizontally or vertically adjacent
 * land cells and is surrounded by water. Diagonal cells do not connect islands.
 *
 * Example:
 * Input: [["1","1","0"],["1","0","0"],["0","0","1"]]; Output: 2.
 *
 * Algorithm (depth-first search):
 * 1. Visit every grid cell.
 * 2. Each time an unvisited land cell is found, count one island.
 * 3. Run DFS from that cell in the four cardinal directions, changing every
 *    reachable land cell to water so it cannot be counted again.
 * 4. Continue scanning until all cells have been visited.
 *
 * Time complexity: O(m * n). Space complexity: O(m * n) in the worst DFS case.
 * This implementation intentionally modifies the input grid.
 */
public class NumberOfIslands {

    public int numIslands(char[][] grid) {
        int islands = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                if (grid[row][column] == '1') {
                    islands++;
                    sinkIsland(grid, row, column);
                }
            }
        }
        return islands;
    }

    private void sinkIsland(char[][] grid, int row, int column) {
        if (row < 0 || row == grid.length || column < 0 || column == grid[row].length
                || grid[row][column] != '1') {
            return;
        }
        grid[row][column] = '0';
        sinkIsland(grid, row + 1, column);
        sinkIsland(grid, row - 1, column);
        sinkIsland(grid, row, column + 1);
        sinkIsland(grid, row, column - 1);
    }
}

/*
 * Discussion:
 * A brute-force scan alone finds land cells but cannot tell whether two cells
 * belong to the same island. The key idea is to treat land cells as vertices in
 * a graph: when one unvisited land cell is found, explore its entire connected
 * component and count it once. Marking cells as water doubles as the visited set.
 *
 * Breadth-first search with a queue is equivalent to this DFS solution. A union-
 * find structure is another option, especially when land is added dynamically,
 * but it is more machinery for a one-time static-grid count. An iterative DFS or
 * BFS avoids stack-overflow risk for a very large, connected grid.
 */
