package com.azhruu.dsa.queue;

/** Generic FIFO queue on singly linked nodes. No fixed capacity. */
public class LinkedQueue<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
        Node(T value) { this.value = value; }
    }

    private Node<T> head, tail;
    private int size;

    public void enqueue(T value) {
        Node<T> node = new Node<>(value);
        if (isEmpty()) head = node;
        else tail.next = node;
        tail = node;
        size++;
    }

    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("queue is empty");
        T value = head.value;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return value;
    }

    public T peek() {
        if (isEmpty()) throw new IllegalStateException("queue is empty");
        return head.value;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("front -> ");
        Node<T> cur = head;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(" | ");
            cur = cur.next;
        }
        return sb.append(" <- back").toString();
    }
}
