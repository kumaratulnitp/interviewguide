# Data Structures and Algorithms Interview Questions

Questions and concise answer points for computer-science graduates. In an interview, state the idea, the time and space complexity, and a small example before writing code. Unless stated otherwise, assume Java collections such as `ArrayList`, `HashMap`, `ArrayDeque`, and `PriorityQueue` are available.

## Foundations and Complexity

1. **What is a data structure? What is an algorithm?**
   - A data structure organizes data so operations can be performed efficiently; an algorithm is a step-by-step method for solving a problem using that data.
   - For example, an array stores values by index, while binary search is an algorithm for finding a value in a *sorted* array. Choosing the right structure often matters as much as the algorithm.

2. **What do time complexity and space complexity mean?**
   - Time complexity describes how the number of operations grows with input size `n`. Space complexity describes extra memory used as `n` grows. Big-O gives an upper growth bound and ignores constants and smaller terms.
   - A single loop over `n` elements is usually `O(n)`. Two nested loops that each run `n` times are `O(n^2)`. An algorithm that creates another array of size `n` uses `O(n)` auxiliary space.

3. **Compare `O(1)`, `O(log n)`, `O(n)`, `O(n log n)`, and `O(n^2)`.**
   - `O(1)` does not grow with input size, such as reading `array[5]`. `O(log n)` repeatedly halves the search range, such as binary search. `O(n)` scans every element. Efficient comparison sorts are usually `O(n log n)`. Comparing every pair is `O(n^2)`.
   - For a million elements, an `O(n^2)` approach can require about a trillion comparisons, whereas `O(n log n)` is closer to tens of millions. This is why avoiding a nested full scan is important.

4. **What is the difference between an array and a linked list?**
   - An array stores elements contiguously, so indexing is `O(1)`, but inserting in the middle is usually `O(n)` because later elements shift. A linked list stores nodes connected by references, so reaching an index is `O(n)`.
   - If the node position is already known, insertion or deletion in a linked list is `O(1)`. In practice, arrays often have better cache locality and lower memory overhead, so an `ArrayList` is frequently the practical default.

5. **What is recursion? What are its risks?**
   - Recursion solves a problem by calling the same function on a smaller case. It needs a base case that stops the calls and a recursive case that makes progress toward it.
   - Computing factorial uses `factorial(n) = n * factorial(n - 1)` with `factorial(0) = 1`. Deep recursion uses one stack frame per call and can cause `StackOverflowError` in Java; an iterative solution may be safer.

6. **When would you use iteration instead of recursion?**
   - Use iteration when the work is naturally sequential, when recursion depth can be large, or when an explicit stack or queue makes the state clearer. Use recursion when the problem naturally branches, such as traversing a tree or generating combinations.
   - A depth-first tree traversal can be recursive, but it can also use an explicit `Deque` when the tree might be very deep.

## Linear Data Structures and Hashing

7. **What is a stack? Give common uses.**
   - A stack follows last-in, first-out (LIFO): `push` adds to the top and `pop` removes from the top. Both are normally `O(1)`.
   - Uses include undo operations, checking balanced parentheses, evaluating expressions, and implementing iterative depth-first search. In Java, prefer `ArrayDeque` over the legacy `Stack` class.

8. **What is a queue? Give common uses.**
   - A queue follows first-in, first-out (FIFO): enqueue at one end and dequeue at the other, normally in `O(1)` time.
   - Breadth-first search, print jobs, request handling, and task scheduling use queues. `ArrayDeque` is a useful general-purpose Java implementation.

9. **What is a hash table?**
   - A hash table uses a hash function to map a key to a bucket or index. Lookup, insert, and delete are `O(1)` on average.
   - Collisions occur when different keys map to the same bucket. They are handled by chaining or probing. In a poor or adversarial case, operations can degrade toward `O(n)`, so good hashing and resizing matter.

10. **Why must `equals` and `hashCode` agree in Java?**
   - If two objects are equal according to `equals`, they must return the same `hashCode`. Otherwise a `HashMap` or `HashSet` can place an object in a different bucket and fail to find an equal key.
   - Mutable map keys are also dangerous: changing fields used by `equals` or `hashCode` after insertion can make the entry effectively unreachable.

11. **How can you find duplicate values in an array?**
   - Scan the array and add each value to a `HashSet`. If `add` reports that the value was already present, it is a duplicate. This is `O(n)` expected time and `O(n)` extra space.
   - If modifying the input is allowed, sort first and compare adjacent values. That uses `O(n log n)` time but may use less extra space depending on the sort.

