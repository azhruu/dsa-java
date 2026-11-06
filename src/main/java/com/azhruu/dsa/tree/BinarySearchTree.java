package com.azhruu.dsa.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree: keys are ordered, so an in-order walk visits them
 * in sorted order. Duplicate keys are ignored (insert returns false).
 *
 * <p>Insert / search / delete are O(h) where h is the tree height -
 * O(log n) on a balanced tree, O(n) when keys arrive already sorted.
 * Traversals are O(n). The tree itself is built from plain nodes;
 * {@code java.util.List} is used only to hand traversal results back.
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private static class Node<T> {
        T value;
        Node<T> left, right;
        Node(T value) { this.value = value; }
    }

    private Node<T> root;
    private int size;

    /** Insert a value; returns false if it is already present. */
    public boolean insert(T value) {
        if (value == null) throw new IllegalArgumentException("null keys are not allowed");
        if (contains(value)) return false;
        root = insert(root, value);
        size++;
        return true;
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) return new Node<>(value);
        int cmp = value.compareTo(node.value);
        if (cmp < 0) node.left = insert(node.left, value);
        else node.right = insert(node.right, value);
        return node;
    }

    /** True if the value is in the tree. */
    public boolean contains(T value) {
        if (value == null) return false;
        Node<T> cur = root;
        while (cur != null) {
            int cmp = value.compareTo(cur.value);
            if (cmp == 0) return true;
            cur = cmp < 0 ? cur.left : cur.right;
        }
        return false;
    }

    /** Delete a value; returns false if it was not present. */
    public boolean delete(T value) {
        if (value == null || !contains(value)) return false;
        root = delete(root, value);
        size--;
        return true;
    }

    private Node<T> delete(Node<T> node, T value) {
        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = delete(node.left, value);
        } else if (cmp > 0) {
            node.right = delete(node.right, value);
        } else {
            // Node found: leaf, one child, or two children.
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Two children: replace with the in-order successor
            // (smallest key in the right subtree), then delete it there.
            node.value = min(node.right);
            node.right = delete(node.right, node.value);
        }
        return node;
    }

    /** Smallest key in the tree. */
    public T min() {
        if (isEmpty()) throw new IllegalStateException("tree is empty");
        return min(root);
    }

    private T min(Node<T> node) {
        while (node.left != null) node = node.left;
        return node.value;
    }

    /** Largest key in the tree. */
    public T max() {
        if (isEmpty()) throw new IllegalStateException("tree is empty");
        Node<T> cur = root;
        while (cur.right != null) cur = cur.right;
        return cur.value;
    }

    /** Keys in sorted (ascending) order. */
    public List<T> inOrder() {
        List<T> out = new ArrayList<>();
        inOrder(root, out);
        return out;
    }

    private void inOrder(Node<T> node, List<T> out) {
        if (node == null) return;
        inOrder(node.left, out);
        out.add(node.value);
        inOrder(node.right, out);
    }

    /** Root first, then left subtree, then right subtree. */
    public List<T> preOrder() {
        List<T> out = new ArrayList<>();
        preOrder(root, out);
        return out;
    }

    private void preOrder(Node<T> node, List<T> out) {
        if (node == null) return;
        out.add(node.value);
        preOrder(node.left, out);
        preOrder(node.right, out);
    }

    /** Children before their parent - useful for safe bottom-up deletion. */
    public List<T> postOrder() {
        List<T> out = new ArrayList<>();
        postOrder(root, out);
        return out;
    }

    private void postOrder(Node<T> node, List<T> out) {
        if (node == null) return;
        postOrder(node.left, out);
        postOrder(node.right, out);
        out.add(node.value);
    }

    /** Height in edges: empty tree is -1, a single node is 0. */
    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        return inOrder().toString();
    }
}
