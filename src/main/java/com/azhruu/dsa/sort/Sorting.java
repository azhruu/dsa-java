package com.azhruu.dsa.sort;

import java.util.Objects;
import java.util.Random;

/**
 * Sorting utilities: merge sort, quick sort and heap sort behind one
 * API, plus a small benchmark helper that times each algorithm on the
 * same input so they can be compared honestly.
 *
 * <p>Merge sort is the default: it is stable (equal elements keep their
 * order) and always O(n log n), at the cost of O(n) extra space.
 * Quick sort is in place and usually the fastest of the three, with a
 * median-of-three pivot and an insertion-sort finish so sorted input
 * does not degrade it to O(n^2). Heap sort delegates to
 * {@link HeapSort}: in place, O(n log n) in every case, not stable.
 *
 * <p>Comparison and swap counts for the most recent sort are available
 * through {@link #lastComparisons()} and {@link #lastSwaps()}. For
 * merge sort a "swap" is an element written back into the array, and
 * for quick sort's insertion-sort finish it is an element moved --
 * both are the honest cost of placing an element, just not a swap
 * between two positions.
 */
public final class Sorting {

    /** The algorithms this utility can run. */
    public enum Algorithm { MERGE, QUICK, HEAP }

    /**
     * One timed run: which algorithm ran, how long it took, and what
     * it cost. Produced by {@link #benchmark(Algorithm, Comparable[])}.
     */
    public static final class Timing {
        public final Algorithm algorithm;
        public final long nanos;
        public final long comparisons;
        public final long swaps;

        private Timing(Algorithm algorithm, long nanos, long comparisons, long swaps) {
            this.algorithm = algorithm;
            this.nanos = nanos;
            this.comparisons = comparisons;
            this.swaps = swaps;
        }

        /** Elapsed time in milliseconds, with fractions. */
        public double millis() { return nanos / 1_000_000.0; }

        @Override
        public String toString() {
            return String.format("%-5s %8.2f ms  (comparisons=%,d, swaps=%,d)",
                    algorithm, millis(), comparisons, swaps);
        }
    }

    private static long comparisons;
    private static long swaps;

    /** Partitions smaller than this are finished with insertion sort. */
    private static final int QUICK_CUTOFF = 16;

    private Sorting() { }

    /**
     * Sort the array in ascending order with the given algorithm,
     * in place. MERGE is stable; QUICK and HEAP are not.
     */
    public static <T extends Comparable<T>> void sort(T[] array, Algorithm algorithm) {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        switch (algorithm) {
            case MERGE: mergeSort(array); break;
            case QUICK: quickSort(array); break;
            case HEAP: heapSort(array); break;
            default: throw new IllegalArgumentException("unknown algorithm: " + algorithm);
        }
    }

    /** Sort the array in ascending order with the default algorithm (merge sort). */
    public static <T extends Comparable<T>> void sort(T[] array) {
        sort(array, Algorithm.MERGE);
    }

    /** Merge sort, ascending. Stable, O(n log n) time, O(n) extra space. */
    public static <T extends Comparable<T>> void mergeSort(T[] array) {
        mergeSort(array, true);
    }

    /** Merge sort, descending. Stable, O(n log n) time, O(n) extra space. */
    public static <T extends Comparable<T>> void mergeSortDescending(T[] array) {
        mergeSort(array, false);
    }

    private static <T extends Comparable<T>> void mergeSort(T[] array, boolean ascending) {
        Objects.requireNonNull(array, "array must not be null");
        checkElements(array);
        comparisons = 0;
        swaps = 0;
        if (array.length < 2) return;
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Comparable[array.length];
        mergeSort(array, aux, 0, array.length, ascending);
    }

    private static <T extends Comparable<T>> void mergeSort(
            T[] a, T[] aux, int lo, int hi, boolean ascending) {
        if (hi - lo < 2) return;
        int mid = (lo + hi) / 2;
        mergeSort(a, aux, lo, mid, ascending);
        mergeSort(a, aux, mid, hi, ascending);
        merge(a, aux, lo, mid, hi, ascending);
    }