12. **What is the two-pointer technique?**
   - It keeps two indices that move through a sequence, often from opposite ends or at different speeds. It can replace a nested loop with one pass in the right problem.
   - In a sorted array, start one pointer at each end to find whether two values sum to a target. Move the left pointer when the sum is too small and the right pointer when it is too large. This is `O(n)` after sorting.

13. **What is the sliding-window technique?**
   - A sliding window keeps a contiguous range `[left, right]` and moves its boundaries while maintaining useful state, such as a sum or a set of characters.
   - To find the longest substring without repeated characters, expand `right`; if a duplicate appears, move `left` until the window is valid again. Each character enters and leaves at most once, so the approach is `O(n)`.

## Trees, Heaps, and Searching

14. **What is a binary tree? What is a binary search tree (BST)?**
   - A binary tree gives each node at most two children. A BST additionally keeps smaller keys in the left subtree and larger keys in the right subtree.
   - Searching a balanced BST is `O(log n)`, but a BST that receives sorted input without balancing can become a chain and take `O(n)`. Self-balancing trees avoid this worst shape.

15. **Compare preorder, inorder, postorder, and level-order traversal.**
   - Preorder visits node, left, right. Inorder visits left, node, right; for a BST this yields sorted keys. Postorder visits left, right, node, which is useful when children must be handled before a parent. Level order visits breadth by breadth using a queue.

16. **What is a heap?**
   - A heap is a complete binary tree with an ordering rule. In a min-heap, every parent is no larger than its children, so the smallest element is at the root; a max-heap keeps the largest at the root.
   - Peeking at the root is `O(1)`. Inserting and removing the root are `O(log n)`. Java's `PriorityQueue` is a min-heap by default and is useful for repeatedly selecting the next smallest item.

17. **How do you find the `k` largest elements efficiently?**
   - Keep a min-heap of at most size `k`. For each value, add it; if the heap grows past `k`, remove its smallest value. The heap finally contains the `k` largest values.
   - This takes `O(n log k)` time and `O(k)` space, which is better than sorting all values when `k` is much smaller than `n`.

18. **How does binary search work, and what is its precondition?**
   - Binary search needs sorted data. Compare the target with the middle element, then discard the half that cannot contain the target. The remaining range halves each iteration, giving `O(log n)` time.
   - Use `mid = low + (high - low) / 2` rather than `(low + high) / 2` to avoid integer overflow in languages where it matters.

19. **What are the usual binary-search boundary mistakes?**
   - Be consistent about whether the search interval is inclusive (`low <= high`) or half-open (`low < high`). Update the bounds so the middle index is excluded after it is checked, otherwise the loop may never finish.
   - For duplicate values, clarify whether the required answer is any occurrence, the first occurrence, or the last occurrence; the boundary update differs.

## Sorting and Graphs

20. **Compare bubble sort, insertion sort, merge sort, and quicksort.**
   - Bubble sort repeatedly swaps adjacent out-of-order elements and is `O(n^2)`. Insertion sort is also `O(n^2)` in general but is simple and good for small or nearly sorted input.
   - Merge sort is reliably `O(n log n)` but generally needs `O(n)` extra memory. Quicksort averages `O(n log n)` and is often fast in practice, but a poor pivot choice can give `O(n^2)`.

21. **Why is merge sort useful for linked lists?**
   - A linked list does not provide fast random access, making many array-oriented sorts awkward. Merge sort only needs sequential traversal and can split a list with slow and fast pointers.
   - Merging two sorted linked lists can be done by relinking nodes in `O(n)` time, so the total remains `O(n log n)`.

22. **What is a graph? What are adjacency lists and adjacency matrices?**
   - A graph has vertices (nodes) and edges (connections). It can be directed or undirected, weighted or unweighted.
   - An adjacency list stores each vertex's neighbors. It uses `O(V + E)` space and is best for sparse graphs. An adjacency matrix uses `O(V^2)` space but tests whether a specific edge exists in `O(1)` time, which can suit dense graphs.

23. **Compare BFS and DFS.**
   - Breadth-first search (BFS) explores level by level using a queue. In an unweighted graph, it finds the shortest path measured by number of edges. Depth-first search (DFS) explores one path deeply using recursion or a stack.
   - Both run in `O(V + E)` time with an adjacency list. DFS is useful for connected components, cycle detection, and topological-sort reasoning; BFS is useful for minimum-hop paths.

24. **How do you detect a cycle in an undirected graph?**
   - Run DFS and remember the parent of each node. If DFS reaches a visited neighbor that is not the current node's parent, a cycle exists.
   - The parent check matters because an undirected edge naturally appears in both adjacency lists; returning to the parent is not itself a cycle.

