package com.azhruu.dsa.graph;

import com.azhruu.dsa.heap.Heap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Weighted graph stored as an adjacency list: every vertex maps to the
 * list of outgoing edges, each carrying a non-negative weight, in the
 * order the edges were added. Vertices are kept in insertion order (a
 * {@code LinkedHashMap}), so traversals are deterministic.
 *
 * <p>Undirected by default: {@code addEdge(a, b, w)} records the edge
 * in both directions with the same weight. Pass {@code directed = true}
 * for a directed graph. Adding an edge auto-adds any missing endpoint
 * as a vertex. A duplicate edge is ignored; use {@code setWeight} to
 * change an existing edge's weight.
 *
 * <p>Dijkstra's algorithm is O((V + E) log V): every vertex is settled
 * once and every edge is relaxed once, with the project's own min-heap
 * ({@code Heap}) as the priority queue. It finds the cheapest path
 * from the source to every reachable vertex, which BFS cannot do once
 * edges have different weights. Weights must be non-negative --
 * Dijkstra's greedy settling is only correct when no edge can make a
 * settled vertex cheaper afterwards, so a negative weight is rejected.
 */
public class WeightedGraph<T> {

    private final boolean directed;
    private final Map<T, List<Edge<T>>> adjacency;
    private int edgeCount;

    /** An undirected weighted graph. */
    public WeightedGraph() {
        this(false);
    }