    /** Merge the sorted halves a[lo..mid) and a[mid..hi) back into a. */
    private static <T extends Comparable<T>> void merge(
            T[] a, T[] aux, int lo, int mid, int hi, boolean ascending) {
        int i = lo, j = mid, k = lo;
        while (i < mid && j < hi) {
            comparisons++;
            // "<=" (not "<") is what keeps merge sort stable: on a tie
            // the left half's element goes first.
            if (inOrder(a[i], a[j], ascending)) aux[k++] = a[i++];
            else aux[k++] = a[j++];
        }
        while (i < mid) aux[k++] = a[i++];
        while (j < hi) aux[k++] = a[j++];
        for (int t = lo; t < hi; t++) {
            a[t] = aux[t];
            swaps++;
        }
    }

    /** True when a should come no later than b in the sort order. */
    private static <T extends Comparable<T>> boolean inOrder(T a, T b, boolean ascending) {
        int cmp = a.compareTo(b);
        return ascending ? cmp <= 0 : cmp >= 0;
    }

    /** Quick sort, ascending. O(n log n) average, O(n) worst with a median-of-three pivot. */
    public static <T extends Comparable<T>> void quickSort(T[] array) {
        quickSort(array, true);
    }

    /** Quick sort, descending. O(n log n) average, O(n) worst with a median-of-three pivot. */
    public static <T extends Comparable<T>> void quickSortDescending(T[] array) {
        quickSort(array, false);
    }

    private static <T extends Comparable<T>> void quickSort(T[] array, boolean ascending) {
        Objects.requireNonNull(array, "array must not be null");
        checkElements(array);
        comparisons = 0;
        swaps = 0;
        if (array.length > 1) {
            quickSort(array, 0, array.length - 1, ascending);
        }
    }

    private static <T extends Comparable<T>> void quickSort(
            T[] a, int lo, int hi, boolean ascending) {
        // Recurse on the smaller side and loop on the larger one, so the
        // recursion depth stays O(log n) even on adversarial input.
        while (hi - lo + 1 > QUICK_CUTOFF) {
            int pivotIndex = partition(a, lo, hi, ascending);
            if (pivotIndex - lo < hi - pivotIndex) {
                quickSort(a, lo, pivotIndex - 1, ascending);
                lo = pivotIndex + 1;
            } else {
                quickSort(a, pivotIndex + 1, hi, ascending);
                hi = pivotIndex - 1;
            }
        }
        insertionSort(a, lo, hi, ascending);
    }

    /** Lomuto partition around a median-of-three pivot. Returns the pivot's final index. */
    private static <T extends Comparable<T>> int partition(
            T[] a, int lo, int hi, boolean ascending) {
        int mid = lo + (hi - lo) / 2;
        int pivotIndex = medianOfThree(a, lo, mid, hi, ascending);
        swap(a, pivotIndex, hi);
        T pivot = a[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            comparisons++;
            if (before(a[j], pivot, ascending)) {
                swap(a, i, j);
                i++;
            }
        }
        swap(a, i, hi);
        return i;
    }

    /** True when a should come strictly before b in the sort order. */
    private static <T extends Comparable<T>> boolean before(T a, T b, boolean ascending) {
        int cmp = a.compareTo(b);
        return ascending ? cmp < 0 : cmp > 0;
    }

    /** Index of the median of a[lo], a[mid], a[hi] under the sort order. */
    private static <T extends Comparable<T>> int medianOfThree(
            T[] a, int lo, int mid, int hi, boolean ascending) {
        if (compareInOrder(a[lo], a[mid], ascending) < 0) {
            if (compareInOrder(a[mid], a[hi], ascending) < 0) return mid;
            if (compareInOrder(a[lo], a[hi], ascending) < 0) return hi;
            return lo;
        }
        if (compareInOrder(a[lo], a[hi], ascending) < 0) return lo;
        if (compareInOrder(a[mid], a[hi], ascending) < 0) return hi;
        return mid;
    }

    /** Sign of the comparison between x and y in the sort order; counts as one comparison. */
    private static <T extends Comparable<T>> int compareInOrder(T x, T y, boolean ascending) {
        comparisons++;
        int cmp = x.compareTo(y);
        return ascending ? cmp : -cmp;
    }

