package com.azhruu.dsa.stack;

import java.util.Arrays;

/** Generic stack on a dynamically growing array. LIFO order. */
public class ArrayStack<T> {

    private Object[] data;
    private int top;

    public ArrayStack() {
        data = new Object[8];
        top = 0;
    }

    public void push(T value) {
        if (top == data.length) data = Arrays.copyOf(data, data.length * 2);
        data[top++] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) throw new IllegalStateException("stack is empty");
        T value = (T) data[--top];
        data[top] = null;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("stack is empty");
        return (T) data[top - 1];
    }

    public int size() { return top; }
    public boolean isEmpty() { return top == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("top -> ");
        for (int i = top - 1; i >= 0; i--) sb.append(data[i]).append(i > 0 ? " | " : "");
        return sb.toString();
    }
}
