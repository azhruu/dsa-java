package com.azhruu.dsa;

import com.azhruu.dsa.heap.Heap;
import com.azhruu.dsa.list.DoublyLinkedList;
import com.azhruu.dsa.list.SinglyLinkedList;
import com.azhruu.dsa.queue.ArrayQueue;
import com.azhruu.dsa.queue.LinkedQueue;
import com.azhruu.dsa.queue.PriorityQueue;
import com.azhruu.dsa.stack.ArrayStack;
import com.azhruu.dsa.stack.LinkedStack;
import com.azhruu.dsa.tree.AvlTree;
import com.azhruu.dsa.tree.BinarySearchTree;

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

        System.out.println("\n=== BinarySearchTree ===");
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        int[] keys = {50, 30, 70, 20, 40, 60, 80, 35};
        for (int k : keys) bst.insert(k);
        System.out.println("in-order:  " + bst.inOrder());
        System.out.println("pre-order: " + bst.preOrder());
        System.out.println("post-order:" + bst.postOrder());
        System.out.println("min=" + bst.min() + ", max=" + bst.max()
                + ", height=" + bst.height() + ", size=" + bst.size());
        System.out.println("contains(35)=" + bst.contains(35) + ", contains(99)=" + bst.contains(99));
        System.out.println("duplicate insert(40) -> " + bst.insert(40) + " (size=" + bst.size() + ")");
        bst.delete(20); // leaf
        System.out.println("delete leaf 20:      " + bst.inOrder());
        bst.delete(30); // one child (40 -> 35)
        System.out.println("delete one-child 30: " + bst.inOrder());
        bst.delete(50); // two children (root)
        System.out.println("delete root 50:      " + bst.inOrder());
        System.out.println("delete missing 99 -> " + bst.delete(99) + " (size=" + bst.size() + ")");

        System.out.println("\n=== AvlTree ===");
        // One rotation case each: LL (30,20,10), RR (10,20,30),
        // LR (30,10,20), RL (10,30,20).
        int[][] cases = {{30, 20, 10}, {10, 20, 30}, {30, 10, 20}, {10, 30, 20}};
        String[] names = {"LL", "RR", "LR", "RL"};
        for (int c = 0; c < cases.length; c++) {
            AvlTree<Integer> avl = new AvlTree<>();
            for (int k : cases[c]) avl.insert(k);
            System.out.println(names[c] + " case " + java.util.Arrays.toString(cases[c])
                    + " -> in-order " + avl.inOrder()
                    + ", height=" + avl.height()
                    + ", balanced=" + avl.isBalanced()
                    + ", rotations=" + avl.getRotationCount());
        }
        // Sorted input: a plain BST would become a 9-high chain; the AVL
        // tree must stay logarithmic.
        AvlTree<Integer> avl = new AvlTree<>();
        BinarySearchTree<Integer> plain = new BinarySearchTree<>();
        for (int k = 10; k <= 100; k += 10) {
            avl.insert(k);
            plain.insert(k);
        }
        System.out.println("sorted 10..100: AVL height=" + avl.height()
                + " (BST height=" + plain.height() + ")"
                + ", balanced=" + avl.isBalanced()
                + ", rotations=" + avl.getRotationCount());
        System.out.println("in-order:  " + avl.inOrder());
        System.out.println("min=" + avl.min() + ", max=" + avl.max()
                + ", contains(50)=" + avl.contains(50) + ", contains(55)=" + avl.contains(55));
        System.out.println("duplicate insert(50) -> " + avl.insert(50) + " (size=" + avl.size() + ")");
        avl.delete(100); // leaf
        avl.delete(20);  // one child
        avl.delete(50);  // two children (root)
        System.out.println("after deletes: " + avl.inOrder()
                + ", height=" + avl.height() + ", balanced=" + avl.isBalanced());
        System.out.println("delete missing 99 -> " + avl.delete(99) + " (size=" + avl.size() + ")");

        System.out.println("\n=== MaxHeap ===");
        Heap<Integer> maxHeap = Heap.maxHeap();
        for (int k : keys) maxHeap.insert(k);
        System.out.println("heap (array layout): " + maxHeap);
        System.out.println("peek -> " + maxHeap.peek() + ", valid=" + maxHeap.isValid()
                + ", size=" + maxHeap.size());
        System.out.println("extract -> " + maxHeap.extract() + ", heap now: " + maxHeap
                + ", valid=" + maxHeap.isValid());
        System.out.print("drain order: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.extract());
            if (!maxHeap.isEmpty()) System.out.print(" -> ");
        }
        System.out.println(" (empty=" + maxHeap.isEmpty() + ")");

        System.out.println("\n=== MinHeap ===");
        Heap<Integer> minHeap = Heap.minHeap();
        for (int k : keys) minHeap.insert(k);
        System.out.println("heap (array layout): " + minHeap);
        System.out.println("peek -> " + minHeap.peek() + ", valid=" + minHeap.isValid());
        System.out.print("drain order: ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.extract());
            if (!minHeap.isEmpty()) System.out.print(" -> ");
        }
        System.out.println();

        System.out.println("\n=== Heapify (O(n) build) ===");
        Integer[] raw = {9, 4, 7, 1, 8, 3};
        Heap<Integer> built = Heap.heapifyMax(raw);
        System.out.println("heapifyMax{9,4,7,1,8,3}: " + built
                + ", peek=" + built.peek() + ", valid=" + built.isValid());
        System.out.println("input untouched: " + java.util.Arrays.toString(raw));
        Heap<Integer> builtMin = Heap.heapifyMin(raw);
        System.out.println("heapifyMin{9,4,7,1,8,3}: " + builtMin
                + ", peek=" + builtMin.peek() + ", valid=" + builtMin.isValid());
        // 40 inserts force the backing array past its initial capacity.
        Heap<Integer> big = Heap.minHeap();
        for (int i = 40; i >= 1; i--) big.insert(i);
        System.out.println("40 inserts (growth): size=" + big.size()
                + ", peek=" + big.peek() + ", valid=" + big.isValid());

        System.out.println("\nAll structures working OK");
    }
}
