package com.azhruu.dsa;

import com.azhruu.dsa.list.DoublyLinkedList;
import com.azhruu.dsa.list.SinglyLinkedList;
import com.azhruu.dsa.queue.ArrayQueue;
import com.azhruu.dsa.queue.LinkedQueue;
import com.azhruu.dsa.queue.PriorityQueue;
import com.azhruu.dsa.stack.ArrayStack;
import com.azhruu.dsa.stack.LinkedStack;

/** Quick demo + smoke test of the structures built so far. */
public class Demo {

    public static void main(String[] args) {
        System.out.println("=== SinglyLinkedList ===");
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        list.addLast(10); list.addLast(20); list.addLast(30);
        list.addFirst(5);
        System.out.println("list:        " + list);
        list.addAt(2, 15);
        System.out.println("addAt(2,15): " + list);
        list.remove(20);
        System.out.println("remove(20):  " + list);
        list.reverse();
        System.out.println("reversed:    " + list);
        System.out.println("size=" + list.size() + ", contains(15)=" + list.contains(15));

        System.out.println("\n=== DoublyLinkedList ===");
        DoublyLinkedList<Integer> dlist = new DoublyLinkedList<>();
        dlist.addLast(10); dlist.addLast(20); dlist.addLast(30);
        dlist.addFirst(5);
        System.out.println("forward:       " + dlist);
        System.out.println("backward:      " + dlist.toReverseString());
        dlist.addAt(2, 15);
        System.out.println("addAt(2,15):   " + dlist + " | backward: " + dlist.toReverseString());
        dlist.remove(20);
        System.out.println("remove(20):    " + dlist);
        System.out.println("removeLast ->  " + dlist.removeLast() + ", get(1)=" + dlist.get(1));
        dlist.reverse();
        System.out.println("reversed:      " + dlist + " | backward: " + dlist.toReverseString());
        System.out.println("size=" + dlist.size() + ", contains(15)=" + dlist.contains(15));

        System.out.println("\n=== ArrayStack ===");
        ArrayStack<String> stack = new ArrayStack<>();
        stack.push("first"); stack.push("second"); stack.push("third");
        System.out.println("stack: " + stack);
        System.out.println("pop -> " + stack.pop() + ", peek -> " + stack.peek());

        System.out.println("\n=== ArrayQueue ===");
        ArrayQueue<String> queue = new ArrayQueue<>();
        queue.enqueue("Ali"); queue.enqueue("Sara"); queue.enqueue("Bilal");
        System.out.println("queue: " + queue);
        System.out.println("dequeue -> " + queue.dequeue() + ", peek -> " + queue.peek());

        System.out.println("\n=== LinkedStack ===");
        LinkedStack<String> lstack = new LinkedStack<>();
        lstack.push("first"); lstack.push("second"); lstack.push("third");
        System.out.println("stack: " + lstack);
        System.out.println("pop -> " + lstack.pop() + ", peek -> " + lstack.peek() + ", size=" + lstack.size());

        System.out.println("\n=== LinkedQueue ===");
        LinkedQueue<String> lqueue = new LinkedQueue<>();
        lqueue.enqueue("Ali"); lqueue.enqueue("Sara"); lqueue.enqueue("Bilal");
        System.out.println("queue: " + lqueue);
        System.out.println("dequeue -> " + lqueue.dequeue() + ", peek -> " + lqueue.peek() + ", size=" + lqueue.size());

        System.out.println("\n=== PriorityQueue ===");
        PriorityQueue<String> pqueue = new PriorityQueue<>();
        pqueue.enqueue("low-priority task", 3);
        pqueue.enqueue("urgent bugfix", 1);
        pqueue.enqueue("normal feature", 2);
        pqueue.enqueue("another urgent", 1);
        System.out.println("queue: " + pqueue);
        System.out.println("peek -> " + pqueue.peek() + " (p" + pqueue.peekPriority() + ")");
        System.out.print("drain order: ");
        while (!pqueue.isEmpty()) {
            System.out.print(pqueue.dequeue());
            if (!pqueue.isEmpty()) System.out.print(" -> ");
        }
        System.out.println(" (size=" + pqueue.size() + ")");

        System.out.println("\nAll structures working OK");
    }
}
