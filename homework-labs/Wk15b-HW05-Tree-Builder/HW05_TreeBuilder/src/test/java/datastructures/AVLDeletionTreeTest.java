package datastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AVLDeletionTreeTest {

    /** Helper to build an AVL with given keys and assert initial balance. */
    private static AVLTree<Integer> build(Integer... keys) {
        AVLTree<Integer> t = new AVLTree<>();
        TreeTestUtils.bulkInsert(t, keys);
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        return t;
    }

    // ─── Single‑rotation LL fix (deleting right‑heavy leaf) ─────────────────
    @Test
    void deleteLL() {
        AVLTree<Integer> t = build(30, 20, 40, 10);  // 10 is left‑left grandchild
        t.delete(40);                                // root becomes unbalanced left
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        TreeTestUtils.assertIsBST(t.getRoot());
        assertFalse(t.contains(40));
    }

    // ─── Single‑rotation RR fix ──────────────────────────────────────────────
    @Test
    void deleteRR() {
        AVLTree<Integer> t = build(10, 5, 30, 25, 40); // 40 is right‑right grandchild
        t.delete(5);                                  // root becomes unbalanced right
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        assertFalse(t.contains(5));
    }


    // ─── Double‑rotation LR fix ──────────────────────────────────────────────
    @Test
    void deleteLR() {
        AVLTree<Integer> t = build(50, 20, 70, 10, 30, 25); // 70 removal triggers LR
        t.delete(70);
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        assertFalse(t.contains(70));
    }

    // ─── Double‑rotation RL fix ──────────────────────────────────────────────
    @Test
    void deleteRL() {
        AVLTree<Integer> t = build(20, 10, 40, 35, 50, 37); // 10 removal triggers RL
        t.delete(10);
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        assertFalse(t.contains(10));
    }

    // ─── Stress: multiple deletions still balanced ──────────────────────────
    @Test
    void deleteMany() {
        AVLTree<Integer> t = build(10,20,30,40,50,25,5,15,35);
        TreeTestUtils.bulkDelete(t, 40, 30, 10);            // mix of leaf & two‑child nodes
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        for (int gone : new int[]{40,30,10})
            assertFalse(t.contains(gone));
    }


    // ─── Double‑rotation RL fix ────────────────────────────────────────────
    @Test void deleteRLLonger() { // RL/RL root 60.
        AVLTree<Integer> t = build(50, 25, 75, 15, 40, 60, 80, 35, 55, 65, 90, 62);
        t.delete(15);                                    // triggers RL
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        assertFalse(t.contains(15));
    }

    // ─── Two-step delete; expect root becomes 80 ───────────────────────────
    @Test
    void deleteTwoRootBecomes80() {
        AVLTree<Integer> t = build(60, 20, 100, 80, 120);
        t.delete(100);
        t.delete(60);

        // Tree should rebalance with 80 at the root
        TreeTestUtils.assertAVLBalanced(t.getRoot());
        assertEquals(80, t.getRoot().getData());
    }

}