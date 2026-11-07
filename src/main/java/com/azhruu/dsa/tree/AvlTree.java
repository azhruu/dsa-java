package com.azhruu.dsa.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * AVL Tree: a self-balancing binary search tree. After every insert or
 * delete the height difference between a node's two subtrees (its balance
 * factor) is kept within [-1, 1] by rotating the tree - a single rotation
 * fixes a left-left or right-right imbalance, a double rotation fixes a
 * left-right or right-left one.
 *
 * <p>Insert / search / delete are O(log n) because the height is always
 * O(log n), even when keys arrive already sorted - the case where a plain
 * binary search tree degrades to a O(n) linked list. Traversals are O(n).
 * Duplicate keys are ignored (insert returns false).
 */
public class AvlTree<T extends Comparable<T>> {

    private static class Node<T> {
        T value;
        Node<T> left, right;
        int height; // height in edges: leaf is 0
        Node(T value) {
            this.value = value;
            this.height = 0;
        }
    }

    private Node<T> root;
    private int size;
    private int rotations; // total rotations performed (single + double count as 1 each)

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
        return rebalance(node);
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
        return rebalance(node);
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
        return node == null ? -1 : node.height;
    }

    /** Balance factor of a node: left height minus right height. */
    private int balanceFactor(Node<T> node) {
        return height(node.left) - height(node.right);
    }

    private void updateHeight(Node<T> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Restore the AVL invariant at this node after an insert or delete:
     * if the balance factor is outside [-1, 1], rotate once (LL / RR) or
     * twice (LR / RL) so the subtree is balanced again.
     */
    private Node<T> rebalance(Node<T> node) {
        updateHeight(node);
        int balance = balanceFactor(node);
        if (balance > 1) {
            // Left-heavy.
            if (balanceFactor(node.left) < 0) node.left = rotateLeft(node.left); // LR case
            rotations++;
            return rotateRight(node); // LL case
        }
        if (balance < -1) {
            // Right-heavy.
            if (balanceFactor(node.right) > 0) node.right = rotateRight(node.right); // RL case
            rotations++;
            return rotateLeft(node); // RR case
        }
        return node;
    }

    /** Right rotation: the left child becomes the new subtree root. */
    private Node<T> rotateRight(Node<T> y) {
        Node<T> x = y.left;
        Node<T> middle = x.right;
        x.right = y;
        y.left = middle;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    /** Left rotation: the right child becomes the new subtree root. */
    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        Node<T> middle = y.left;
        y.left = x;
        x.right = middle;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    /**
     * True if every node in the tree has a balance factor within [-1, 1].
     * Handy for asserting the self-balancing invariant after bulk inserts.
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node<T> node) {
        if (node == null) return true;
        int balance = balanceFactor(node);
        return balance >= -1 && balance <= 1
                && isBalanced(node.left) && isBalanced(node.right);
    }

    /** Total rotations performed so far (single and double count as 1 each). */
    public int getRotationCount() { return rotations; }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    @Override
    public String toString() {
        return inOrder().toString();
    }
}
