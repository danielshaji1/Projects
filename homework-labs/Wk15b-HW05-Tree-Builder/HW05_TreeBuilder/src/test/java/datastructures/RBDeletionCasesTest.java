package datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RBDeletionCasesTest {

    private <T extends Comparable<T>> void assertRBProperties(RedBlackTree<T> tree) {
        RBNode<T> root = (RBNode<T>) tree.getRoot();
        if (root == null) return;
        if (!root.isNil()) assertTrue(root.isBlack(), "Root must be black");
        assertNoRedRed(root);
    }

    private <T extends Comparable<T>> void assertNoRedRed(RBNode<T> node) {
        if (node == null || node.isNil()) return;
        RBNode<T> left = (RBNode<T>) node.getLeft();
        RBNode<T> right = (RBNode<T>) node.getRight();

        if (node.isRed()) {
            if (left != null && !left.isNil()) assertTrue(left.isBlack());
            if (right != null && !right.isNil()) assertTrue(right.isBlack());
        }

        assertNoRedRed(left);
        assertNoRedRed(right);
    }

    @Test
    public void testDeleteLeafNode() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.delete(5);
        assertFalse(tree.contains(5));
        assertTrue(tree.contains(10));
        assertTrue(tree.contains(15));
        assertRBProperties(tree);
    }

    @Test
    public void testDeleteNodeWithOneChild() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.insert(5);
        tree.insert(7);
        tree.delete(5);
        assertFalse(tree.contains(5));
        assertTrue(tree.contains(7));
        assertRBProperties(tree);
    }

    @Test
    public void testDeleteNodeWithTwoChildrenSuccessor() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.setUsePredecessorForDelete(false);
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(12);
        tree.insert(18);
        tree.delete(10);
        assertFalse(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(12));
        assertRBProperties(tree);
    }

    @Test
    public void testDeleteNodeWithTwoChildrenPredecessor() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.setUsePredecessorForDelete(true);
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(12);
        tree.insert(18);
        tree.delete(10);
        assertFalse(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(12));
        assertRBProperties(tree);
    }

    @Test
    public void testDeleteRootSingleNode() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.delete(10);
        assertFalse(tree.contains(10));
        RBNode<Integer> root = (RBNode<Integer>) tree.getRoot();
        assertTrue(root.isNil());
    }

    @Test
    public void testDeleteNonExistent() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(10);
        tree.delete(99); // should do nothing
        assertTrue(tree.contains(10));
        assertRBProperties(tree);
    }
}
