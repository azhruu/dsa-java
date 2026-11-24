package com.azhruu.dsa.list;

/**
 * Generic singly linked list implemented from scratch.
 * Operations: insert (head/tail/index), delete (first/last/value),
 * search, reverse, size.
 */
public class SinglyLinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    private Node<T> head;
    private int size;

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
        } else {
            Node<T> cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = node;
        }
        size++;
    }

    public void addAt(int index, T value) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("index " + index);
        if (index == 0) { addFirst(value); return; }
        Node<T> cur = head;
        for (int i = 0; i < index - 1; i++) cur = cur.next;
        Node<T> node = new Node<>(value);
        node.next = cur.next;
        cur.next = node;
        size++;
    }

    public T removeFirst() {
        if (head == null) throw new IllegalStateException("list is empty");
        T value = head.data;
        head = head.next;
        size--;
        return value;
    }

    /**
     * Removes and returns the last element. O(n): a singly linked list
     * has no tail pointer, so the walk stops at the second-last node.
     */
    public T removeLast() {
        if (head == null) throw new IllegalStateException("list is empty");
        if (head.next == null) return removeFirst();
        Node<T> cur = head;
        while (cur.next.next != null) cur = cur.next;
        T value = cur.next.data;
        cur.next = null;
        size--;
        return value;
    }

    public boolean remove(T value) {
        if (head == null) return false;
        if (head.data.equals(value)) { removeFirst(); return true; }
        Node<T> cur = head;
        while (cur.next != null && !cur.next.data.equals(value)) cur = cur.next;
        if (cur.next == null) return false;
        cur.next = cur.next.next;
        size--;
        return true;
    }

    public boolean contains(T value) {
        for (Node<T> cur = head; cur != null; cur = cur.next)
            if (cur.data.equals(value)) return true;
        return false;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index);
        Node<T> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.data;
    }

    public void reverse() {
        Node<T> prev = null, cur = head;
        while (cur != null) {
            Node<T> next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        head = prev;
    }

    /** Empty the list, dropping every node so it can be collected. */
    public void clear() {
        head = null;
        size = 0;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<T> cur = head; cur != null; cur = cur.next) {
            sb.append(cur.data);
            if (cur.next != null) sb.append(" -> ");
        }
        return sb.append("]").toString();
    }
}
