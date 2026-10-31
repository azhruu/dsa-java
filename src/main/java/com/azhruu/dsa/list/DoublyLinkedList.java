package com.azhruu.dsa.list;

/**
 * Generic doubly linked list implemented from scratch.
 * Each node links to both neighbours, so the list can be walked
 * forwards from the head and backwards from the tail in O(n).
 * Operations: insert (head/tail/index), delete, search, reverse, size.
 */
public class DoublyLinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void addAt(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index " + index);
        if (index == 0) { addFirst(value); return; }
        if (index == size) { addLast(value); return; }
        Node<T> cur = nodeAt(index);
        Node<T> node = new Node<>(value);
        node.prev = cur.prev;
        node.next = cur;
        cur.prev.next = node;
        cur.prev = node;
        size++;
    }

    public T removeFirst() {
        if (head == null) throw new IllegalStateException("list is empty");
        T value = head.data;
        head = head.next;
        if (head == null) tail = null;
        else head.prev = null;
        size--;
        return value;
    }

    public T removeLast() {
        if (tail == null) throw new IllegalStateException("list is empty");
        T value = tail.data;
        tail = tail.prev;
        if (tail == null) head = null;
        else tail.next = null;
        size--;
        return value;
    }

    public boolean remove(T value) {
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            if (cur.data.equals(value)) {
                unlink(cur);
                return true;
            }
        }
        return false;
    }

    private void unlink(Node<T> node) {
        if (node.prev == null) head = node.next;
        else node.prev.next = node.next;
        if (node.next == null) tail = node.prev;
        else node.next.prev = node.prev;
        size--;
    }

    public boolean contains(T value) {
        return nodeOf(value) != null;
    }

    private Node<T> nodeOf(T value) {
        for (Node<T> cur = head; cur != null; cur = cur.next)
            if (cur.data.equals(value)) return cur;
        return null;
    }

    public T get(int index) {
        return nodeAt(index).data;
    }

    /** Walks from the nearer end, so indexed access is O(min(i, n - i)). */
    private Node<T> nodeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index);
        Node<T> cur;
        if (index < size / 2) {
            cur = head;
            for (int i = 0; i < index; i++) cur = cur.next;
        } else {
            cur = tail;
            for (int i = size - 1; i > index; i--) cur = cur.prev;
        }
        return cur;
    }

    public void reverse() {
        Node<T> cur = head;
        while (cur != null) {
            Node<T> next = cur.next;
            cur.next = cur.prev;
            cur.prev = next;
            cur = next;
        }
        Node<T> oldHead = head;
        head = tail;
        tail = oldHead;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Forward traversal, head to tail. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            sb.append(cur.data);
            if (cur.next != null) sb.append(" <-> ");
        }
        return sb.append("]").toString();
    }

    /** Backward traversal, tail to head. */
    public String toReverseString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<T> cur = tail; cur != null; cur = cur.prev) {
            sb.append(cur.data);
            if (cur.prev != null) sb.append(" <-> ");
        }
        return sb.append("]").toString();
    }
}
