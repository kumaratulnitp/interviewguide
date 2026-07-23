package lc.medium;

/**
 * LeetCode 24 - Swap Nodes in Pairs
 *
 * Problem statement:
 * Given the head of a singly linked list, swap every two adjacent nodes and
 * return the new head. The nodes themselves must be relinked; their values may
 * not be changed. An odd trailing node stays in place.
 *
 * Examples:
 * - Input: 1 -> 2 -> 3 -> 4; Output: 2 -> 1 -> 4 -> 3.
 * - Input: (empty);          Output: (empty).
 * - Input: 1 -> 2 -> 3;      Output: 2 -> 1 -> 3.
 *
 * Algorithm (dummy head with pointer relinking):
 * 1. Put a dummy node before the head so the first pair is handled uniformly.
 * 2. Keep a pointer to the node preceding the pair being swapped.
 * 3. For each pair (first, second), rewire: first points past second, second
 *    points to first, and the preceding node points to second.
 * 4. Advance the preceding pointer to first (now the pair's tail) and repeat
 *    while at least two nodes remain.
 *
 * Time complexity: O(n). Space complexity: O(1).
 */
public class SwapNodesInPairs {

    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode previous = dummy;

        while (previous.next != null && previous.next.next != null) {
            ListNode first = previous.next;
            ListNode second = first.next;

            first.next = second.next;
            second.next = first;
            previous.next = second;

            previous = first;
        }
        return dummy.next;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}

/*
 * Discussion:
 * Copying values into an array, swapping pairs there, and writing them back
 * solves it in O(n) time and space, but the problem forbids touching values, so
 * the real exercise is pointer manipulation. Doing that in place needs care:
 * three links change per pair and the order of reassignment matters, or a node
 * reference is lost.
 *
 * The dummy head removes the special case where the very first pair changes the
 * list's head, letting one loop body handle every pair. A recursive version is
 * shorter - swap the first two, then recurse on the rest - but it uses O(n)
 * stack space, whereas the iterative form is O(1).
 *
 * Follow-ups and edge cases: an empty list and a single node return unchanged,
 * and an odd-length list leaves its final node in place - the loop guard on
 * previous.next.next handles both. The dummy node is what makes the new head
 * fall out without a special case. The classic extension is reversing nodes in
 * groups of k (LeetCode 25), of which this is the k = 2 instance; interviewers
 * also probe the recursive-versus-iterative trade-off and the constraint that
 * values must not be swapped, forcing genuine pointer relinking rather than a
 * data copy.
 */
