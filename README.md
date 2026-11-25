# dsa-java

**Classic data structures, implemented from scratch in Java** — no `java.util` shortcuts for the structures themselves. Clean generics, documented operations with complexity notes, and a runnable demo for every structure.

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/status-actively%20building-brightgreen?style=for-the-badge)

## Implemented

| Area | Structures |
|---|---|
| Lists | Singly Linked List (insert / delete / search / reverse, removeFirst / removeLast, clear), Doubly Linked List (forward / backward traversal, clear) |
| Stacks | Array Stack (dynamic growth, clear), Linked Stack (unbounded, clear) |
| Queues | Circular Array Queue (clear), Linked Queue (unbounded, clear), Priority Queue (sorted linked list, FIFO within a priority, clear) |
| Trees | Binary Search Tree (insert / search / delete, in-order / pre-order / post-order traversals, clear), AVL Tree (self-balancing insert / delete, LL / RR / LR / RL rotations, balance check, clear + rotation-count reset) |
| Heaps | Max/Min Heap (insert / extract / peek, contains, clear, O(n) heapify, heap-property check) |
| Hashing | Hash Table with separate chaining (put / get / remove, collision chains, auto-rehash at 0.75 load), Open-Addressing Hash Table with linear probing (probe sequences, tombstone deletes, probe counts, auto-rehash at 0.7 load) |
| Sorting | Heap Sort (in-place, O(n log n) in every case, ascending / descending, sorted copy, comparison & swap counts), Merge Sort (stable, O(n log n), O(n) extra space), Quick Sort (median-of-three pivot, insertion-sort finish, O(n log n) average), Sorting utilities (one API for merge / quick / heap, sorted copy, deterministic shuffle, timed benchmark) |
| Searching | Binary search utilities (binary search on int[] and Comparables, range search, first / last occurrence, lower / upper bound, rotated-array search, rotated minimum, comparison counts) |
| Graphs | Graph (adjacency list, directed / undirected, BFS visit order, shortest-path distances & paths, DFS visit order & paths, DFS forest, cycle detection, topological sort, connected components, clear), Weighted Graph (Dijkstra shortest-path distances & paths, non-negative weights, min-heap priority queue, clear) |

## Complexity cheat-sheet

Every structure and algorithm in this repo, at a glance. `n` is the
number of elements, `V` vertices, `E` edges.

### Data structures

| Structure | Access | Search | Insert | Delete | Space |
|---|---|---|---|---|---|
| Singly Linked List | O(n) | O(n) | O(1) head · O(n) tail / index | O(1) head · O(n) value / index | O(n) |
| Doubly Linked List | O(n) | O(n) | O(1) head / tail · O(n) index | O(1) head / tail · O(n) value / index | O(n) |
| Array Stack | — (top only) | — | O(1) amortized push | O(1) amortized pop | O(n) |
| Linked Stack | — (top only) | — | O(1) push | O(1) pop | O(n) |
| Circular Array Queue | — (ends only) | — | O(1) enqueue | O(1) dequeue | O(n) |
| Linked Queue | — (ends only) | — | O(1) enqueue | O(1) dequeue | O(n) |
| Priority Queue (sorted list) | — | O(n) | O(n) enqueue | O(1) dequeue | O(n) |
| Binary Search Tree | O(log n) avg · O(n) worst | O(log n) avg · O(n) worst | O(log n) avg · O(n) worst | O(log n) avg · O(n) worst | O(n) |
| AVL Tree | O(log n) | O(log n) | O(log n) | O(log n) | O(n) |
| Max/Min Heap | O(1) peek | O(n) | O(log n) insert | O(log n) extract | O(n) |
| Hash Table (separate chaining) | — | O(1) avg · O(n) worst get | O(1) avg · O(n) worst put | O(1) avg · O(n) worst remove | O(n) |
| Open-Addressing Hash Table | — | O(1) avg · O(n) worst get | O(1) avg · O(n) worst put | O(1) avg · O(n) worst remove | O(n) |

Notes: the heap also builds in O(n) (`heapify`). Both hash tables
rehash automatically (chaining at 0.75 load, open addressing at 0.7),
which is what keeps the average case at O(1). A plain BST degrades to
O(n) on sorted input — that is exactly what the AVL tree's rotations
prevent.

### Sorting & searching

| Algorithm | Best | Average | Worst | Space | Notes |
|---|---|---|---|---|---|
| Merge Sort | O(n log n) | O(n log n) | O(n log n) | O(n) | Stable |
| Quick Sort | O(n log n) | O(n log n) | O(n²) | O(log n) | Median-of-three pivot, insertion-sort finish |
| Heap Sort | O(n log n) | O(n log n) | O(n log n) | O(1) | In-place |
| Binary Search | O(1) | O(log n) | O(log n) | O(1) | Sorted input; rotated-array search is also O(log n) |

### Graphs

| Algorithm | Time | Space | Notes |
|---|---|---|---|
| BFS | O(V + E) | O(V) | Visit order, shortest path on unweighted graphs |
| DFS | O(V + E) | O(V) | Visit order, cycle detection |
| Topological sort | O(V + E) | O(V) | Kahn's algorithm; empty list when a cycle makes ordering impossible |
| Connected components | O(V + E) | O(V) | One list per separate piece; isolated vertices are pieces of one |
| Dijkstra | O((V + E) log V) | O(V) | Non-negative weights, min-heap priority queue |

## Coming next

Sorting utilities ✓ → Binary search utilities ✓ → Unit-style assertion tests ✓ → Complexity cheat-sheet in README ✓. Full plan: [ROADMAP.md](ROADMAP.md).

Companion project: **[AlgoViz](https://github.com/azhruu/AlgoViz)** animates these same structures in the browser.

## Run the demo

Zero dependencies, plain `javac`:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.azhruu.dsa.Demo
```

## Run the tests

Unit-style assertion tests for every structure — no framework, no
dependencies, just a `main` that checks and reports. Exit code 0
means all 304 checks passed.

```bash
javac -d out $(find src -name "*.java")
java -cp out com.azhruu.dsa.Tests
```

## Structure

```
src/main/java/com/azhruu/dsa/
├── list/    SinglyLinkedList, DoublyLinkedList …
├── stack/   ArrayStack, LinkedStack …
├── queue/   ArrayQueue, LinkedQueue, PriorityQueue …
├── tree/    BinarySearchTree, AvlTree …
├── heap/    Heap (max/min) …
├── hash/    HashTable (separate chaining), OpenAddressingHashTable (linear probing) …
├── sort/    HeapSort, Sorting (merge / quick / heap + benchmark) …
├── graph/   Graph (adjacency list, BFS, DFS, cycle detection, topological sort, connected components), WeightedGraph (Dijkstra) …
└── Demo.java
src/test/java/com/azhruu/dsa/
└── Tests.java    # assertion tests for every structure above
```

## License

MIT — see [LICENSE](LICENSE).
