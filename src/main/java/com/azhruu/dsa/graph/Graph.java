package com.azhruu.dsa.graph;

import com.azhruu.dsa.queue.ArrayQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph stored as an adjacency list: every vertex maps to the list of
 * vertices it points at, in the order the edges were added. Vertices
 * are kept in insertion order (a {@code LinkedHashMap}), so traversals
 * are deterministic - the same graph always yields the same BFS order.
 *
 * <p>Undirected by default: {@code addEdge(a, b)} records the edge in
 * both directions. Pass {@code directed = true} for a directed graph.
 * Adding an edge auto-adds any missing endpoint as a vertex.
 *
 * <p>Breadth-first search is O(V + E): every vertex is dequeued once
 * and every edge is followed once. It visits vertices level by level,
 * so the first time a vertex is reached is via a shortest path (fewest
 * edges). The BFS work queue is this project's own {@code ArrayQueue};
 * maps and sets are only the bookkeeping around it.
 */
public class Graph<T> {

    private final boolean directed;
    private final Map<T, List<T>> adjacency;
    private int edgeCount;

    /** An undirected graph. */
    public Graph() {
        this(false);
    }

    /** A directed graph when {@code directed} is true, undirected otherwise. */
    public Graph(boolean directed) {
        this.directed = directed;
        this.adjacency = new LinkedHashMap<>();
        this.edgeCount = 0;
    }

    /** Add a vertex; returns false if it is already present. */
    public boolean addVertex(T vertex) {
        if (vertex == null) throw new IllegalArgumentException("null vertices are not allowed");
        if (adjacency.containsKey(vertex)) return false;
        adjacency.put(vertex, new ArrayList<>());
        return true;
    }

    /**
     * Add an edge from {@code from} to {@code to}, auto-adding any
     * missing endpoint. A duplicate edge is ignored (no parallel edges).
     * In an undirected graph the reverse edge is added as well.
     */
    public void addEdge(T from, T to) {
        if (from == null || to == null)
            throw new IllegalArgumentException("null vertices are not allowed");
        addVertex(from);
        addVertex(to);
        if (adjacency.get(from).contains(to)) return; // already an edge
        adjacency.get(from).add(to);
        // A self-loop is stored once; an undirected edge gets its reverse.
        if (!directed && !from.equals(to)) adjacency.get(to).add(from);
        edgeCount++;
    }

    /** Remove the edge from {@code from} to {@code to}; false if there was none. */
    public boolean removeEdge(T from, T to) {
        if (from == null || to == null || !adjacency.containsKey(from)) return false;
        boolean removed = adjacency.get(from).remove(to);
        if (removed && !directed) adjacency.get(to).remove(from);
        if (removed) edgeCount--;
        return removed;
    }

    /** Remove a vertex and every edge touching it; false if it was absent. */
    public boolean removeVertex(T vertex) {
        if (vertex == null || !adjacency.containsKey(vertex)) return false;
        List<T> outgoing = adjacency.remove(vertex);
        edgeCount -= outgoing.size();
        if (directed) {
            // Incoming edges are only stored on the other endpoint.
            for (List<T> neighbours : adjacency.values()) {
                if (neighbours.remove(vertex)) edgeCount--;
            }
        } else {
            // Undirected: the reverse entries are the same edges already
            // subtracted above, so just drop the stale references.
            for (List<T> neighbours : adjacency.values()) neighbours.remove(vertex);
        }
        return true;
    }

    /** True if the vertex is in the graph. */
    public boolean hasVertex(T vertex) {
        return vertex != null && adjacency.containsKey(vertex);
    }

    /** True if there is an edge from {@code from} to {@code to}. */
    public boolean hasEdge(T from, T to) {
        return from != null && to != null
                && adjacency.containsKey(from)
                && adjacency.get(from).contains(to);
    }

    /** The neighbours of a vertex, in edge-add order. A copy - edits do not affect the graph. */
    public List<T> neighbors(T vertex) {
        if (vertex == null || !adjacency.containsKey(vertex))
            throw new IllegalArgumentException("unknown vertex: " + vertex);
        return new ArrayList<>(adjacency.get(vertex));
    }

    /** Out-degree of a vertex (in an undirected graph, its degree). */
    public int degree(T vertex) {
        return neighbors(vertex).size();
    }

    /** All vertices, in the order they were added. */
    public List<T> vertices() {
        return new ArrayList<>(adjacency.keySet());
    }

    public int vertexCount() { return adjacency.size(); }
    public int edgeCount() { return edgeCount; }
    public boolean isDirected() { return directed; }
    public boolean isEmpty() { return adjacency.isEmpty(); }

    /**
     * Breadth-first traversal from {@code start}: vertices in the order
     * they are first reached, level by level. O(V + E).
     */
    public List<T> bfs(T start) {
        return bfsWithParents(start).order;
    }

    /**
     * Shortest-path distances from {@code start}, in edges. The map
     * holds only the vertices reachable from {@code start}; the start
     * itself maps to 0. Unreachable vertices are absent, not -1.
     */
    public Map<T, Integer> bfsDistances(T start) {
        return new HashMap<>(bfsWithParents(start).distances);
    }

    /**
     * A shortest path (fewest edges) from {@code start} to {@code goal},
     * or an empty list when no path exists. The endpoints are included.
     */
    public List<T> bfsPath(T start, T goal) {
        if (goal == null || !adjacency.containsKey(goal))
            throw new IllegalArgumentException("unknown vertex: " + goal);
        BfsResult<T> result = bfsWithParents(start);
        if (!result.parent.containsKey(goal)) return new ArrayList<>();
        List<T> path = new ArrayList<>();
        T current = goal;
        while (current != null) {
            path.add(0, current);
            current = result.parent.get(current);
        }
        return path;
    }

    /** One BFS pass, keeping the visit order, each vertex's parent, and its distance. */
    private BfsResult<T> bfsWithParents(T start) {
        if (start == null || !adjacency.containsKey(start))
            throw new IllegalArgumentException("unknown vertex: " + start);
        List<T> order = new ArrayList<>();
        Map<T, T> parent = new HashMap<>();
        Map<T, Integer> distances = new HashMap<>();
        Set<T> visited = new HashSet<>();
        ArrayQueue<T> queue = new ArrayQueue<>();
        visited.add(start);
        parent.put(start, null);
        distances.put(start, 0);
        queue.enqueue(start);
        while (!queue.isEmpty()) {
            T current = queue.dequeue();
            order.add(current);
            for (T neighbour : adjacency.get(current)) {
                if (visited.add(neighbour)) {
                    parent.put(neighbour, current);
                    distances.put(neighbour, distances.get(current) + 1);
                    queue.enqueue(neighbour);
                }
            }
        }
        return new BfsResult<>(order, parent, distances);
    }

    private static class BfsResult<T> {
        final List<T> order;
        final Map<T, T> parent;
        final Map<T, Integer> distances;
        BfsResult(List<T> order, Map<T, T> parent, Map<T, Integer> distances) {
            this.order = order;
            this.parent = parent;
            this.distances = distances;
        }
    }

    /** One line per vertex: {@code A -> [B, C]}. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<T, List<T>> entry : adjacency.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
