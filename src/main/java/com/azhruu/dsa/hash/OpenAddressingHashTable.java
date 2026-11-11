package com.azhruu.dsa.hash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hash table with open addressing (linear probing).
 *
 * <p>Unlike {@link HashTable}, which chains colliding keys into linked
 * lists, this table stores every entry directly in the backing array:
 * a key that lands on an occupied slot walks forward one slot at a
 * time until it finds a free one. That walk is the probe sequence,
 * and it is also how {@code get} finds a key - it replays the same
 * walk from the key's home slot.
 *
 * <p>Deleting is the subtle part: a removed slot cannot just be
 * emptied, or a later {@code get} would stop at the hole and miss
 * keys that probed past it. Removed slots are therefore marked with
 * a tombstone, which {@code get} steps over and {@code put} may
 * reuse. When the load factor passes 0.7 the table doubles and every
 * live entry is rehashed (tombstones are dropped in the process).
 *
 * <p>Costs: put, get and remove are O(1) on average while the load
 * stays low. They degrade towards O(n) as the table fills, which is
 * why the rehash threshold (0.7) is lower than the chaining table's
 * (0.75). Null keys and null values are rejected.
 */
public class OpenAddressingHashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double MAX_LOAD_FACTOR = 0.7;

    /** One slot in the table: either empty, a live entry, or a tombstone. */
    private static final class Entry<K, V> {
        final K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /** Marker left behind by a removed entry - probed over, never matched. */
    private static final Entry<?, ?> TOMBSTONE = new Entry<>(null, null);

    private Entry<K, V>[] slots;
    private int size;
    private int lastProbes; // probes used by the most recent put/get/remove

    /** Empty table with the default capacity of 16 slots. */
    public OpenAddressingHashTable() {
        this(DEFAULT_CAPACITY);
    }

    /** Empty table with the given starting number of slots. */
    @SuppressWarnings("unchecked")
    public OpenAddressingHashTable(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("capacity must be at least 1");
        this.slots = new Entry[initialCapacity];
        this.size = 0;
        this.lastProbes = 0;
    }

    /**
     * Store a key-value pair. If the key is already present its value is
     * replaced and the old value is returned; otherwise the pair is
     * placed in the first free slot of its probe sequence (reusing a
     * tombstone when one is met) and null is returned.
     */
    public V put(K key, V value) {
        Objects.requireNonNull(key, "hash key must not be null");
        Objects.requireNonNull(value, "hash value must not be null");
        // Grow first if the table is getting full, so probing stays short.
        if (size + 1 > slots.length * MAX_LOAD_FACTOR) {
            rehash();
        }
        int home = homeSlot(key);
        int firstTombstone = -1;
        int probes = 0;
        for (int i = 0; i < slots.length; i++) {
            int index = (home + i) % slots.length;
            probes++;
            Entry<K, V> entry = slots[index];
            if (entry == null) {
                int target = firstTombstone == -1 ? index : firstTombstone;
                slots[target] = new Entry<>(key, value);
                size++;
                lastProbes = probes;
                return null;
            }
            if (entry == TOMBSTONE) {
                if (firstTombstone == -1) firstTombstone = index;
                continue;
            }
            if (entry.key.equals(key)) {
                V old = entry.value;
                entry.value = value;
                lastProbes = probes;
                return old;
            }
        }
        // Unreachable: rehashing above guarantees a free slot exists.
        throw new IllegalStateException("hash table is full");
    }

    /** Return the value for a key, or null when the key is absent. O(1) average. */
    public V get(K key) {
        Objects.requireNonNull(key, "hash key must not be null");
        int index = findSlot(key);
        return index == -1 ? null : slots[index].value;
    }

    /** True when the table holds this key. O(1) average. */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /** True when any entry holds this value (scans every slot). O(n). */
    public boolean containsValue(V value) {
        Objects.requireNonNull(value, "hash value must not be null");
        for (Entry<K, V> entry : slots) {
            if (entry != null && entry != TOMBSTONE && entry.value.equals(value)) return true;
        }
        return false;
    }

    /**
     * Remove a key and return its value, or null when the key is absent.
     * The slot becomes a tombstone, so keys that probed past it are
     * still reachable. O(1) average.
     */
    @SuppressWarnings("unchecked")
    public V remove(K key) {
        Objects.requireNonNull(key, "hash key must not be null");
        int index = findSlot(key);
        if (index == -1) return null;
        V old = slots[index].value;
        slots[index] = (Entry<K, V>) TOMBSTONE;
        size--;
        return old;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Current number of slots (doubles on every rehash). */
    public int capacity() { return slots.length; }

    /** Entries per slot, on average - rehashing keeps it under 0.7. */
    public double loadFactor() {
        return (double) size / slots.length;
    }

    /**
     * Probes the most recent put, get or remove needed - a quick read
     * on how crowded a key's neighbourhood is. 1 means no collision.
     */
    public int lastProbes() { return lastProbes; }

    /**
     * How many tombstones are currently in the table. They are dropped
     * on the next rehash, so a high count means a rehash is near.
     */
    public int tombstoneCount() {
        int count = 0;
        for (Entry<K, V> entry : slots) {
            if (entry == TOMBSTONE) count++;
        }
        return count;
    }

    /** All keys, in slot order. O(n). */
    public List<K> keys() {
        List<K> out = new ArrayList<>(size);
        for (Entry<K, V> entry : slots) {
            if (entry != null && entry != TOMBSTONE) out.add(entry.key);
        }
        return out;
    }

    // -- internals -------------------------------------------------------

    /** Home slot for a key: the hash code folded into the table size. */
    private int homeSlot(K key) {
        return (key.hashCode() & 0x7fffffff) % slots.length;
    }

    /**
     * Index of the slot holding a key, or -1 when absent. Walks the
     * probe sequence from the home slot, stepping over tombstones and
     * stopping at the first empty slot (nothing past it can exist).
     */
    private int findSlot(K key) {
        int home = homeSlot(key);
        int probes = 0;
        for (int i = 0; i < slots.length; i++) {
            int index = (home + i) % slots.length;
            probes++;
            Entry<K, V> entry = slots[index];
            if (entry == null) {
                lastProbes = probes;
                return -1;
            }
            if (entry == TOMBSTONE) continue;
            if (entry.key.equals(key)) {
                lastProbes = probes;
                return index;
            }
        }
        lastProbes = probes;
        return -1;
    }

    /** Double the slots and rehash every live entry into its new home. O(n). */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Entry<K, V>[] old = slots;
        slots = new Entry[old.length * 2];
        size = 0;
        for (Entry<K, V> entry : old) {
            if (entry != null && entry != TOMBSTONE) {
                // Re-insert directly: no tombstones exist in the fresh table.
                int home = homeSlot(entry.key);
                for (int i = 0; i < slots.length; i++) {
                    int index = (home + i) % slots.length;
                    if (slots[index] == null) {
                        slots[index] = entry;
                        size++;
                        break;
                    }
                }
            }
        }
    }

    /** One entry per slot: live entries, "-" for empty, "x" for tombstones. */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("[");
        for (int i = 0; i < slots.length; i++) {
            if (i > 0) out.append(", ");
            Entry<K, V> entry = slots[i];
            if (entry == null) out.append(i).append(":-");
            else if (entry == TOMBSTONE) out.append(i).append(":x");
            else out.append(i).append(":").append(entry.key).append("=").append(entry.value);
        }
        return out.append("]").toString();
    }
}
