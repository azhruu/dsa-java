package com.azhruu.dsa.queue;

/** Generic FIFO queue on a circular array buffer. */
public class ArrayQueue<T> {

    private Object[] data;
    private int head, tail, size;

    public ArrayQueue() {
        data = new Object[8];
    }

    public void enqueue(T value) {
        if (size == data.length) grow();
        data[tail] = value;
        tail = (tail + 1) % data.length;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) throw new IllegalStateException("queue is empty");
        T value = (T) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("queue is empty");
        return (T) data[head];
    }

    private void grow() {
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < size; i++) bigger[i] = data[(head + i) % data.length];
        data = bigger;
        head = 0;
        tail = size;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("front -> ");
        for (int i = 0; i < size; i++) {
            sb.append(data[(head + i) % data.length]);
            if (i < size - 1) sb.append(" | ");
        }
        return sb.append(" <- back").toString();
    }
}
