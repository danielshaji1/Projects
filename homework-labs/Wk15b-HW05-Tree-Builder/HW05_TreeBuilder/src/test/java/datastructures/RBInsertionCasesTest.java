package datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RBInsertionCasesTest {

    private <T extends Comparable<T>> void assertRBProperties(RedBlackTree<T> tree) {
        RBNode<T> root = (RBNode<T>) tree.getRoot();
        assertNotNull(root);
        assertTrue(root.isBlack(), "Root must be black");
        assertNoRedRed(root);
        int blackHeight = blackHeight(root);
        assertBlackHeightConsistent(root, blackHeight);
    }

    private <T extends Comparable<T>> void assertNoRedRed(RBNode<T> node) {
        if (node == null || node.isNil()) return;
        RBNode<T> left = (RBNode<T>) node.getLeft();
        RBNode<T> right = (RBNode<T>) node.getRight();

        if (node.isRed()) {
            if (left != null && !left.isNil()) assertTrue(left.isBlack(), "Red node cannot have red left child");
            if (right != null && !right.isNil()) assertTrue(right.isBlack(), "Red node cannot have red right child");
        }

        assertNoRedRed(left);
        assertNoRedRed(right);
    }

    private <T extends Comparable<T>> int blackHeight(RBNode<T> node) {
        if (node == null || node.isNil()) return 1;
        int left = blackHeight((RBNode<T>) node.getLeft());
        return left + (node.isBlack() ? 1 : 0);
    }

    private <T extends Comparable<T>> void assertBlackHeightConsistent(RBNode<T> node, int expected) {
        if (node == null || node.isNil()) return;
        int left = blackHeight((RBNode<T>) node.getLeft());
        int right = blackHeight((RBNode<T>) node.getRight());
        assertEquals(left, right, "Black height must be same on both sides");
        assertBlackHeightConsistent((RBNode<T>) node.getLeft(), expected);
        assertBlackHeightConsistent((RBNode<T>) node.getRight(), expected);
    }

    @Test
    public void testIntegerInsertion() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        int[] values = {10, 20, 30, 15, 25, 5};
        for (int v : values) tree.insert(v);
        for (int v : values) assertTrue(tree.contains(v));
        assertRBProperties(tree);
    }

    @Test
    public void testDoubleInsertion() {
        RedBlackTree<Double> tree = new RedBlackTree<>();
        double[] values = {10.5, 20.2, 5.1, 15.0};
        for (double v : values) tree.insert(v);
        for (double v : values) assertTrue(tree.contains(v));
        assertRBProperties(tree);
    }

    @Test
    public void testStringInsertion() {
        RedBlackTree<String> tree = new RedBlackTree<>();
        String[] values = {"apple", "banana", "cherry", "date"};
        for (String v : values) tree.insert(v);
        for (String v : values) assertTrue(tree.contains(v));
        assertRBProperties(tree);
    }

    @Test
    public void testDuplicateInsertion() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(10); // duplicate
        tree.insert(10);
        assertTrue(tree.contains(10));
        assertRBProperties(tree);
    }

    @Test
    public void testEmptyTreeInsertion() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertFalse(tree.contains(5));
        tree.insert(5);
        assertTrue(tree.contains(5));
        assertRBProperties(tree);
    }
}
