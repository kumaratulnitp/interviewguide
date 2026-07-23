package lc.medium;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * LeetCode 102 - Binary Tree Level Order Traversal
 *
 * Problem statement:
 * Given the root of a binary tree, return the values of its nodes level by
 * level from left to right. Each level must be returned as a separate list.
 *
 * Example:
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [[3],[9,20],[15,7]]
 *
 * Algorithm (breadth-first search):
 * 1. Return an empty result when the root is null; otherwise put the root in a queue.
 * 2. The queue size at the start of an iteration is exactly one tree level.
 * 3. Remove that many nodes, append their values to a level list, and add each
 *    non-null child to the queue.
 * 4. Add the completed level list to the result and repeat until the queue is empty.
 *
 * Time complexity: O(n). Space complexity: O(n) in the widest level.
 */
public class BinaryTreeLevelOrderTraversal {

    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }

        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>(levelSize);
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.remove();
                level.add(node.val);
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            levels.add(level);
        }
        return levels;
    }

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }
}

/*
 * Discussion:
 * A straightforward recursive depth-first traversal can visit every node, but
 * it must carry the depth and create or locate the correct result list. The
 * important clue is the phrase "level by level": breadth-first search naturally
 * visits all nodes at one depth before moving to the next.
 *
 * The queue-size technique is the standard iterative solution. DFS is also a
 * valid alternative when a helper receives the depth; it has the same O(n) time
 * complexity but uses the tree height on the call stack rather than a queue.
 *
 * Follow-ups and edge cases: a null root returns an empty list, a single node
 * returns one level, and a completely skewed tree produces one node per level,
 * which stresses the per-level bookkeeping. The same queue skeleton solves a
 * whole family of variants: zigzag order (LeetCode 103) reverses alternate
 * levels, bottom-up order (LeetCode 107) reverses the result, the right-side
 * view (LeetCode 199) keeps the last node of each level, and level averages
 * (LeetCode 637) reduce each level instead of collecting it.
 */