25. **What is topological sorting?**
   - It orders the vertices of a directed acyclic graph (DAG) so every edge `u -> v` places `u` before `v`. Course prerequisites and build dependencies are common examples.
   - Kahn's algorithm repeatedly removes vertices with indegree zero. If vertices remain but none has indegree zero, the graph has a cycle and no topological order exists.

## Problem-Solving Techniques

26. **What is dynamic programming (DP)?**
   - DP solves problems with overlapping subproblems and optimal substructure by saving smaller results instead of recomputing them.
   - Fibonacci is a simple example: plain recursion recomputes the same values, while an array or two variables store previous answers. DP can be top-down (memoization) or bottom-up (tabulation).

27. **How do you recognize a DP problem?**
   - Look for a problem that asks for a best value, count, or yes/no answer and can be divided into repeated smaller states. Define what each state means, write its transition, choose base cases, then decide the evaluation order.
   - For “minimum coins to make amount `x`,” a state can be `dp[x]`: the fewest coins needed for amount `x`. Try every permitted coin to transition from smaller amounts.

28. **What is greedy strategy, and when can it fail?**
   - A greedy algorithm makes the best-looking local choice at each step and does not revisit it. It is correct only when the problem has a proof that local choices lead to a global optimum.
   - Choosing the largest coin first works for some coin systems but not all: with coins `1, 3, 4`, target `6`, greedy picks `4 + 1 + 1` (three coins), but `3 + 3` uses two. DP handles the general version.

29. **What is backtracking?**
   - Backtracking builds a candidate solution step by step. When a partial choice cannot lead to a valid answer, it undoes that choice and tries another.
   - N-Queens, generating permutations, and Sudoku use backtracking. The search can be exponential, so pruning invalid partial states early is essential.

30. **How should you approach a coding problem in an interview?**
   - First restate the input, output, assumptions, and edge cases. Work through a small example. Start with a correct simple approach, then identify the repeated work or slow operation that a data structure can remove.
   - Before coding, state the chosen approach and its complexity. While coding, use clear variable names and handle empty input, one element, duplicates, and boundary indices. Finally, trace the code on the example.

## Common Coding Prompts

The snippets below use `java.util.*` where required. In an interview, first explain the approach and complexity, then write the smallest clear version of the code.

### 1. Reverse an array in place using two pointers

Move the left and right pointers inward, swapping their values. Time: `O(n)`; extra space: `O(1)`.

```java
static void reverse(int[] values) {
    int left = 0;
    int right = values.length - 1;

    while (left < right) {
        int temp = values[left];
        values[left] = values[right];
        values[right] = temp;
        left++;
        right--;
    }
}
```

### 2. Check whether parentheses are balanced using a stack

Push opening brackets. A closing bracket must match the most recent opening bracket. Time: `O(n)`; extra space: `O(n)` in the worst case.

```java
static boolean isBalanced(String text) {
    Deque<Character> stack = new ArrayDeque<>();

    for (char ch : text.toCharArray()) {
        if (ch == '(' || ch == '[' || ch == '{') {
            stack.push(ch);
        } else if (ch == ')' || ch == ']' || ch == '}') {
            if (stack.isEmpty()) {
                return false;
            }

            char open = stack.pop();
            if ((ch == ')' && open != '(')
                    || (ch == ']' && open != '[')
                    || (ch == '}' && open != '{')) {
                return false;
            }
        }
    }

    return stack.isEmpty();
}
```

### 3. Find the first non-repeated character

Count every character first, then scan the string again in its original order. Time: `O(n)`; extra space: `O(n)`.

```java
static Character firstNonRepeatedCharacter(String text) {
    Map<Character, Integer> frequency = new HashMap<>();

    for (char ch : text.toCharArray()) {
        frequency.put(ch, frequency.getOrDefault(ch, 0) + 1);
    }

    for (char ch : text.toCharArray()) {
        if (frequency.get(ch) == 1) {
            return ch;
        }
    }

    return null;
}
```

### 4. Merge two sorted arrays

Compare the current unmerged values and take the smaller one. Time: `O(m + n)`; extra space: `O(m + n)` for the result.

```java
static int[] mergeSortedArrays(int[] first, int[] second) {
    int[] merged = new int[first.length + second.length];
    int i = 0;
    int j = 0;
    int k = 0;

    while (i < first.length && j < second.length) {
        if (first[i] <= second[j]) {
            merged[k++] = first[i++];
        } else {
            merged[k++] = second[j++];
        }
    }

    while (i < first.length) {
        merged[k++] = first[i++];
    }
    while (j < second.length) {
        merged[k++] = second[j++];
    }

    return merged;
}
```

