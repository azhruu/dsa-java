package com.azhruu.dsa.stack;

/** Generic stack on singly linked nodes. LIFO order, no fixed capacity. */
public class LinkedStack<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private Node<T> top;
    private int size;

    public void push(T value) {
        top = new Node<>(value, top);
        size++;
    }

    public T pop() {
        if (isEmpty()) throw new IllegalStateException("stack is empty");
        T value = top.value;
        top = top.next;
        size--;
        return value;
    }

    public T peek() {
        if (isEmpty()) throw new IllegalStateException("stack is empty");
        return top.value;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("top -> ");
        Node<T> cur = top;
        while (cur != null) {
            sb.append(cur.value);
            if (cur.next != null) sb.append(" | ");
            cur = cur.next;
        }
        return sb.toString();
    }
}
