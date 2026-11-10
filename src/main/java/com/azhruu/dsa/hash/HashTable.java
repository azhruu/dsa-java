package com.azhruu.dsa.hash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hash table with separate chaining.
 *
 * <p>Each bucket holds a singly linked chain of entries, so keys that hash
 * to the same bucket simply share it instead of overwriting each other.
 * When the load factor passes 0.75 the table doubles in size and every
 * entry is rehashed into its new bucket.
 *
 * <p>Costs: put, get and remove are O(1) on average. They degrade to
 * O(n) only when many keys collide into one bucket, which a good
 * {@code hashCode()} avoids. Null keys and null values are rejected.
 */
public class HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_LOAD_FACTOR = 0.75;

    /** One link in a bucket chain. */
    private static final class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] buckets;
    private int size;

    /** Empty table with the default capacity of 16 buckets. */
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    /** Empty table with the given starting number of buckets. */
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("capacity must be at least 1");
        this.buckets = new Node[initialCapacity];
        this.size = 0;
    }

    /**
     * Store a key-value pair. If the key is already present its value is
     * replaced and the old value is returned; otherwise the pair is added
     * (at the head of its bucket chain) and null is returned.
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key, "hash key must not be null");
        Objects.requireNonNull(value, "hash value must not be null");
        int index = bucketIndex(key);
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                V old = node.value;
                node.value = value;
                return old;
            }
        }
        // New key: grow first if the table is getting full, then insert.
        if (size + 1 > buckets.length * MAX_LOAD_FACTOR) {
            rehash();
            index = bucketIndex(key);
        }
        buckets[index] = new Node<>(key, value, buckets[index]);
        size++;
        return null;
    }

    /** Return the value for a key, or null when the key is absent. O(1) average. */
    public V get(K key) {
        Objects.requireNonNull(key, "hash key must not be null");
        for (Node<K, V> node = buckets[bucketIndex(key)]; node != null; node = node.next) {
            if (node.key.equals(key)) return node.value;
        }
        return null;
    }

    /** True when the table holds this key. O(1) average. */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /** True when any entry holds this value (scans every bucket). O(n). */
    public boolean containsValue(V value) {
        Objects.requireNonNull(value, "hash value must not be null");
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                if (node.value.equals(value)) return true;
            }
        }
        return false;
    }

    /**
     * Remove a key and return its value, or null when the key is absent.
     * O(1) average.
     */
    public V remove(K key) {
        Objects.requireNonNull(key, "hash key must not be null");
        int index = bucketIndex(key);
        Node<K, V> prev = null;
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                if (prev == null) buckets[index] = node.next;
                else prev.next = node.next;
                size--;
                return node.value;
            }
            prev = node;
        }
        return null;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Current number of buckets (doubles on every rehash). */
    public int capacity() { return buckets.length; }

    /** Entries per bucket, on average - rehashing keeps it under 0.75. */
    public double loadFactor() {
        return (double) size / buckets.length;
    }

    /** Length of the chain in one bucket - a quick collision check. O(chain). */
    public int chainLength(int bucket) {
        if (bucket < 0 || bucket >= buckets.length)
            throw new IndexOutOfBoundsException("bucket " + bucket + " of " + buckets.length);
        int length = 0;
        for (Node<K, V> node = buckets[bucket]; node != null; node = node.next) length++;
        return length;
    }

    /** All keys, in bucket order. O(n). */
    public List<K> keys() {
        List<K> out = new ArrayList<>(size);
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                out.add(node.key);
            }
        }
        return out;
    }

    // -- internals -------------------------------------------------------

    /** Bucket for a key: the hash code folded into the table size. */
    private int bucketIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % buckets.length;
    }

    /** Double the buckets and rehash every entry into its new home. O(n). */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Node<K, V>[] old = buckets;
        buckets = new Node[old.length * 2];
        for (Node<K, V> head : old) {
            for (Node<K, V> node = head; node != null; ) {
                Node<K, V> next = node.next;
                int index = bucketIndex(node.key);
                node.next = buckets[index];
                buckets[index] = node;
                node = next;
            }
        }
    }

    /** One line per non-empty bucket, so chains (collisions) are visible. */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("{");
        boolean firstBucket = true;
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] == null) continue;
            if (!firstBucket) out.append(", ");
            firstBucket = false;
            out.append(i).append(":[");
            boolean first = true;
            for (Node<K, V> node = buckets[i]; node != null; node = node.next) {
                if (!first) out.append(" -> ");
                first = false;
                out.append(node.key).append("=").append(node.value);
            }
            out.append("]");
        }
        return out.append("}").toString();
    }
}
