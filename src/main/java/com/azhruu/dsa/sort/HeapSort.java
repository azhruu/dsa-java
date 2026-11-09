package com.azhruu.dsa.sort;

import java.util.Objects;

/**
 * Heap sort: in-place sorting in O(n log n) time and O(1) extra space.
 *
 * <p>Builds a max-heap in linear time (bottom-up heapify), then repeatedly
 * moves the root (the current maximum) to the end of the unsorted region
 * and restores the heap. The same machinery with a min-heap sorts in
 * descending order. {@link #sortedCopy} leaves the input untouched.
 *
 * <p>Costs: O(n log n) in every case (best, average, worst) -- heap sort
 * never degrades the way quick sort can -- and O(1) extra space. It is
 * not stable: equal elements can change relative order. Comparison and
 * swap counts for the most recent sort are available through
 * {@link #lastComparisons()} and {@link #lastSwaps()}.
 */
public final class HeapSort {

    private static long comparisons;
    private static long swaps;

    private HeapSort() { }

    /** Sort the array in ascending order, in place. O(n log n), O(1) space. */
    public static <T extends Comparable<T>> void sort(T[] array) {
        sort(array, true);
    }

    /** Sort the array in descending order, in place. O(n log n), O(1) space. */
    public static <T extends Comparable<T>> void sortDescending(T[] array) {
        sort(array, false);
    }

    private static <T extends Comparable<T>> void sort(T[] array, boolean ascending) {
        Objects.requireNonNull(array, "array must not be null");
        checkElements(array);
        comparisons = 0;
        swaps = 0;
        int n = array.length;
        // Build the heap: sift down every parent, bottom-up (O(n)).
        for (int i = parent(n - 1); i >= 0; i--) {
            siftDown(array, i, n, ascending);
        }
        // Repeatedly take the root to the end and restore the heap.
        for (int end = n - 1; end > 0; end--) {
            swap(array, 0, end);
            siftDown(array, 0, end, ascending);
        }
    }

    /**
     * Return a new array with the elements sorted in ascending order.
     * The input array is copied, never modified.
     */
    public static <T extends Comparable<T>> T[] sortedCopy(T[] array) {
        Objects.requireNonNull(array, "array must not be null");
        T[] copy = array.clone();
        sort(copy);
        return copy;
    }

    /** True if the array is sorted in ascending order. O(n). */
    public static <T extends Comparable<T>> boolean isSorted(T[] array) {
        Objects.requireNonNull(array, "array must not be null");
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1].compareTo(array[i]) > 0) return false;
        }
        return true;
    }

    /** Comparisons made by the most recent sort() / sortDescending() / sortedCopy() call. */
    public static long lastComparisons() { return comparisons; }

    /** Swaps made by the most recent sort() / sortDescending() / sortedCopy() call. */
    public static long lastSwaps() { return swaps; }

    // -- internals -------------------------------------------------------

    /** Sift the element at i down until the heap property holds in a[0..end). */
    private static <T extends Comparable<T>> void siftDown(T[] a, int i, int end, boolean maxHeap) {
        while (true) {
            int left = 2 * i + 1, right = left + 1;
            int best = i;
            if (left < end && better(a[left], a[best], maxHeap)) best = left;
            if (right < end && better(a[right], a[best], maxHeap)) best = right;
            if (best == i) break;
            swap(a, i, best);
            i = best;
        }
    }

    /** True when a should sit above b (larger for a max-heap, smaller for a min-heap). */
    private static <T extends Comparable<T>> boolean better(T a, T b, boolean maxHeap) {
        comparisons++;
        int cmp = a.compareTo(b);
        return maxHeap ? cmp > 0 : cmp < 0;
    }

    private static int parent(int i) { return (i - 1) / 2; }

    private static <T> void swap(T[] a, int i, int j) {
        swaps++;
        T tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private static <T extends Comparable<T>> void checkElements(T[] array) {
        for (T element : array) {
            Objects.requireNonNull(element, "array elements must not be null");
        }
    }
}
