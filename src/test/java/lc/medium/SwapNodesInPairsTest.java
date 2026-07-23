package lc.medium;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import lc.medium.SwapNodesInPairs.ListNode;
import org.junit.jupiter.api.Test;

class SwapNodesInPairsTest {

    private final SwapNodesInPairs solution = new SwapNodesInPairs();

    @Test
    void swapsEvenLengthList() {
        assertArrayEquals(new int[] {2, 1, 4, 3},
                toArray(solution.swapPairs(fromArray(1, 2, 3, 4))));
    }

    @Test
    void leavesTrailingNodeInPlaceForOddLength() {
        assertArrayEquals(new int[] {2, 1, 3},
                toArray(solution.swapPairs(fromArray(1, 2, 3))));
    }

    @Test
    void handlesEmptyList() {
        assertArrayEquals(new int[] {}, toArray(solution.swapPairs(fromArray())));
    }

    @Test
    void handlesSingleNode() {
        assertArrayEquals(new int[] {1}, toArray(solution.swapPairs(fromArray(1))));
    }

    private ListNode fromArray(int... values) {
        ListNode dummy = new ListNode();
        ListNode current = dummy;
        for (int value : values) {
            current.next = new ListNode(value);
            current = current.next;
        }
        return dummy.next;
    }

    private int[] toArray(ListNode head) {
        int length = 0;
        for (ListNode node = head; node != null; node = node.next) {
            length++;
        }
        int[] values = new int[length];
        int index = 0;
        for (ListNode node = head; node != null; node = node.next) {
            values[index++] = node.val;
        }
        return values;
    }
}
