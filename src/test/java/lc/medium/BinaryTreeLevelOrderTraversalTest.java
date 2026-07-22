package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class BinaryTreeLevelOrderTraversalTest {

    private final BinaryTreeLevelOrderTraversal solution = new BinaryTreeLevelOrderTraversal();

    @Test
    void traversesMultipleLevelsFromLeftToRight() {
        BinaryTreeLevelOrderTraversal.TreeNode root = node(3, node(9), node(20, node(15), node(7)));

        assertEquals(List.of(List.of(3), List.of(9, 20), List.of(15, 7)), solution.levelOrder(root));
    }

    @Test
    void handlesASingleNode() {
        assertEquals(List.of(List.of(1)), solution.levelOrder(node(1)));
    }

    @Test
    void handlesAnEmptyTree() {
        assertEquals(List.of(), solution.levelOrder(null));
    }

    private BinaryTreeLevelOrderTraversal.TreeNode node(int value) {
        return new BinaryTreeLevelOrderTraversal.TreeNode(value);
    }

    private BinaryTreeLevelOrderTraversal.TreeNode node(
            int value,
            BinaryTreeLevelOrderTraversal.TreeNode left,
            BinaryTreeLevelOrderTraversal.TreeNode right) {
        BinaryTreeLevelOrderTraversal.TreeNode node = node(value);
        node.left = left;
        node.right = right;
        return node;
    }
}
