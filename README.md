# dsa-java

**Classic data structures, implemented from scratch in Java** — no `java.util` shortcuts for the structures themselves. Clean generics, documented operations with complexity notes, and a runnable demo for every structure.

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/status-actively%20building-brightgreen?style=for-the-badge)

## Implemented

| Area | Structures |
|---|---|
| Lists | Singly Linked List (insert / delete / search / reverse), Doubly Linked List (forward / backward traversal) |
| Stacks | Array Stack (dynamic growth), Linked Stack (unbounded) |
| Queues | Circular Array Queue, Linked Queue (unbounded), Priority Queue (sorted linked list, FIFO within a priority) |
| Trees | Binary Search Tree (insert / search / delete, in-order / pre-order / post-order traversals), AVL Tree (self-balancing insert / delete, LL / RR / LR / RL rotations, balance check) |
| Heaps | Max/Min Heap (insert / extract / peek, O(n) heapify, heap-property check) |
| Sorting | Heap Sort (in-place, O(n log n) in every case, ascending / descending, sorted copy, comparison & swap counts) |

## Coming next

Hash Table (chaining) → Graph (adjacency list, BFS/DFS) → Sorting & searching utilities. Full plan: [ROADMAP.md](ROADMAP.md).

Companion project: **[AlgoViz](https://github.com/azhruu/AlgoViz)** animates these same structures in the browser.

## Run the demo

Zero dependencies, plain `javac`:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.azhruu.dsa.Demo
```

## Structure

```
src/main/java/com/azhruu/dsa/
├── list/    SinglyLinkedList, DoublyLinkedList …
├── stack/   ArrayStack, LinkedStack …
├── queue/   ArrayQueue, LinkedQueue, PriorityQueue …
├── tree/    BinarySearchTree, AvlTree …
├── heap/    Heap (max/min) …
├── sort/    HeapSort …
└── Demo.java
```

## License

MIT — see [LICENSE](LICENSE).
