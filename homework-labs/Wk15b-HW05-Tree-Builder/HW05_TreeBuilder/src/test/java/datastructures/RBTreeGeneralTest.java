package datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RBTreeGeneralTest {

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
    public void testEmptyTree() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertFalse(tree.contains(5));
        assertRBProperties(tree);
    }

    @Test
    public void testSingleNode() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(42);
        assertTrue(tree.contains(42));
        assertRBProperties(tree);
    }


    @Test
    public void testTraversalConsistency() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        int[] vals = {10, 5, 15, 3, 7, 12, 18};
        for (int v : vals) tree.insert(v);

        List<Integer> inorder = new ArrayList<>();
        traverseInorder((RBNode<Integer>) tree.getRoot(), inorder);
        int[] sortedVals = vals.clone();
        java.util.Arrays.sort(sortedVals);

        assertArrayEquals(sortedVals, inorder.stream().mapToInt(Integer::intValue).toArray());
        assertRBProperties(tree);
    }

    private void traverseInorder(RBNode<Integer> node, List<Integer> list) {
        if (node == null || node.isNil()) return;
        traverseInorder((RBNode<Integer>) node.getLeft(), list);
        list.add(node.getData());
        traverseInorder((RBNode<Integer>) node.getRight(), list);
    }
}
