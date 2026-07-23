package lc.medium;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * LeetCode 692 - Top K Frequent Words
 *
 * Problem statement:
 * Given an array of words and an integer k, return the k most frequent words.
 * Sort the result by descending frequency; words with the same frequency are
 * ordered lexicographically (ascending).
 *
 * Examples:
 * - Input: ["i","love","leetcode","i","love","coding"], k = 2;
 *   Output: ["i","love"] (both appear twice; "i" precedes "love").
 * - Input: ["the","day","is","sunny","the","the","the","sunny","is","is"], k=4;
 *   Output: ["the","is","sunny","day"].
 *
 * Algorithm (bounded min-heap):
 * 1. Count how often each word occurs.
 * 2. Push words into a min-heap ordered so the weakest candidate sits on top:
 *    lower frequency is weaker, and on ties the lexicographically larger word
 *    is weaker.
 * 3. Whenever the heap grows past k, drop the top - that word cannot be in the
 *    answer.
 * 4. Pop the heap into a list and reverse it, giving strongest first.
 *
 * Time complexity: O(n + m log k), where m is the number of distinct words.
 * Space complexity: O(m).
 */
public class TopKFrequentWords {

    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> counts = new HashMap<>();
        for (String word : words) {
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }

        PriorityQueue<String> heap = new PriorityQueue<>(new WeakestFirst(counts));
        for (String word : counts.keySet()) {
            heap.offer(word);
            if (heap.size() > k) {
                heap.poll();
            }
        }

        LinkedList<String> result = new LinkedList<>();
        while (!heap.isEmpty()) {
            result.addFirst(heap.poll());
        }
        return result;
    }

    /**
     * Orders the weakest candidate first so the heap can evict it: a lower
     * frequency is weaker, and on a frequency tie the lexicographically larger
     * word is treated as weaker.
     */
    private static class WeakestFirst implements Comparator<String> {

        private final Map<String, Integer> counts;

        WeakestFirst(Map<String, Integer> counts) {
            this.counts = counts;
        }

        @Override
        public int compare(String first, String second) {
            int firstCount = counts.get(first);
            int secondCount = counts.get(second);
            if (firstCount != secondCount) {
                return firstCount - secondCount;
            }
            return second.compareTo(first);
        }
    }
}

/*
 * Discussion:
 * The direct approach counts every word and fully sorts the distinct words by
 * the frequency-then-alphabetical rule, which is O(m log m). When k is much
 * smaller than the number of distinct words, that sorts far more than needed.
 *
 * A min-heap capped at size k keeps only the current best k candidates: the
 * weakest sits on top and is evicted as stronger words arrive, so each of the m
 * words costs O(log k). The comparator must invert the tie-break - larger words
 * are treated as weaker - so that after popping and reversing, equal-frequency
 * words come out in ascending order. Bucket sort by frequency plus per-bucket
 * alphabetical ordering is an O(n log n)-free alternative worth mentioning.
 *
 * Follow-ups and edge cases: when k equals the number of distinct words the
 * answer is simply every word in sorted order, and the tie-break rule must be
 * stated precisely since it is the part most people get wrong. The integer
 * variant is Top K Frequent Elements (LeetCode 347). The strongest follow-up is
 * a streaming source too large to hold in memory: the bounded heap already helps
 * because it stores only k candidates, and an approximate count-min sketch can
 * replace the exact hash map. Distributed inputs push toward a map-reduce style
 * partial-top-k merge.
 */