    /** A directed weighted graph when {@code directed} is true, undirected otherwise. */
    public WeightedGraph(boolean directed) {
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
     * Add a weighted edge from {@code from} to {@code to}, auto-adding
     * any missing endpoint. A duplicate edge is ignored (no parallel
     * edges). In an undirected graph the reverse edge is added as well.
     * Weights must be non-negative; a negative weight is rejected
     * because Dijkstra's algorithm cannot handle it.
     */
    public void addEdge(T from, T to, double weight) {
        if (from == null || to == null)
            throw new IllegalArgumentException("null vertices are not allowed");
        if (weight < 0 || Double.isNaN(weight))
            throw new IllegalArgumentException("weights must be non-negative, got " + weight);
        addVertex(from);
        addVertex(to);
        if (hasEdge(from, to)) return; // already an edge
        adjacency.get(from).add(new Edge<>(to, weight));
        // A self-loop is stored once; an undirected edge gets its reverse.
        if (!directed && !from.equals(to)) adjacency.get(to).add(new Edge<>(from, weight));
        edgeCount++;
    }

    /**
     * Change the weight of an existing edge (both directions in an
     * undirected graph); returns false if there was no such edge.
     */
    public boolean setWeight(T from, T to, double weight) {
        if (from == null || to == null || !adjacency.containsKey(from)) return false;
        if (weight < 0 || Double.isNaN(weight))
            throw new IllegalArgumentException("weights must be non-negative, got " + weight);
        boolean changed = false;
        for (Edge<T> edge : adjacency.get(from)) {
            if (edge.target.equals(to)) {
                edge.weight = weight;
                changed = true;
            }
        }
        if (changed && !directed) {
            for (Edge<T> edge : adjacency.get(to)) {
                if (edge.target.equals(from)) edge.weight = weight;
            }
        }
        return changed;
    }

    /** Remove the edge from {@code from} to {@code to}; false if there was none. */
    public boolean removeEdge(T from, T to) {
        if (from == null || to == null || !adjacency.containsKey(from)) return false;
        boolean removed = adjacency.get(from).removeIf(e -> e.target.equals(to));
        if (removed && !directed) adjacency.get(to).removeIf(e -> e.target.equals(from));
        if (removed) edgeCount--;
        return removed;
    }

    /** Remove a vertex and every edge touching it; false if it was absent. */
    public boolean removeVertex(T vertex) {
        if (vertex == null || !adjacency.containsKey(vertex)) return false;
        List<Edge<T>> outgoing = adjacency.remove(vertex);
        edgeCount -= outgoing.size();
        if (directed) {
            // Incoming edges are only stored on the other endpoint.
            for (List<Edge<T>> edges : adjacency.values()) {
                if (edges.removeIf(e -> e.target.equals(vertex))) edgeCount--;
            }
        } else {
            // Undirected: the reverse entries are the same edges already
            // subtracted above, so just drop the stale references.
            for (List<Edge<T>> edges : adjacency.values()) {
                edges.removeIf(e -> e.target.equals(vertex));
            }
        }
        return true;
    }

    /** True if the vertex is in the graph. */
    public boolean hasVertex(T vertex) {
        return vertex != null && adjacency.containsKey(vertex);
    }

    /** True if there is an edge from {@code from} to {@code to}. */
    public boolean hasEdge(T from, T to) {
        if (from == null || to == null || !adjacency.containsKey(from)) return false;
        for (Edge<T> edge : adjacency.get(from)) {
            if (edge.target.equals(to)) return true;
        }
        return false;
    }

    /** The weight of the edge from {@code from} to {@code to}; NaN if there is none. */
    public double weight(T from, T to) {
        if (from == null || to == null || !adjacency.containsKey(from)) return Double.NaN;
        for (Edge<T> edge : adjacency.get(from)) {
            if (edge.target.equals(to)) return edge.weight;
        }
        return Double.NaN;
    }

    /** The outgoing edges of a vertex, in edge-add order. A copy - edits do not affect the graph. */
    public List<Edge<T>> edgesFrom(T vertex) {
        if (vertex == null || !adjacency.containsKey(vertex))
            throw new IllegalArgumentException("unknown vertex: " + vertex);
        return new ArrayList<>(adjacency.get(vertex));
    }

    /** The neighbours of a vertex, in edge-add order. A copy - edits do not affect the graph. */
    public List<T> neighbors(T vertex) {
        List<T> result = new ArrayList<>();
        for (Edge<T> edge : edgesFrom(vertex)) result.add(edge.target);
        return result;
    }

    /** Out-degree of a vertex (in an undirected graph, its degree). */
    public int degree(T vertex) {
        return edgesFrom(vertex).size();
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
     * Cheapest-path distances from {@code source}, by Dijkstra's
     * algorithm. The map holds only the vertices reachable from the
     * source; the source itself maps to 0. Unreachable vertices are
     * absent, not infinite.
     */
    public Map<T, Double> dijkstraDistances(T source) {
        return new HashMap<>(dijkstra(source).distances);
    }

    /**
     * The cheapest path from {@code source} to {@code goal}, or an
     * empty list when no path exists. The endpoints are included.
     * Equal-cost paths are not guaranteed to be a particular one --
     * ties break on heap order.
     */
    public List<T> dijkstraPath(T source, T goal) {
        if (goal == null || !adjacency.containsKey(goal))
            throw new IllegalArgumentException("unknown vertex: " + goal);
        DijkstraResult<T> result = dijkstra(source);
        if (!result.parent.containsKey(goal)) return new ArrayList<>();
        List<T> path = new ArrayList<>();
        T current = goal;
        while (current != null) {
            path.add(0, current);
            current = result.parent.get(current);
        }
        return path;
    }

    /**
     * One Dijkstra pass from {@code source}, keeping each reachable
     * vertex's cheapest distance and the parent it was reached by.
     * The priority queue is this project's own min-heap: a vertex
     * enters the heap every time a cheaper route to it is found, so
     * stale entries (a distance worse than the one already recorded)
     * are skipped when extracted. A vertex is settled the first time
     * it is extracted with its recorded distance, and because all
     * weights are non-negative no later route can beat it.
     */
    private DijkstraResult<T> dijkstra(T source) {
        if (source == null || !adjacency.containsKey(source))
            throw new IllegalArgumentException("unknown vertex: " + source);
        Map<T, Double> distances = new HashMap<>();
        Map<T, T> parent = new HashMap<>();
        Heap<DistanceEntry<T>> heap = Heap.minHeap();
        distances.put(source, 0.0);
        parent.put(source, null);
        heap.insert(new DistanceEntry<>(source, 0.0));
        while (!heap.isEmpty()) {
            DistanceEntry<T> entry = heap.extract();
            // A stale entry: a cheaper route to this vertex was found
            // after this one was queued, so it is already settled.
            if (entry.distance > distances.get(entry.vertex)) continue;
            for (Edge<T> edge : adjacency.get(entry.vertex)) {
                double via = entry.distance + edge.weight;
                Double known = distances.get(edge.target);
                if (known == null || via < known) {
                    distances.put(edge.target, via);
                    parent.put(edge.target, entry.vertex);
                    heap.insert(new DistanceEntry<>(edge.target, via));
                }
            }
        }
        return new DijkstraResult<>(distances, parent);
    }

    /** One weighted edge: the vertex it points at, and its cost. */
    public static class Edge<T> {
        public final T target;
        public double weight;
        public Edge(T target, double weight) {
            this.target = target;
            this.weight = weight;
        }
        @Override
        public String toString() {
            return target + "(" + weight + ")";
        }
    }

    /** One heap entry: a vertex plus the distance it was queued with. */
    private static class DistanceEntry<T> implements Comparable<DistanceEntry<T>> {
        final T vertex;
        final double distance;
        DistanceEntry(T vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
        @Override
        public int compareTo(DistanceEntry<T> other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    private static class DijkstraResult<T> {
        final Map<T, Double> distances;
        final Map<T, T> parent;
        DijkstraResult(Map<T, Double> distances, Map<T, T> parent) {
            this.distances = distances;
            this.parent = parent;
        }
    }

    /** One line per vertex: {@code A -> [B(4.0), C(2.0)]}. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<T, List<Edge<T>>> entry : adjacency.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
