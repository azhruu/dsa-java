package com.azhruu.dsa.search;

import java.util.Objects;

/**
 * Binary search utilities: the classic search plus the variants that
 * turn up in real code and interviews -- first / last occurrence in
 * an array with duplicates, lower / upper bounds, and searching a
 * rotated sorted array (including finding its minimum).
 *
 * <p>Every method assumes the array is sorted ascending, except
 * {@link #searchRotated(int[], int)} and {@link #findMinRotated(int[], int)},
 * which assume a sorted array rotated at some pivot. Passing an
 * unsorted array gives an unspecified (wrong) answer, never an
 * exception -- the same contract as {@link java.util.Arrays#binarySearch}.
 * A method that finds nothing returns -1, except the bounds, which
 * return an insertion point.
 *
 * <p>All searches are iterative, O(log n) time and O(1) extra space.
 * The one exception is a rotated array full of duplicates: when the
 * ends and the middle are all equal the pivot cannot be located, so
 * the search shrinks both ends and degrades towards O(n). The
 * comparison count of the most recent call is available through
 * {@link #lastComparisons()}.
 */
public final class BinarySearch {

    private static long comparisons;

    private BinarySearch() { }

    /**
     * Binary search on a sorted array. Returns the index of the target,
     * or -1 when it is absent. With duplicates, any matching index may
     * be returned -- use {@link #firstOccurrence(int[], int)} or
     * {@link #lastOccurrence(int[], int)} when the position matters.
     */
    public static int binarySearch(int[] array, int target) {
        Objects.requireNonNull(array, "array must not be null");
        return binarySearch(array, target, 0, array.length);
    }

    /**
     * Binary search over array[fromIndex..toIndex). Returns the index
     * of the target, or -1 when it is absent from that range.
     */
    public static int binarySearch(int[] array, int target, int fromIndex, int toIndex) {
        Objects.requireNonNull(array, "array must not be null");
        checkRange(fromIndex, toIndex, array.length);
        comparisons = 0;
        int lo = fromIndex, hi = toIndex - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            if (array[mid] == target) return mid;
            if (array[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    /**
     * Binary search on a sorted array of Comparables. Returns the index
     * of the target, or -1 when it is absent.
     */
    public static <T extends Comparable<T>> int binarySearch(T[] array, T target) {
        Objects.requireNonNull(array, "array must not be null");
        return binarySearch(array, target, 0, array.length);
    }

    /**
     * Binary search over array[fromIndex..toIndex) of Comparables.
     * Returns the index of the target, or -1 when it is absent from
     * that range.
     */
    public static <T extends Comparable<T>> int binarySearch(
            T[] array, T target, int fromIndex, int toIndex) {
        Objects.requireNonNull(array, "array must not be null");
        Objects.requireNonNull(target, "target must not be null");
        checkRange(fromIndex, toIndex, array.length);
        checkElements(array, fromIndex, toIndex);
        comparisons = 0;
        int lo = fromIndex, hi = toIndex - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            int cmp = array[mid].compareTo(target);
            if (cmp == 0) return mid;
            if (cmp < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    /**
     * First index holding the target in a sorted array with duplicates,
     * or -1 when it is absent. The array stays sorted; equal elements
     * are what make "first" meaningful.
     */
    public static int firstOccurrence(int[] array, int target) {
        Objects.requireNonNull(array, "array must not be null");
        comparisons = 0;
        int lo = 0, hi = array.length - 1, answer = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            if (array[mid] == target) {
                answer = mid;
                hi = mid - 1; // keep looking left for an earlier copy
            } else if (array[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return answer;
    }

    /**
     * Last index holding the target in a sorted array with duplicates,
     * or -1 when it is absent.
     */
    public static int lastOccurrence(int[] array, int target) {
        Objects.requireNonNull(array, "array must not be null");
        comparisons = 0;
        int lo = 0, hi = array.length - 1, answer = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            if (array[mid] == target) {
                answer = mid;
                lo = mid + 1; // keep looking right for a later copy
            } else if (array[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return answer;
    }

    /**
     * First index whose element is greater than or equal to the target
     * -- the insertion point that keeps the array sorted. Returns
     * array.length when every element is smaller than the target.
     */
    public static int lowerBound(int[] array, int target) {
        Objects.requireNonNull(array, "array must not be null");
        comparisons = 0;
        int lo = 0, hi = array.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            if (array[mid] < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /**
     * First index whose element is strictly greater than the target --
     * the insertion point after any copies of the target. Returns
     * array.length when no element is greater than the target.
     */
    public static int upperBound(int[] array, int target) {
        Objects.requireNonNull(array, "array must not be null");
        comparisons = 0;
        int lo = 0, hi = array.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            if (array[mid] <= target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    /**
     * Search a rotated sorted array: a sorted array cut at some pivot
     * and rejoined, e.g. {4, 5, 6, 7, 0, 1, 2}. Returns the index of
     * the target, or -1 when it is absent. One half of the array is
     * always sorted, and the search keeps the half that can hold the
     * target. O(log n) without duplicates; with many duplicates it
     * degrades towards O(n) (see the class javadoc).
     */
    public static int searchRotated(int[] array, int target) {
        Objects.requireNonNull(array, "array must not be null");
        comparisons = 0;
        int lo = 0, hi = array.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            if (array[mid] == target) return mid;
            // Ends and middle all equal: the sorted half cannot be
            // identified, so shrink both ends and try again.
            if (array[lo] == array[mid] && array[mid] == array[hi]) {
                lo++;
                hi--;
                continue;
            }
            if (array[lo] <= array[mid]) {
                // Left half is sorted: target inside it goes left.
                if (array[lo] <= target && target < array[mid]) hi = mid - 1;
                else lo = mid + 1;
            } else {
                // Right half is sorted: target inside it goes right.
                if (array[mid] < target && target <= array[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return -1;
    }

    /**
     * The minimum value of a rotated sorted array -- the element at
     * the pivot. An unrotated array returns its first element. Throws
     * IllegalArgumentException on an empty array.
     */
    public static int findMinRotated(int[] array) {
        Objects.requireNonNull(array, "array must not be null");
        if (array.length == 0) {
            throw new IllegalArgumentException("array must not be empty");
        }
        comparisons = 0;
        int lo = 0, hi = array.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            if (array[mid] > array[hi]) {
                // Minimum is right of mid: mid cannot be it.
                lo = mid + 1;
            } else if (array[mid] < array[hi]) {
                // Minimum is at mid or left of it.
                hi = mid;
            } else {
                // array[mid] == array[hi]: mid might be the minimum,
                // but hi cannot be (a duplicate sits at mid), so drop hi.
                hi--;
            }
        }
        return array[lo];
    }

    /** Comparisons made by the most recent search call. */
    public static long lastComparisons() { return comparisons; }

    // -- internals -------------------------------------------------------

    private static void checkRange(int fromIndex, int toIndex, int length) {
        if (fromIndex < 0 || toIndex > length || fromIndex > toIndex) {
            throw new IllegalArgumentException(
                    "range [" + fromIndex + ", " + toIndex + ") is invalid for length " + length);
        }
    }

    private static <T> void checkElements(T[] array, int fromIndex, int toIndex) {
        for (int i = fromIndex; i < toIndex; i++) {
            Objects.requireNonNull(array[i], "array elements must not be null");
        }
    }
}
