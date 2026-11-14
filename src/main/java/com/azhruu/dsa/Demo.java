package com.azhruu.dsa;

import com.azhruu.dsa.graph.Graph;
import com.azhruu.dsa.graph.WeightedGraph;
import com.azhruu.dsa.hash.HashTable;
import com.azhruu.dsa.hash.OpenAddressingHashTable;
import com.azhruu.dsa.heap.Heap;
import com.azhruu.dsa.list.DoublyLinkedList;
import com.azhruu.dsa.list.SinglyLinkedList;
import com.azhruu.dsa.queue.ArrayQueue;
import com.azhruu.dsa.queue.LinkedQueue;
import com.azhruu.dsa.queue.PriorityQueue;
import com.azhruu.dsa.sort.HeapSort;
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

        System.out.println("\n=== HeapSort ===");
        Integer[] unsorted = {42, 7, 19, 3, 88, 15, 64, 27};
        HeapSort.sort(unsorted);
        System.out.println("sort{42,7,19,3,88,15,64,27}: " + java.util.Arrays.toString(unsorted)
                + " (comparisons=" + HeapSort.lastComparisons()
                + ", swaps=" + HeapSort.lastSwaps() + ")");
        // Worst case for heap sort is still O(n log n): reverse-sorted input.
        Integer[] reversed = {90, 80, 70, 60, 50, 40, 30, 20, 10};
        HeapSort.sort(reversed);
        System.out.println("sort reverse 90..10: " + java.util.Arrays.toString(reversed)
                + ", sorted=" + HeapSort.isSorted(reversed));
        Integer[] already = {1, 2, 3, 4, 5};
        HeapSort.sort(already);
        System.out.println("sort already sorted: " + java.util.Arrays.toString(already)
                + " (comparisons=" + HeapSort.lastComparisons() + ")");
        HeapSort.sortDescending(already);
        System.out.println("sortDescending:      " + java.util.Arrays.toString(already));
        Integer[] original = {5, 2, 9, 1};
        Integer[] copy = HeapSort.sortedCopy(original);
        System.out.println("sortedCopy{5,2,9,1}: " + java.util.Arrays.toString(copy)
                + ", input untouched: " + java.util.Arrays.toString(original));
        Integer[] one = {7};
        HeapSort.sort(one);
        System.out.println("single element: " + java.util.Arrays.toString(one)
                + ", isSorted=" + HeapSort.isSorted(one));
        Integer[] empty = {};
        HeapSort.sort(empty);
        System.out.println("empty array: sorted=" + HeapSort.isSorted(empty)
                + " (size=" + empty.length + ")");
        String[] words = {"pear", "apple", "fig", "banana"};
        HeapSort.sort(words);
        System.out.println("sort strings: " + java.util.Arrays.toString(words));

        System.out.println("\n=== HashTable (separate chaining) ===");
        // 1, 17 and 33 all hash to bucket 1 at capacity 16: a real collision chain.
        HashTable<Integer, String> table = new HashTable<>();
        table.put(1, "one"); table.put(17, "seventeen"); table.put(33, "thirty-three");
        table.put(5, "five");
        System.out.println("buckets: " + table);
        System.out.println("chain at bucket 1: length=" + table.chainLength(1)
                + ", get(17)=" + table.get(17) + ", get(33)=" + table.get(33));
        System.out.println("get missing 99 -> " + table.get(99)
                + ", containsKey(5)=" + table.containsKey(5)
                + ", containsValue(\"five\")=" + table.containsValue("five"));
        System.out.println("overwrite put(5,\"FIVE\") -> old=" + table.put(5, "FIVE")
                + ", get(5)=" + table.get(5) + " (size=" + table.size() + ")");
        System.out.println("remove(17) -> " + table.remove(17)
                + ", remove missing 99 -> " + table.remove(99));
        System.out.println("after remove: " + table);
        // 13 entries push the load past 0.75, so the table rehashes 16 -> 32.
        HashTable<String, Integer> grown = new HashTable<>();
        String[] words2 = {"alpha", "bravo", "charlie", "delta", "echo", "foxtrot",
                "golf", "hotel", "india", "juliet", "kilo", "lima", "mike"};
        for (int i = 0; i < words2.length; i++) grown.put(words2[i], i + 1);
        System.out.println("13 puts: capacity=" + grown.capacity()
                + ", size=" + grown.size()
                + ", load=" + String.format("%.2f", grown.loadFactor()));
        System.out.println("get(\"golf\")=" + grown.get("golf") + ", get(\"mike\")=" + grown.get("mike"));
        System.out.println("keys: " + grown.keys());
        HashTable<String, String> emptyTable = new HashTable<>();
        System.out.println("empty: isEmpty=" + emptyTable.isEmpty()
                + ", size=" + emptyTable.size() + ", " + emptyTable);

        System.out.println("\n=== OpenAddressingHashTable (linear probing) ===");
        // 1, 17 and 33 all hash to slot 1 at capacity 16: linear probing
        // must walk them to slots 1, 2 and 3 instead of chaining them.
        OpenAddressingHashTable<Integer, String> probe = new OpenAddressingHashTable<>();
        probe.put(1, "one");
        System.out.println("put(1):  probes=" + probe.lastProbes());
        probe.put(17, "seventeen");
        System.out.println("put(17): probes=" + probe.lastProbes() + " (collided with 1, walked to slot 2)");
        probe.put(33, "thirty-three");
        System.out.println("put(33): probes=" + probe.lastProbes() + " (walked to slot 3)");
        probe.put(5, "five");
        System.out.println("table: " + probe);
        System.out.println("get(17)=" + probe.get(17) + " (probes=" + probe.lastProbes() + ")"
                + ", get(33)=" + probe.get(33) + ", get missing 99 -> " + probe.get(99));
        System.out.println("containsKey(5)=" + probe.containsKey(5)
                + ", containsValue(\"five\")=" + probe.containsValue("five"));
        System.out.println("overwrite put(5,\"FIVE\") -> old=" + probe.put(5, "FIVE")
                + ", get(5)=" + probe.get(5) + " (size=" + probe.size() + ")");
        // The tombstone test: removing 17 must not hide 33, which probed past it.
        System.out.println("remove(17) -> " + probe.remove(17)
                + ", tombstones=" + probe.tombstoneCount()
                + ", get(33) still=" + probe.get(33) + " (probes=" + probe.lastProbes() + ")");
        System.out.println("remove missing 99 -> " + probe.remove(99));
        System.out.println("after remove: " + probe);
        // 13 entries push the load past 0.7, so the table rehashes 16 -> 32
        // and the tombstone is dropped in the process.
        OpenAddressingHashTable<String, Integer> grownProbe = new OpenAddressingHashTable<>();
        for (int i = 0; i < words2.length; i++) grownProbe.put(words2[i], i + 1);
        System.out.println("13 puts: capacity=" + grownProbe.capacity()
                + ", size=" + grownProbe.size()
                + ", load=" + String.format("%.2f", grownProbe.loadFactor()));
        System.out.println("get(\"golf\")=" + grownProbe.get("golf") + ", get(\"mike\")=" + grownProbe.get("mike"));
        System.out.println("keys: " + grownProbe.keys());
        OpenAddressingHashTable<String, String> emptyProbe = new OpenAddressingHashTable<>();
        System.out.println("empty: isEmpty=" + emptyProbe.isEmpty()
                + ", size=" + emptyProbe.size() + ", " + emptyProbe);

        System.out.println("\n=== Graph (adjacency list) + BFS ===");
        Graph<String> graph = new Graph<>();
        graph.addEdge("A", "B"); graph.addEdge("A", "C");
        graph.addEdge("B", "D"); graph.addEdge("C", "D"); graph.addEdge("C", "E");
        graph.addEdge("D", "F"); graph.addEdge("E", "F");
        graph.addVertex("G"); // isolated: no edges, unreachable from A
        System.out.println("adjacency list:\n" + graph);
        System.out.println("vertices=" + graph.vertexCount() + ", edges=" + graph.edgeCount()
                + ", hasEdge(A,B)=" + graph.hasEdge("A", "B")
                + ", hasEdge(A,D)=" + graph.hasEdge("A", "D"));
        System.out.println("neighbours(C)=" + graph.neighbors("C") + ", degree(C)=" + graph.degree("C"));
        System.out.println("bfs(A):         " + graph.bfs("A"));
        System.out.println("distances(A):   " + graph.bfsDistances("A"));
        System.out.println("path A -> F:    " + graph.bfsPath("A", "F") + " (shortest: 3 edges)");
        System.out.println("path A -> G:    " + graph.bfsPath("A", "G") + " (unreachable)");
        graph.addEdge("A", "B"); // duplicate: ignored, no parallel edge
        System.out.println("duplicate addEdge(A,B): edges still=" + graph.edgeCount()
                + ", duplicate addVertex(A) -> " + graph.addVertex("A"));
        graph.removeEdge("C", "E");
        System.out.println("removeEdge(C,E): edges=" + graph.edgeCount()
                + ", bfs(A) now " + graph.bfs("A"));
        graph.removeVertex("E");
        System.out.println("removeVertex(E): vertices=" + graph.vertexCount()
                + ", edges=" + graph.edgeCount() + ", hasVertex(E)=" + graph.hasVertex("E"));
        // Directed: edges only go one way, so Z is a dead end from X.
        Graph<String> directed = new Graph<>(true);
        directed.addEdge("X", "Y"); directed.addEdge("Y", "Z"); directed.addEdge("X", "Z");
        System.out.println("directed: bfs(X)=" + directed.bfs("X")
                + ", bfs(Z)=" + directed.bfs("Z")
                + ", path X -> Z: " + directed.bfsPath("X", "Z"));
        try {
            graph.bfs("missing");
        } catch (IllegalArgumentException e) {
            System.out.println("bfs(missing) -> " + e.getMessage());
        }
        Graph<String> emptyGraph = new Graph<>();
        System.out.println("empty graph: isEmpty=" + emptyGraph.isEmpty()
                + ", vertices=" + emptyGraph.vertexCount() + ", edges=" + emptyGraph.edgeCount());

        System.out.println("\n=== Graph DFS + cycle detection ===");
        // Same diamond as the BFS demo, plus isolated G.
        Graph<String> dfsGraph = new Graph<>();
        dfsGraph.addEdge("A", "B"); dfsGraph.addEdge("A", "C");
        dfsGraph.addEdge("B", "D"); dfsGraph.addEdge("C", "D"); dfsGraph.addEdge("C", "E");
        dfsGraph.addEdge("D", "F"); dfsGraph.addEdge("E", "F");
        dfsGraph.addVertex("G");
        System.out.println("dfs(A):         " + dfsGraph.dfs("A")
                + " (dives B -> D -> C -> E before F)");
        System.out.println("dfs forest:     " + dfsGraph.dfsForest() + " (isolated G included)");
        System.out.println("dfs path A->F:  " + dfsGraph.dfsPath("A", "F") + " (not the shortest: BFS gave 3 edges)");
        System.out.println("dfs path A->G:  " + dfsGraph.dfsPath("A", "G") + " (unreachable)");
        System.out.println("hasCycle:       " + dfsGraph.hasCycle() + " (A-B-D-C-A closes a loop)");
        // A tree has no cycle: DFS order and the cycle check agree.
        Graph<String> tree = new Graph<>();
        tree.addEdge("A", "B"); tree.addEdge("A", "C"); tree.addEdge("B", "D");
        System.out.println("tree dfs(A):    " + tree.dfs("A") + ", hasCycle=" + tree.hasCycle());
        // Directed: a loop X -> Y -> Z -> X is a cycle; X -> Y -> Z is not.
        Graph<String> cyclic = new Graph<>(true);
        cyclic.addEdge("X", "Y"); cyclic.addEdge("Y", "Z"); cyclic.addEdge("Z", "X");
        System.out.println("directed cycle: dfs(X)=" + cyclic.dfs("X")
                + ", hasCycle=" + cyclic.hasCycle());
        Graph<String> dag = new Graph<>(true);
        dag.addEdge("X", "Y"); dag.addEdge("Y", "Z"); dag.addEdge("X", "Z");
        System.out.println("directed acyclic: dfs(X)=" + dag.dfs("X")
                + ", hasCycle=" + dag.hasCycle());
        // A self-loop is a cycle of one vertex.
        Graph<String> loop = new Graph<>();
        loop.addEdge("S", "S");
        System.out.println("self-loop: hasCycle=" + loop.hasCycle() + ", dfs(S)=" + loop.dfs("S"));
        Graph<String> emptyCycle = new Graph<>();
        System.out.println("empty graph: hasCycle=" + emptyCycle.hasCycle()
                + ", dfs forest=" + emptyCycle.dfsForest());
        try {
            dfsGraph.dfs("missing");
        } catch (IllegalArgumentException e) {
            System.out.println("dfs(missing) -> " + e.getMessage());
        }

        System.out.println("\n=== WeightedGraph + Dijkstra ===");
        WeightedGraph<String> wgraph = new WeightedGraph<>();
        wgraph.addEdge("A", "B", 4); wgraph.addEdge("A", "C", 2);
        wgraph.addEdge("C", "B", 1); wgraph.addEdge("B", "D", 5);
        wgraph.addEdge("C", "D", 8); wgraph.addEdge("C", "E", 10);
        wgraph.addEdge("D", "E", 2); wgraph.addEdge("D", "F", 6);
        wgraph.addEdge("E", "F", 3);
        wgraph.addVertex("G"); // isolated: unreachable from A
        System.out.println("weighted adjacency list:\n" + wgraph);
        System.out.println("vertices=" + wgraph.vertexCount() + ", edges=" + wgraph.edgeCount()
                + ", weight(A,B)=" + wgraph.weight("A", "B")
                + ", weight(A,D)=" + wgraph.weight("A", "D") + " (no edge)");
        System.out.println("neighbours(C)=" + wgraph.neighbors("C") + ", degree(C)=" + wgraph.degree("C"));
        System.out.println("distances(A):   " + wgraph.dijkstraDistances("A")
                + " (B=3 via C, not 4 direct)");
        System.out.println("path A -> F:    " + wgraph.dijkstraPath("A", "F")
                + " (cost 13: A-C-B-D-E-F)");
        System.out.println("path A -> E:    " + wgraph.dijkstraPath("A", "E") + " (cost 10)");
        System.out.println("path A -> G:    " + wgraph.dijkstraPath("A", "G") + " (unreachable)");
        wgraph.addEdge("A", "B", 9); // duplicate: ignored, weight stays 4
        System.out.println("duplicate addEdge(A,B,9): weight(A,B) still=" + wgraph.weight("A", "B"));
        wgraph.setWeight("A", "B", 1);
        System.out.println("setWeight(A,B,1): distances(A) now " + wgraph.dijkstraDistances("A")
                + ", path A -> F now " + wgraph.dijkstraPath("A", "F") + " (cost 11)");
        wgraph.removeEdge("C", "D");
        System.out.println("removeEdge(C,D): edges=" + wgraph.edgeCount()
                + ", distances(A) still " + wgraph.dijkstraDistances("A"));
        // Directed: edges only go one way, and the two-hop route beats the direct edge.
        WeightedGraph<String> wdirected = new WeightedGraph<>(true);
        wdirected.addEdge("X", "Y", 1); wdirected.addEdge("Y", "Z", 2); wdirected.addEdge("X", "Z", 5);
        System.out.println("directed: distances(X)=" + wdirected.dijkstraDistances("X")
                + ", path X -> Z: " + wdirected.dijkstraPath("X", "Z") + " (cost 3, not 5 direct)");
        System.out.println("directed: distances(Z)=" + wdirected.dijkstraDistances("Z")
                + ", path Z -> X: " + wdirected.dijkstraPath("Z", "X") + " (no way back)");
        try {
            wgraph.addEdge("P", "Q", -1);
        } catch (IllegalArgumentException e) {
            System.out.println("addEdge(P,Q,-1) -> " + e.getMessage());
        }
        try {
            wgraph.dijkstraDistances("missing");
        } catch (IllegalArgumentException e) {
            System.out.println("dijkstra(missing) -> " + e.getMessage());
        }
        WeightedGraph<String> emptyWeighted = new WeightedGraph<>();
        System.out.println("empty weighted graph: isEmpty=" + emptyWeighted.isEmpty()
                + ", vertices=" + emptyWeighted.vertexCount() + ", edges=" + emptyWeighted.edgeCount());

        System.out.println("\nAll structures working OK");
    }
}
