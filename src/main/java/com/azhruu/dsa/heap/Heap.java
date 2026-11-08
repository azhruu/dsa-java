package com.azhruu.dsa.heap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Binary heap backed by a resizable array.
 *
 * <p>One class covers both flavours: a max-heap keeps the largest element
 * at the root, a min-heap the smallest. Use the factories
 * {@link #maxHeap()} / {@link #minHeap()}, or build a heap from an
 * existing array in linear time with {@link #heapifyMax} /
 * {@link #heapifyMin}.
 *
 * <p>Costs: insert and extract are O(log n), peek is O(1), heapify is
 * O(n). The array grows by doubling, so inserting n elements one by one
 * is O(n log n) in total.
 */
public class Heap<T extends Comparable<T>> {

    private static final int DEFAULT_CAPACITY = 16;

    private final boolean max;
    private Object[] data;
    private int size;

    private Heap(boolean max) {
        this.max = max;
        this.data = new Object[DEFAULT_CAPACITY];
    }

    /** Empty max-heap: extract() returns the largest element first. */
    public static <T extends Comparable<T>> Heap<T> maxHeap() {
        return new Heap<>(true);
    }

    /** Empty min-heap: extract() returns the smallest element first. */
    public static <T extends Comparable<T>> Heap<T> minHeap() {
        return new Heap<>(false);
    }

    /**
     * Build a max-heap from the given items in O(n) (bottom-up heapify).
     * The input array is copied, never modified.
     */
    public static <T extends Comparable<T>> Heap<T> heapifyMax(T[] items) {
        return heapify(items, true);
    }

    /**
     * Build a min-heap from the given items in O(n) (bottom-up heapify).
     * The input array is copied, never modified.
     */
    public static <T extends Comparable<T>> Heap<T> heapifyMin(T[] items) {
        return heapify(items, false);
    }

    private static <T extends Comparable<T>> Heap<T> heapify(T[] items, boolean max) {
        Objects.requireNonNull(items, "items must not be null");
        Heap<T> heap = new Heap<>(max);
        heap.data = new Object[Math.max(DEFAULT_CAPACITY, items.length)];
        for (int i = 0; i < items.length; i++) {
            heap.data[i] = Objects.requireNonNull(items[i], "heap items must not be null");
        }
        heap.size = items.length;
        // Sift down every parent, bottom-up: leaves already satisfy the
        // heap property, so this is O(n), not O(n log n).
        for (int i = heap.parent(heap.size - 1); i >= 0; i--) {
            heap.siftDown(i);
        }
        return heap;
    }

    /** Insert a value, restoring the heap property by sifting up. O(log n). */
    public void insert(T value) {
        Objects.requireNonNull(value, "heap value must not be null");
        ensureCapacity();
        data[size] = value;
        size++;
        siftUp(size - 1);
    }

    /** Remove and return the root (max in a max-heap, min in a min-heap). O(log n). */
    public T extract() {
        if (isEmpty()) throw new IllegalStateException("heap is empty");
        T root = elementAt(0);
        size--;
        if (size > 0) {
            data[0] = data[size];
            data[size] = null;
            siftDown(0);
        } else {
            data[0] = null;
        }
        return root;
    }

    /** Peek at the root without removing it. O(1). */
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("heap is empty");
        return elementAt(0);
    }

    /** True if this is a max-heap, false for a min-heap. */
    public boolean isMaxHeap() { return max; }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Check the heap property holds at every parent. O(n). */
    public boolean isValid() {
        for (int i = 0; i < size; i++) {
            int left = left(i), right = right(i);
            if (left < size && better(elementAt(left), elementAt(i))) return false;
            if (right < size && better(elementAt(right), elementAt(i))) return false;
        }
        return true;
    }

    /** The heap contents in array (level-order) layout. */
    public List<T> toList() {
        List<T> out = new ArrayList<>(size);
        for (int i = 0; i < size; i++) out.add(elementAt(i));
        return out;
    }

    // -- internals -------------------------------------------------------

    /** True when a should sit above b (larger for max, smaller for min). */
    private boolean better(T a, T b) {
        int cmp = a.compareTo(b);
        return max ? cmp > 0 : cmp < 0;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int p = parent(i);
            if (!better(elementAt(i), elementAt(p))) break;
            swap(i, p);
            i = p;
        }
    }

    private void siftDown(int i) {
        while (true) {
            int left = left(i), right = right(i);
            int best = i;
            if (left < size && better(elementAt(left), elementAt(best))) best = left;
            if (right < size && better(elementAt(right), elementAt(best))) best = right;
            if (best == i) break;
            swap(i, best);
            i = best;
        }
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i) { return 2 * i + 1; }
    private int right(int i) { return 2 * i + 2; }

    @SuppressWarnings("unchecked")
    private T elementAt(int i) { return (T) data[i]; }

    private void swap(int i, int j) {
        Object tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            Object[] grown = new Object[data.length * 2];
            System.arraycopy(data, 0, grown, 0, size);
            data = grown;
        }
    }

    @Override
    public String toString() {
        return toList().toString();
    }
}
