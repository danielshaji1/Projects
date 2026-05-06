package datastructures;

/* ─────────────────────────────  TreeTestUtils.java  ─────────────────────────── */

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/** Helper routines shared by the AVL & Red-Black tree tests. */
public final class TreeTestUtils {

    private TreeTestUtils() {}                // static-only utility

    /* ---------- generic BST helpers ---------- */

    /** In-order traversal (null-safe). */

    /**
     * Public wrapper that returns the in‑order traversal as a list.
     * Sentinel (data == null) nodes are skipped.
     */
    public static <T extends Comparable<T>> List<T> inorder(TreeNode<T> root) {
        List<T> acc = new ArrayList<>();
        inorder(root, acc);
        return acc;
    }

    private static <T extends Comparable<T>> void inorder(TreeNode<T> n, List<T> acc) {
        if (n == null || n.getData() == null) return;   // stop at NIL
        inorder(n.getLeft(),  acc);
        acc.add(n.getData());
        inorder(n.getRight(), acc);
    }
    /** Verifies that the tree obeys BST ordering. */
    public static <T extends Comparable<T>> void assertIsBST(TreeNode<T> root) {
        List<T> vals = inorder(root);
        List<T> sorted = new ArrayList<>(vals);
        Collections.sort(sorted);
        assertEquals(sorted, vals, "in-order sequence not sorted ⇒ not a BST");
    }

    /* ---------- AVL helpers ---------- */

    /** Recursively checks AVL balance; returns subtree height. */
    public static <T extends Comparable<T>> int assertAVLBalanced(TreeNode<T> n) {
        if (n == null) return 0;
        int lh = assertAVLBalanced(n.getLeft());
        int rh = assertAVLBalanced(n.getRight());
        assertTrue(Math.abs(lh - rh) <= 1,
                () -> "AVL balance violated at node " + n.getData());
        return Math.max(lh, rh) + 1;
    }

    /* ---------- Red-Black helpers ---------- */

    public static <T extends Comparable<T>> void assertRedBlackProperties(TreeNode<T> root) {
        if (root == null) return;
        RBNode<T> rbRoot = (RBNode<T>) root;
        assertTrue(rbRoot.isBlack(), "root must be black");
        blackHeight(rbRoot);                  // recursion does the rest
    }
    /** Returns black-height while asserting all RB invariants. */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> int blackHeight(RBNode<T> n) {
        if (n == null || n.getData() == null) return 1;   // treat NIL / null as black leaf
        RBNode<T> L = (RBNode<T>) n.getLeft(), R = (RBNode<T>) n.getRight();

        if (n.isRed()) {                      // no red-red parent/child
            assertTrue(L == null || L.isBlack(), "red-red on left of " + n.getData());
            assertTrue(R == null || R.isBlack(), "red-red on right of " + n.getData());
        }

        int lb = blackHeight(L), rb = blackHeight(R);
        assertEquals(lb, rb, "black-height mismatch under " + n.getData());
        return lb + (n.isBlack() ? 1 : 0);
    }
    /* ---------- Convenience bulk helpers ---------- */

    /**
     * Inserts each value into the given tree.  Works for any implementation
     * of {@link TreeInterface}.
     */
    @SafeVarargs
    public static <T extends Comparable<T>>
    void bulkInsert(TreeInterface<T> tree, T... values) {
        for (T v : values) tree.insert(v);
    }

    /**
     * Deletes each value from the given tree.
     */
    @SafeVarargs
    public static <T extends Comparable<T>>
    void bulkDelete(TreeInterface<T> tree, T... values) {
        for (T v : values) tree.delete(v);
    }

    /* ---------- Random-data helper ---------- */

    /**
     * Generates an array of {@code size} random Integers using the given seed.
     * Handy for deterministic fuzz tests.
     */
    public static Integer[] randomInts(int size, long seed) {
        java.util.Random rng = new java.util.Random(seed);
        Integer[] out = new Integer[size];
        for (int i = 0; i < size; i++) out[i] = rng.nextInt();
        return out;
    }
}