### 5. Find the middle node of a linked list

The fast pointer moves two nodes for every one node moved by the slow pointer. When fast reaches the end, slow is at the middle. Time: `O(n)`; extra space: `O(1)`.

```java
static class ListNode {
    int value;
    ListNode next;

    ListNode(int value) {
        this.value = value;
    }
}

static ListNode findMiddle(ListNode head) {
    ListNode slow = head;
    ListNode fast = head;

    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
    }

    return slow; // for an even-length list, returns the second middle node
}
```

### 6. Find two values in a sorted array whose sum equals a target

Use one pointer at each end. The sorted order tells us which pointer to move. Time: `O(n)`; extra space: `O(1)`.

```java
static boolean hasPairWithSum(int[] values, int target) {
    int left = 0;
    int right = values.length - 1;

    while (left < right) {
        int sum = values[left] + values[right];
        if (sum == target) {
            return true;
        }
        if (sum < target) {
            left++;
        } else {
            right--;
        }
    }

    return false;
}
```

### 7. Traverse a binary tree in level order

Use a queue: remove one level's nodes, record their values, then add their children. Time: `O(n)`; extra space: `O(n)` in the widest level.

```java
static class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;

    TreeNode(int value) {
        this.value = value;
    }
}

static List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) {
        return result;
    }

    Queue<TreeNode> queue = new ArrayDeque<>();
    queue.add(root);

    while (!queue.isEmpty()) {
        int levelSize = queue.size();
        List<Integer> level = new ArrayList<>();

        for (int i = 0; i < levelSize; i++) {
            TreeNode node = queue.remove();
            level.add(node.value);

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }

        result.add(level);
    }

    return result;
}
```

### 8. Find the shortest path in an unweighted grid using BFS

Each move has the same cost, so BFS reaches a cell using the fewest moves first. This example treats `0` as open and `1` as blocked. Time: `O(rows * columns)`; extra space: `O(rows * columns)`.

```java
static int shortestPath(int[][] grid, int startRow, int startCol,
                        int endRow, int endCol) {
    int rows = grid.length;
    int columns = grid[0].length;
    boolean[][] visited = new boolean[rows][columns];
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    Queue<int[]> queue = new ArrayDeque<>();

    queue.add(new int[]{startRow, startCol, 0});
    visited[startRow][startCol] = true;

    while (!queue.isEmpty()) {
        int[] current = queue.remove();
        int row = current[0];
        int column = current[1];
        int distance = current[2];

        if (row == endRow && column == endCol) {
            return distance;
        }

        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextColumn = column + direction[1];
            boolean isInsideGrid = nextRow >= 0 && nextRow < rows
                    && nextColumn >= 0 && nextColumn < columns;

            if (isInsideGrid && grid[nextRow][nextColumn] == 0
                    && !visited[nextRow][nextColumn]) {
                visited[nextRow][nextColumn] = true;
                queue.add(new int[]{nextRow, nextColumn, distance + 1});
            }
        }
    }

    return -1; // destination cannot be reached
}
```

### 9. Determine whether two strings are anagrams

After converting to the same case, matching strings must have identical character counts. Time: `O(n)`; extra space: `O(n)`.

```java
static boolean areAnagrams(String first, String second) {
    if (first.length() != second.length()) {
        return false;
    }

    Map<Character, Integer> counts = new HashMap<>();
    for (char ch : first.toLowerCase().toCharArray()) {
        counts.put(ch, counts.getOrDefault(ch, 0) + 1);
    }

    for (char ch : second.toLowerCase().toCharArray()) {
        int count = counts.getOrDefault(ch, 0);
        if (count == 0) {
            return false;
        }
        counts.put(ch, count - 1);
    }

    return true;
}
```

### 10. Find the maximum subarray sum (Kadane's algorithm)

At each value, choose whether to extend the previous subarray or start a new one. `currentSum` is the best sum ending at the current index. Time: `O(n)`; extra space: `O(1)`.

```java
static int maximumSubarraySum(int[] values) {
    if (values.length == 0) {
        throw new IllegalArgumentException("Array must not be empty");
    }

    int currentSum = values[0];
    int bestSum = values[0];

    for (int i = 1; i < values.length; i++) {
        currentSum = Math.max(values[i], currentSum + values[i]);
        bestSum = Math.max(bestSum, currentSum);
    }

    return bestSum;
}
```
