package com.azhruu.dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import com.azhruu.dsa.search.BinarySearch;
import com.azhruu.dsa.sort.HeapSort;
import com.azhruu.dsa.sort.Sorting;
import com.azhruu.dsa.stack.ArrayStack;
import com.azhruu.dsa.stack.LinkedStack;
import com.azhruu.dsa.tree.AvlTree;
import com.azhruu.dsa.tree.BinarySearchTree;

/**
 * Unit-style assertion tests for every structure in dsa-java.
 *
 * <p>No test framework, no dependencies: a plain {@code main} plus
 * checks, so the suite runs with the same {@code javac} / {@code java}
 * commands as the demo. Each check prints only when it fails; the
 * summary at the end says how many passed and failed.
 *
 * <p>Run it:
 * <pre>
 * javac -d out $(find src -name "*.java")
 * java -cp out com.azhruu.dsa.Tests
 * </pre>
 * Exit code 0 means every check passed.
 */
public final class Tests {

    private static int passed;
    private static int failed;
    private static String section;

    private Tests() { }

    private static void section(String name) {
        section = name;
        System.out.println("\n=== " + name + " ===");
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
        } else {
            failed++;
            System.out.println("FAIL [" + section + "] " + name);
        }
    }

    private static void eq(String name, Object expected, Object actual) {
        check(name + " (expected " + expected + ", got " + actual + ")",
                expected == null ? actual == null : expected.equals(actual));
    }

    private static void throwsCheck(String name, Class<? extends Throwable> type, Runnable action) {
        try {
            action.run();
            check(name + " (expected " + type.getSimpleName() + ", nothing thrown)", false);
        } catch (Throwable t) {
            check(name + " (threw " + t.getClass().getSimpleName()
                    + ", expected " + type.getSimpleName() + ")", type.isInstance(t));
        }
    }

    public static void main(String[] args) {
        testSinglyLinkedList();
        testDoublyLinkedList();
        testArrayStack();
        testLinkedStack();
        testArrayQueue();
        testLinkedQueue();
        testPriorityQueue();
        testBinarySearchTree();
        testAvlTree();
        testHeap();
        testHashTable();
        testOpenAddressingHashTable();
        testSorting();
        testHeapSort();
        testBinarySearch();
        testGraph();
        testWeightedGraph();

        System.out.println("\nTests: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) {
            System.out.println("Some checks failed -- see FAIL lines above.");
            System.exit(1);
        }
        System.out.println("All checks passed.");
    }

    private static void testSinglyLinkedList() {
        section("SinglyLinkedList");
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        check("starts empty", list.isEmpty() && list.size() == 0);
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addFirst(5);
        eq("addFirst/addLast order", "[5 -> 10 -> 20 -> 30]", list.toString());
        list.addAt(2, 15);
        eq("addAt(2, 15)", "[5 -> 10 -> 15 -> 20 -> 30]", list.toString());
        eq("get(2)", 15, list.get(2));
        check("contains present", list.contains(15));
        check("contains absent", !list.contains(99));
        check("remove(20)", list.remove(20));
        eq("after remove(20)", "[5 -> 10 -> 15 -> 30]", list.toString());
        check("remove missing returns false", !list.remove(99));
        eq("removeFirst", 5, list.removeFirst());
        eq("removeLast", 30, list.removeLast());
        eq("after removeLast", "[10 -> 15]", list.toString());
        SinglyLinkedList<Integer> single = new SinglyLinkedList<>();
        single.addLast(7);
        eq("removeLast on one element", 7, single.removeLast());
        check("empty after removeLast on one element", single.isEmpty());
        list.reverse();
        eq("reversed", "[15 -> 10]", list.toString());
        eq("size", 2, list.size());
        SinglyLinkedList<Integer> empty = new SinglyLinkedList<>();
        throwsCheck("removeFirst on empty", IllegalStateException.class, empty::removeFirst);
        throwsCheck("removeLast on empty", IllegalStateException.class, empty::removeLast);
        throwsCheck("addAt past the end", IndexOutOfBoundsException.class,
                () -> list.addAt(9, 1));
        throwsCheck("addAt negative", IndexOutOfBoundsException.class,
                () -> list.addAt(-1, 1));
    }

    private static void testDoublyLinkedList() {
        section("DoublyLinkedList");
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();
        check("starts empty", list.isEmpty() && list.size() == 0);
        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addFirst(5);
        eq("addFirst/addLast order", "[5 <-> 10 <-> 20 <-> 30]", list.toString());
        eq("backward order", "[30 <-> 20 <-> 10 <-> 5]", list.toReverseString());
        list.addAt(2, 15);
        eq("addAt(2, 15)", "[5 <-> 10 <-> 15 <-> 20 <-> 30]", list.toString());
        eq("get(2)", 15, list.get(2));
        check("contains present", list.contains(15));
        eq("removeFirst", 5, list.removeFirst());
        eq("removeLast", 30, list.removeLast());
        check("remove(20)", list.remove(20));
        eq("after removes", "[10 <-> 15]", list.toString());
        eq("backward after removes", "[15 <-> 10]", list.toReverseString());
        list.reverse();
        eq("reversed", "[15 <-> 10]", list.toString());
        eq("reversed backward", "[10 <-> 15]", list.toReverseString());
        eq("size", 2, list.size());
    }

    private static void testArrayStack() {
        section("ArrayStack");
        ArrayStack<Integer> stack = new ArrayStack<>();
        check("starts empty", stack.isEmpty() && stack.size() == 0);
        stack.push(10);
        stack.push(20);
        stack.push(30);
        eq("toString shows top first", "top -> 30 | 20 | 10", stack.toString());
        eq("peek", 30, stack.peek());
        eq("pop order", 30, stack.pop());
        eq("pop order", 20, stack.pop());
        eq("pop order", 10, stack.pop());
        check("empty after pops", stack.isEmpty());
        ArrayStack<Integer> empty = new ArrayStack<>();
        throwsCheck("pop on empty", IllegalStateException.class, empty::pop);
        throwsCheck("peek on empty", IllegalStateException.class, empty::peek);
        // Growth past the initial capacity, then a full LIFO drain.
        ArrayStack<Integer> big = new ArrayStack<>();
        for (int i = 0; i < 100; i++) big.push(i);
        eq("grew to 100", 100, big.size());
        boolean lifo = true;
        for (int i = 99; i >= 0; i--) lifo &= big.pop() == i;
        check("100 pushes drain in LIFO order", lifo);
    }

    private static void testLinkedStack() {
        section("LinkedStack");
        LinkedStack<String> stack = new LinkedStack<>();
        check("starts empty", stack.isEmpty() && stack.size() == 0);
        stack.push("a");
        stack.push("b");
        stack.push("c");
        eq("peek", "c", stack.peek());
        eq("pop order", "c", stack.pop());
        eq("pop order", "b", stack.pop());
        eq("pop order", "a", stack.pop());
        check("empty after pops", stack.isEmpty());
        LinkedStack<String> empty = new LinkedStack<>();
        throwsCheck("pop on empty", IllegalStateException.class, empty::pop);
        throwsCheck("peek on empty", IllegalStateException.class, empty::peek);
    }

    private static void testArrayQueue() {
        section("ArrayQueue");
        ArrayQueue<Integer> queue = new ArrayQueue<>();
        check("starts empty", queue.isEmpty() && queue.size() == 0);
        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        eq("toString shows front first", "front -> 10 | 20 | 30 <- back", queue.toString());
        eq("peek", 10, queue.peek());
        eq("dequeue order", 10, queue.dequeue());
        eq("dequeue order", 20, queue.dequeue());
        // Wrap-around: the freed slots at the front are reused.
        queue.enqueue(40);
        queue.enqueue(50);
        eq("after wrap-around", "front -> 30 | 40 | 50 <- back", queue.toString());
        eq("dequeue order", 30, queue.dequeue());
        eq("dequeue order", 40, queue.dequeue());
        eq("dequeue order", 50, queue.dequeue());
        check("empty after dequeues", queue.isEmpty());
        ArrayQueue<Integer> empty = new ArrayQueue<>();
        throwsCheck("dequeue on empty", IllegalStateException.class, empty::dequeue);
        throwsCheck("peek on empty", IllegalStateException.class, empty::peek);
        // Growth past the initial capacity, then a full FIFO drain.
        ArrayQueue<Integer> big = new ArrayQueue<>();
        for (int i = 0; i < 100; i++) big.enqueue(i);
        eq("grew to 100", 100, big.size());
        boolean fifo = true;
        for (int i = 0; i < 100; i++) fifo &= big.dequeue() == i;
        check("100 enqueues drain in FIFO order", fifo);
    }

    private static void testLinkedQueue() {
        section("LinkedQueue");
        LinkedQueue<String> queue = new LinkedQueue<>();
        check("starts empty", queue.isEmpty() && queue.size() == 0);
        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        eq("peek", "a", queue.peek());
        eq("dequeue order", "a", queue.dequeue());
        eq("dequeue order", "b", queue.dequeue());
        eq("dequeue order", "c", queue.dequeue());
        check("empty after dequeues", queue.isEmpty());
        LinkedQueue<String> empty = new LinkedQueue<>();
        throwsCheck("dequeue on empty", IllegalStateException.class, empty::dequeue);
        throwsCheck("peek on empty", IllegalStateException.class, empty::peek);
    }

    private static void testPriorityQueue() {
        section("PriorityQueue");
        PriorityQueue<String> queue = new PriorityQueue<>();
        check("starts empty", queue.isEmpty() && queue.size() == 0);
        queue.enqueue("low", 3);
        queue.enqueue("urgent", 1);
        queue.enqueue("mid", 2);
        queue.enqueue("also-urgent", 1);
        eq("peek is highest priority", "urgent", queue.peek());
        eq("peekPriority", 1, queue.peekPriority());
        // Lowest priority number first; equal priorities stay FIFO.
        List<String> drain = new ArrayList<>();
        while (!queue.isEmpty()) drain.add(queue.dequeue());
        eq("drain order", Arrays.asList("urgent", "also-urgent", "mid", "low"), drain);
        PriorityQueue<String> empty = new PriorityQueue<>();
        throwsCheck("dequeue on empty", IllegalStateException.class, empty::dequeue);
        throwsCheck("peek on empty", IllegalStateException.class, empty::peek);
        throwsCheck("peekPriority on empty", IllegalStateException.class, empty::peekPriority);
    }

    private static void testBinarySearchTree() {
        section("BinarySearchTree");
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        check("starts empty", bst.isEmpty() && bst.size() == 0);
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) check("insert " + v, bst.insert(v));
        check("duplicate insert refused", !bst.insert(50));
        eq("size after duplicate", 7, bst.size());
        eq("inOrder is sorted", Arrays.asList(20, 30, 40, 50, 60, 70, 80), bst.inOrder());
        eq("preOrder", Arrays.asList(50, 30, 20, 40, 70, 60, 80), bst.preOrder());
        eq("postOrder", Arrays.asList(20, 40, 30, 60, 80, 70, 50), bst.postOrder());
        // Height counts edges: a single node is 0, this tree is 2.
        eq("height", 2, bst.height());
        eq("min", 20, bst.min());
        eq("max", 80, bst.max());
        check("contains present", bst.contains(40));
        check("contains absent", !bst.contains(99));
        check("delete leaf (20)", bst.delete(20));
        eq("inOrder after leaf delete", Arrays.asList(30, 40, 50, 60, 70, 80), bst.inOrder());
        check("delete one-child node (30)", bst.delete(30));
        eq("inOrder after one-child delete", Arrays.asList(40, 50, 60, 70, 80), bst.inOrder());
        check("delete two-child node (70)", bst.delete(70));
        eq("inOrder after two-child delete", Arrays.asList(40, 50, 60, 80), bst.inOrder());
        check("delete missing returns false", !bst.delete(99));
        BinarySearchTree<Integer> empty = new BinarySearchTree<>();
        throwsCheck("min on empty", IllegalStateException.class, empty::min);
        throwsCheck("max on empty", IllegalStateException.class, empty::max);
        throwsCheck("insert null", IllegalArgumentException.class, () -> bst.insert(null));
    }

    private static void testAvlTree() {
        section("AvlTree");
        AvlTree<Integer> avl = new AvlTree<>();
        check("starts empty", avl.isEmpty() && avl.size() == 0);
        // Ascending inserts would chain a plain BST; the AVL must stay balanced.
        for (int v = 1; v <= 7; v++) check("insert " + v, avl.insert(v));
        eq("inOrder is sorted", Arrays.asList(1, 2, 3, 4, 5, 6, 7), avl.inOrder());
        check("stays balanced", avl.isBalanced());
        // Height counts edges: a single node is 0, this tree is 2.
        eq("height of 7 balanced nodes", 2, avl.height());
        check("rotations happened", avl.getRotationCount() > 0);
        eq("min", 1, avl.min());
        eq("max", 7, avl.max());
        check("contains present", avl.contains(4));
        check("contains absent", !avl.contains(9));
        check("delete(4)", avl.delete(4));
        check("balanced after delete", avl.isBalanced());
        eq("inOrder after delete", Arrays.asList(1, 2, 3, 5, 6, 7), avl.inOrder());
        check("delete missing returns false", !avl.delete(9));
    }

    private static void testHeap() {
        section("Heap");
        Heap<Integer> max = Heap.maxHeap();
        check("starts empty", max.isEmpty() && max.size() == 0);
        for (int v : new int[]{5, 1, 4, 2, 3}) max.insert(v);
        check("max heap valid", max.isValid());
        eq("max peek", 5, max.peek());
        List<Integer> drain = new ArrayList<>();
        while (!max.isEmpty()) drain.add(max.extract());
        eq("max extract order", Arrays.asList(5, 4, 3, 2, 1), drain);
        Heap<Integer> min = Heap.minHeap();
        for (int v : new int[]{5, 1, 4, 2, 3}) min.insert(v);
        check("min heap valid", min.isValid());
        eq("min peek", 1, min.peek());
        drain.clear();
        while (!min.isEmpty()) drain.add(min.extract());
        eq("min extract order", Arrays.asList(1, 2, 3, 4, 5), drain);
        Heap<Integer> heapified = Heap.heapifyMax(new Integer[]{3, 1, 4, 1, 5, 9});
        check("heapifyMax valid", heapified.isValid());
        eq("heapifyMax peek", 9, heapified.peek());
        Heap<Integer> heapifiedMin = Heap.heapifyMin(new Integer[]{3, 1, 4, 1, 5, 9});
        check("heapifyMin valid", heapifiedMin.isValid());
        eq("heapifyMin peek", 1, heapifiedMin.peek());
        Heap<Integer> empty = Heap.maxHeap();
        throwsCheck("extract on empty", IllegalStateException.class, empty::extract);
        throwsCheck("peek on empty", IllegalStateException.class, empty::peek);
        Heap<Integer> mixed = Heap.maxHeap();
        for (int v : new int[]{5, 1, 4, 2, 3}) mixed.insert(v);
        check("contains present", mixed.contains(4));
        check("contains absent", !mixed.contains(99));
        throwsCheck("contains null", NullPointerException.class, () -> mixed.contains(null));
        mixed.clear();
        check("clear empties", mixed.isEmpty() && mixed.size() == 0);
        check("contains after clear", !mixed.contains(4));
        check("clear keeps heap valid", mixed.isValid());
        mixed.insert(7);
        eq("usable after clear", 7, mixed.peek());
        check("contains after re-insert", mixed.contains(7));
    }

    private static void testHashTable() {
        section("HashTable (separate chaining)");
        HashTable<String, Integer> table = new HashTable<>();
        check("starts empty", table.isEmpty() && table.size() == 0);
        eq("first put returns null", null, table.put("a", 1));
        eq("overwrite returns previous", 1, table.put("a", 2));
        eq("get after overwrite", 2, table.get("a"));
        table.put("b", 3);
        check("containsKey present", table.containsKey("b"));
        check("containsKey absent", !table.containsKey("z"));
        check("containsValue present", table.containsValue(3));
        check("containsValue absent", !table.containsValue(99));
        eq("remove returns value", 2, table.remove("a"));
        eq("get after remove", null, table.get("a"));
        check("remove missing returns null", table.remove("a") == null);
        // Rehash: 100 entries must all survive and grow the bucket array.
        int initialCapacity = table.capacity();
        for (int i = 0; i < 100; i++) table.put("k" + i, i);
        eq("size after 100 puts", 101, table.size());
        check("capacity grew past " + initialCapacity, table.capacity() > initialCapacity);
        boolean allFound = true;
        for (int i = 0; i < 100; i++) allFound &= table.get("k" + i) == i;
        check("all 100 entries retrievable after rehash", allFound);
        eq("keys() covers every entry", 101, table.keys().size());
    }

    private static void testOpenAddressingHashTable() {
        section("OpenAddressingHashTable (linear probing)");
        OpenAddressingHashTable<String, Integer> table = new OpenAddressingHashTable<>();
        check("starts empty", table.isEmpty() && table.size() == 0);
        eq("first put returns null", null, table.put("a", 1));
        eq("overwrite returns previous", 1, table.put("a", 2));
        eq("get after overwrite", 2, table.get("a"));
        table.put("b", 3);
        check("containsKey present", table.containsKey("b"));
        check("containsKey absent", !table.containsKey("z"));
        table.get("b");
        check("lastProbes counts the get", table.lastProbes() >= 1);
        eq("remove returns value", 2, table.remove("a"));
        eq("get after remove", null, table.get("a"));
        check("tombstone left behind", table.tombstoneCount() == 1);
        check("remove missing returns null", table.remove("a") == null);
        // Rehash: 100 entries must all survive and grow the slot array.
        int initialCapacity = table.capacity();
        for (int i = 0; i < 100; i++) table.put("k" + i, i);
        eq("size after 100 puts", 101, table.size());
        check("capacity grew past " + initialCapacity, table.capacity() > initialCapacity);
        boolean allFound = true;
        for (int i = 0; i < 100; i++) allFound &= table.get("k" + i) == i;
        check("all 100 entries retrievable after rehash", allFound);
        eq("keys() covers every entry", 101, table.keys().size());
    }

    private static void testSorting() {
        section("Sorting");
        Integer[] unsorted = {5, 2, 9, 1, 7, 3};
        List<Integer> sorted = Arrays.asList(1, 2, 3, 5, 7, 9);
        Integer[] a = unsorted.clone();
        Sorting.mergeSort(a);
        eq("mergeSort", sorted, Arrays.asList(a));
        check("mergeSort counted comparisons", Sorting.lastComparisons() > 0);
        a = unsorted.clone();
        Sorting.quickSort(a);
        eq("quickSort", sorted, Arrays.asList(a));
        a = unsorted.clone();
        Sorting.heapSort(a);
        eq("heapSort", sorted, Arrays.asList(a));
        a = unsorted.clone();
        Sorting.sort(a, Sorting.Algorithm.QUICK);
        eq("sort(QUICK)", sorted, Arrays.asList(a));
        a = unsorted.clone();
        Sorting.sort(a);
        eq("sort() defaults to merge", sorted, Arrays.asList(a));
        Integer[] dupes = {3, 1, 3, 2, 1};
        Sorting.mergeSort(dupes);
        eq("duplicates", Arrays.asList(1, 1, 2, 3, 3), Arrays.asList(dupes));
        Integer[] one = {42};
        Sorting.quickSort(one);
        eq("single element", Arrays.asList(42), Arrays.asList(one));
        Integer[] none = {};
        Sorting.mergeSort(none);
        eq("empty array", Arrays.asList(), Arrays.asList(none));
        Integer[] desc = unsorted.clone();
        Sorting.mergeSortDescending(desc);
        eq("mergeSortDescending", Arrays.asList(9, 7, 5, 3, 2, 1), Arrays.asList(desc));
        check("isSorted", Sorting.isSorted(new Integer[]{1, 2, 3}));
        check("isSorted rejects unsorted", !Sorting.isSorted(unsorted));
        check("isSortedDescending", Sorting.isSortedDescending(new Integer[]{3, 2, 1}));
        // sortedCopy must not touch the input.
        Integer[] original = unsorted.clone();
        Integer[] copy = Sorting.sortedCopy(original);
        eq("sortedCopy result", sorted, Arrays.asList(copy));
        eq("sortedCopy leaves input alone", Arrays.asList(unsorted), Arrays.asList(original));
        // Shuffling is deterministic for a fixed seed and keeps every element.
        Integer[] ten = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Integer[] shuffledA = Sorting.shuffledCopy(ten, 42L);
        Integer[] shuffledB = Sorting.shuffledCopy(ten, 42L);
        check("same seed shuffles the same way", Arrays.equals(shuffledA, shuffledB));
        Integer[] shuffledSorted = shuffledA.clone();
        Sorting.mergeSort(shuffledSorted);
        eq("shuffle keeps every element", Arrays.asList(ten), Arrays.asList(shuffledSorted));
        Sorting.Timing timing = Sorting.benchmark(Sorting.Algorithm.MERGE, unsorted.clone());
        check("benchmark reports its algorithm", timing.algorithm == Sorting.Algorithm.MERGE);
        check("benchmark nanos non-negative", timing.nanos >= 0);
        check("benchmark millis non-negative", timing.millis() >= 0);
    }

    private static void testHeapSort() {
        section("HeapSort");
        Integer[] unsorted = {5, 2, 9, 1, 7, 3};
        Integer[] a = unsorted.clone();
        HeapSort.sort(a);
        eq("sort", Arrays.asList(1, 2, 3, 5, 7, 9), Arrays.asList(a));
        check("counts comparisons", HeapSort.lastComparisons() > 0);
        check("counts swaps", HeapSort.lastSwaps() >= 0);
        a = unsorted.clone();
        HeapSort.sortDescending(a);
        eq("sortDescending", Arrays.asList(9, 7, 5, 3, 2, 1), Arrays.asList(a));
        check("isSorted", HeapSort.isSorted(new Integer[]{1, 2, 3}));
        check("isSorted rejects unsorted", !HeapSort.isSorted(unsorted));
        Integer[] original = unsorted.clone();
        Integer[] copy = HeapSort.sortedCopy(original);
        eq("sortedCopy result", Arrays.asList(1, 2, 3, 5, 7, 9), Arrays.asList(copy));
        eq("sortedCopy leaves input alone", Arrays.asList(unsorted), Arrays.asList(original));
    }

    private static void testBinarySearch() {
        section("BinarySearch");
        int[] sorted = {1, 3, 5, 7, 9, 11};
        eq("finds 7", 3, BinarySearch.binarySearch(sorted, 7));
        eq("missing returns -1", -1, BinarySearch.binarySearch(sorted, 4));
        eq("empty array", -1, BinarySearch.binarySearch(new int[]{}, 1));
        eq("range search finds 5", 2, BinarySearch.binarySearch(sorted, 5, 0, 3));
        eq("range search misses 7", -1, BinarySearch.binarySearch(sorted, 7, 0, 3));
        eq("generic search", 1, BinarySearch.binarySearch(new String[]{"a", "b", "c"}, "b"));
        check("counts comparisons", BinarySearch.lastComparisons() > 0);
        int[] dupes = {1, 2, 2, 2, 3};
        eq("firstOccurrence", 1, BinarySearch.firstOccurrence(dupes, 2));
        eq("lastOccurrence", 3, BinarySearch.lastOccurrence(dupes, 2));
        eq("firstOccurrence missing", -1, BinarySearch.firstOccurrence(dupes, 9));
        eq("lowerBound(4)", 2, BinarySearch.lowerBound(sorted, 4));
        eq("lowerBound exact", 2, BinarySearch.lowerBound(sorted, 5));
        eq("upperBound(2) skips duplicates", 4, BinarySearch.upperBound(dupes, 2));
        eq("upperBound(9)", 5, BinarySearch.upperBound(sorted, 9));
        int[] rotated = {7, 9, 11, 1, 3, 5};
        eq("searchRotated finds 3", 4, BinarySearch.searchRotated(rotated, 3));
        eq("searchRotated finds 9", 1, BinarySearch.searchRotated(rotated, 9));
        eq("searchRotated misses 8", -1, BinarySearch.searchRotated(rotated, 8));
        eq("findMinRotated", 1, BinarySearch.findMinRotated(rotated));
        eq("findMinRotated unrotated", 1, BinarySearch.findMinRotated(sorted));
    }

    private static void testGraph() {
        section("Graph");
        Graph<String> graph = new Graph<>();
        check("starts empty", graph.isEmpty() && graph.vertexCount() == 0);
        for (String v : new String[]{"A", "B", "C", "D"}) graph.addVertex(v);
        check("duplicate vertex refused", !graph.addVertex("A"));
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        eq("vertexCount", 4, graph.vertexCount());
        eq("edgeCount", 3, graph.edgeCount());
        check("hasVertex", graph.hasVertex("C"));
        check("hasEdge", graph.hasEdge("B", "C"));
        check("undirected edge is two-way", graph.hasEdge("C", "B"));
        eq("neighbors(B)", Arrays.asList("A", "C"), graph.neighbors("B"));
        eq("degree(B)", 2, graph.degree("B"));
        eq("bfs visits level by level", Arrays.asList("A", "B", "C", "D"), graph.bfs("A"));
        eq("dfs follows the chain", Arrays.asList("A", "B", "C", "D"), graph.dfs("A"));
        Map<String, Integer> distances = graph.bfsDistances("A");
        eq("bfs distance to D", 3, distances.get("D"));
        eq("bfs distance to A", 0, distances.get("A"));
        eq("bfsPath A->D", Arrays.asList("A", "B", "C", "D"), graph.bfsPath("A", "D"));
        eq("dfsPath A->D", Arrays.asList("A", "B", "C", "D"), graph.dfsPath("A", "D"));
        eq("bfsPath to self", Arrays.asList("A"), graph.bfsPath("A", "A"));
        check("chain has no cycle", !graph.hasCycle());
        graph.addEdge("D", "A");
        check("closing the loop makes a cycle", graph.hasCycle());
        check("removeEdge", graph.removeEdge("D", "A"));
        check("cycle gone after removeEdge", !graph.hasCycle());
        check("removeVertex", graph.removeVertex("D"));
        check("D is gone", !graph.hasVertex("D"));
        eq("vertexCount after remove", 3, graph.vertexCount());
        throwsCheck("bfs from unknown vertex", IllegalArgumentException.class,
                () -> graph.bfs("Z"));
        // A forest: two disconnected components are each walked in turn.
        Graph<String> forest = new Graph<>();
        forest.addEdge("A", "B");
        forest.addEdge("C", "D");
        eq("dfsForest covers both components",
                Arrays.asList("A", "B", "C", "D"), forest.dfsForest());
        check("forest has no cycle", !forest.hasCycle());
        // Directed edges go one way only.
        Graph<String> directed = new Graph<>(true);
        directed.addEdge("A", "B");
        check("directed edge exists", directed.hasEdge("A", "B"));
        check("directed edge has no reverse", !directed.hasEdge("B", "A"));
        eq("directed bfs", Arrays.asList("A", "B"), directed.bfs("A"));
        eq("directed bfs from B stops", Arrays.asList("B"), directed.bfs("B"));
        // Topological sort: every edge points forward in the order.
        Graph<String> dag = new Graph<>(true);
        dag.addEdge("Intro", "Data Structures");
        dag.addEdge("Intro", "Databases");
        dag.addEdge("Data Structures", "Algorithms");
        dag.addEdge("Databases", "Web Development");
        List<String> topo = dag.topologicalSort();
        eq("topo order", Arrays.asList("Intro", "Data Structures", "Databases",
                "Algorithms", "Web Development"), topo);
        check("topo respects every edge", topo.indexOf("Intro") < topo.indexOf("Algorithms")
                && topo.indexOf("Data Structures") < topo.indexOf("Algorithms")
                && topo.indexOf("Databases") < topo.indexOf("Web Development"));
        Graph<String> cyclicTopo = new Graph<>(true);
        cyclicTopo.addEdge("X", "Y");
        cyclicTopo.addEdge("Y", "Z");
        cyclicTopo.addEdge("Z", "X");
        eq("cyclic graph has no topo order", Arrays.asList(), cyclicTopo.topologicalSort());
        eq("undirected graph has no topo order", Arrays.asList(), graph.topologicalSort());
        eq("empty graph topo order", Arrays.asList(), new Graph<String>().topologicalSort());
    }

    private static void testWeightedGraph() {
        section("WeightedGraph (Dijkstra)");
        WeightedGraph<String> graph = new WeightedGraph<>();
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        graph.addEdge("A", "C", 10.0);
        graph.addEdge("C", "D", 1.0);
        graph.addVertex("E");
        Map<String, Double> distances = graph.dijkstraDistances("A");
        eq("distance to A", 0.0, distances.get("A"));
        eq("distance to B", 1.0, distances.get("B"));
        // A->B->C (3.0) beats the direct A->C edge (10.0).
        eq("distance to C takes the cheap route", 3.0, distances.get("C"));
        eq("distance to D", 4.0, distances.get("D"));
        check("unreachable E is absent, not infinite", !distances.containsKey("E"));
        eq("dijkstraPath A->D", Arrays.asList("A", "B", "C", "D"), graph.dijkstraPath("A", "D"));
        eq("dijkstraPath to unreachable is empty", Arrays.asList(), graph.dijkstraPath("A", "E"));
        eq("dijkstraPath to self", Arrays.asList("A"), graph.dijkstraPath("A", "A"));
        eq("weight(A, C)", 10.0, graph.weight("A", "C"));
        check("setWeight", graph.setWeight("A", "C", 2.0));
        eq("weight after setWeight", 2.0, graph.weight("A", "C"));
        eq("distance to C after setWeight", 2.0, graph.dijkstraDistances("A").get("C"));
        eq("distance to D after setWeight", 3.0, graph.dijkstraDistances("A").get("D"));
        eq("edgesFrom(A) covers both neighbours", 2, graph.edgesFrom("A").size());
        eq("edgeCount", 4, graph.edgeCount());
        check("removeEdge", graph.removeEdge("A", "C"));
        eq("distance to C falls back to A->B->C", 3.0, graph.dijkstraDistances("A").get("C"));
        throwsCheck("dijkstra from unknown vertex", IllegalArgumentException.class,
                () -> graph.dijkstraDistances("Z"));
    }
}
