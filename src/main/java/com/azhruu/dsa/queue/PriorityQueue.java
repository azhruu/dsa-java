package com.azhruu.dsa.queue;

/**
 * Priority queue backed by a sorted singly linked list.
 *
 * <p>Items are dequeued in ascending priority order (priority 1 is served
 * before priority 2). Items sharing a priority are served FIFO, in the
 * order they were enqueued. Insertion is O(n) (list scan), dequeue and
 * peek are O(1) since the head always holds the highest-priority item.
 */
public class PriorityQueue<T> {

    private static class Node<T> {
        T value;
        int priority;
        Node<T> next;
        Node(T value, int priority) {
            this.value = value;
            this.priority = priority;
        }
    }

    private Node<T> head;
    private int size;

    /** Insert the value at the position its priority dictates. */
    public void enqueue(T value, int priority) {
        Node<T> node = new Node<>(value, priority);
        // Insert after all nodes with priority <= the new one, so equal
        // priorities keep their enqueue order (stable, FIFO within a level).
        if (head == null || priority < head.priority) {
            node.next = head;
            head = node;
        } else {
            Node<T> cur = head;
            while (cur.next != null && cur.next.priority <= priority) {
                cur = cur.next;
            }
            node.next = cur.next;
            cur.next = node;
        }
        size++;
    }

    /** Remove and return the highest-priority (lowest value) item. */
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("priority queue is empty");
        T value = head.value;
        head = head.next;
        size--;
        return value;
    }

    /** Peek at the highest-priority item without removing it. */
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("priority queue is empty");
        return head.value;
    }

    /** Priority of the item peek() would return. */
    public int peekPriority() {
        if (isEmpty()) throw new IllegalStateException("priority queue is empty");
        return head.priority;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("front -> ");
        Node<T> cur = head;
        while (cur != null) {
            sb.append(cur.value).append("(p").append(cur.priority).append(")");
            if (cur.next != null) sb.append(" | ");
            cur = cur.next;
        }
        return sb.append(" <- back").toString();
    }
}
