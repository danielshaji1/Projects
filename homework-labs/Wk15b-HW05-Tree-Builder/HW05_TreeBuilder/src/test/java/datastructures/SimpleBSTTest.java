package datastructures;

import datastructures.SimpleBST;
import utils.TreeStringBuilder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SimpleBSTTest {

    @Test
    public void testInsertion() {
        SimpleBST<Integer> bst = new SimpleBST<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        String expected = "10(5(null,null),15(null,null))";
        String actual = TreeStringBuilder.build(bst.getRoot());
        assertEquals(expected, actual);
    }

    @Test
    public void testInsertionString() {
        SimpleBST<String> bst = new SimpleBST<>();
        bst.insert("koala");
        bst.insert("elephant");
        bst.insert("dog");
        bst.insert("lion");
        // Expected structure based on BST insertion order:
        // 1. Insert "koala" → root.
        // 2. Insert "elephant": "elephant" < "koala", goes left.
        // 3. Insert "dog": "dog" < "koala", then "dog" < "elephant", goes left of "elephant".
        // 4. Insert "lion": "lion" > "koala", goes right.
        // Expected structure: koala(elephant(dog(null,null),null),lion(null,null))
        String expected = "koala(elephant(dog(null,null),null),lion(null,null))";
        String actual = TreeStringBuilder.build(bst.getRoot());
        assertEquals(expected, actual);
    }

    @Test
    public void testTraversalsString() {
        SimpleBST<String> bst = new SimpleBST<>();
        bst.insert("koala");
        bst.insert("elephant");
        bst.insert("dog");
        bst.insert("lion");

        String preorder = TreeStringBuilder.buildPreorder(bst.getRoot());
        String inorder = TreeStringBuilder.buildInorder(bst.getRoot());
        String postorder = TreeStringBuilder.buildPostorder(bst.getRoot());

        // Expected traversals:
        // Preorder: root, left, right → "koala elephant dog lion"
        // Inorder: left, root, right → "dog elephant koala lion"
        // Postorder: left, right, root → "dog elephant lion koala"
        assertEquals("koala elephant dog lion", preorder);
        assertEquals("dog elephant koala lion", inorder);
        assertEquals("dog elephant lion koala", postorder);
    }

    @Test
    public void testDeletion() {
        // Test deletion of a leaf node.
        SimpleBST<Integer> bst = new SimpleBST<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        bst.insert(12);
        bst.insert(18);
        // Delete a leaf node (3)
        bst.delete(3);
        String expectedAfterDeleting3 = "10(5(null,7(null,null)),15(12(null,null),18(null,null)))";
        String actualAfterDeleting3 = TreeStringBuilder.build(bst.getRoot());
        assertEquals(expectedAfterDeleting3, actualAfterDeleting3);

        // Test deletion of a node with one child.
        SimpleBST<Integer> bst2 = new SimpleBST<>();
        bst2.insert(10);
        bst2.insert(5);
        bst2.insert(15);
        bst2.insert(3);
        bst2.insert(7);
        bst2.insert(6); // 7 now has one child (6)
        bst2.delete(7);
        String expectedAfterDeleting7 = "10(5(3(null,null),6(null,null)),15(null,null))";
        String actualAfterDeleting7 = TreeStringBuilder.build(bst2.getRoot());
        assertEquals(expectedAfterDeleting7, actualAfterDeleting7);

        // Test deletion of a node with two children.
        // Using bst2 after deleting 7, delete the root (10).
        bst2.delete(10);
        String expectedAfterDeleting10 = "15(5(3(null,null),6(null,null)),null)";
        String actualAfterDeleting10 = TreeStringBuilder.build(bst2.getRoot());
        assertEquals(expectedAfterDeleting10, actualAfterDeleting10);
    }

    @Test
    public void testContains() {
        SimpleBST<Integer> bst = new SimpleBST<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        assertTrue(bst.contains(10));
        assertTrue(bst.contains(5));
        assertTrue(bst.contains(15));
        assertFalse(bst.contains(8));
        bst.delete(5);
        assertFalse(bst.contains(5));
    }

    @Test
    public void testTraversals() {
        // Build a tree:
        //       10
        //      /  \
        //     5    15
        //    / \     \
        //   3   7     20
        SimpleBST<Integer> bst = new SimpleBST<>();
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        bst.insert(3);
        bst.insert(7);
        bst.insert(20);

        String preorder = TreeStringBuilder.buildPreorder(bst.getRoot());
        String inorder = TreeStringBuilder.buildInorder(bst.getRoot());
        String postorder = TreeStringBuilder.buildPostorder(bst.getRoot());

        // Expected traversals:
        // Preorder: 10 5 3 7 15 20
        // Inorder: 3 5 7 10 15 20
        // Postorder: 3 7 5 20 15 10
        assertEquals("10 5 3 7 15 20", preorder);
        assertEquals("3 5 7 10 15 20", inorder);
        assertEquals("3 7 5 20 15 10", postorder);
    }
}