    /** Insertion sort over a[lo..hi] -- fast on the small partitions quick sort leaves behind. */
    private static <T extends Comparable<T>> void insertionSort(
            T[] a, int lo, int hi, boolean ascending) {
        for (int i = lo + 1; i <= hi; i++) {
            T key = a[i];
            int j = i - 1;
            // Shift right while a[j] belongs after the key; stop as soon
            // as a[j] is already in place (equal keys stop too, which
            // keeps the sort stable within this small range).
            while (j >= lo) {
                comparisons++;
                if (inOrder(a[j], key, ascending)) break;
                a[j + 1] = a[j];
                swaps++;
                j--;
            }
            a[j + 1] = key;
            swaps++;
        }
    }

    /** Heap sort, ascending, via {@link HeapSort}. O(n log n) in every case, in place. */
    public static <T extends Comparable<T>> void heapSort(T[] array) {
        Objects.requireNonNull(array, "array must not be null");
        HeapSort.sort(array);
        comparisons = HeapSort.lastComparisons();
        swaps = HeapSort.lastSwaps();
    }

    /** Heap sort, descending, via {@link HeapSort}. O(n log n) in every case, in place. */
    public static <T extends Comparable<T>> void heapSortDescending(T[] array) {
        Objects.requireNonNull(array, "array must not be null");
        HeapSort.sortDescending(array);
        comparisons = HeapSort.lastComparisons();
        swaps = HeapSort.lastSwaps();
    }

    /**
     * Return a new array with the elements sorted in ascending order
     * by the given algorithm. The input array is copied, never modified.
     */
    public static <T extends Comparable<T>> T[] sortedCopy(T[] array, Algorithm algorithm) {
        Objects.requireNonNull(array, "array must not be null");
        T[] copy = array.clone();
        sort(copy, algorithm);
        return copy;
    }

    /** Return a new array with the elements sorted in ascending order (merge sort). */
    public static <T extends Comparable<T>> T[] sortedCopy(T[] array) {
        return sortedCopy(array, Algorithm.MERGE);
    }

    /** True if the array is sorted in ascending order. O(n). */
    public static <T extends Comparable<T>> boolean isSorted(T[] array) {
        Objects.requireNonNull(array, "array must not be null");
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1].compareTo(array[i]) > 0) return false;
        }
        return true;
    }

    /** True if the array is sorted in descending order. O(n). */
    public static <T extends Comparable<T>> boolean isSortedDescending(T[] array) {
        Objects.requireNonNull(array, "array must not be null");
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1].compareTo(array[i]) < 0) return false;
        }
        return true;
    }

    /**
     * Return a shuffled copy of the array (Fisher-Yates, seeded so the
     * same seed always gives the same shuffle -- handy for repeatable
     * benchmarks). The input array is untouched.
     */
    public static <T> T[] shuffledCopy(T[] array, long seed) {
        Objects.requireNonNull(array, "array must not be null");
        T[] copy = array.clone();
        Random random = new Random(seed);
        for (int i = copy.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T tmp = copy[i];
            copy[i] = copy[j];
            copy[j] = tmp;
        }
        return copy;
    }

    /**
     * Sort a copy of the array with the given algorithm and time it.
     * The input array is untouched; the returned {@link Timing} carries
     * the elapsed time and the comparison / swap counts of that run.
     */
    public static <T extends Comparable<T>> Timing benchmark(Algorithm algorithm, T[] array) {
        Objects.requireNonNull(array, "array must not be null");
        T[] copy = array.clone();
        long start = System.nanoTime();
        sort(copy, algorithm);
        long nanos = System.nanoTime() - start;
        return new Timing(algorithm, nanos, comparisons, swaps);
    }

    /** Comparisons made by the most recent sort() / benchmark() call. */
    public static long lastComparisons() { return comparisons; }

    /** Swaps (or element moves, see class javadoc) made by the most recent sort() / benchmark() call. */
    public static long lastSwaps() { return swaps; }

    // -- internals -------------------------------------------------------

    private static <T> void swap(T[] a, int i, int j) {
        if (i == j) return;
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
