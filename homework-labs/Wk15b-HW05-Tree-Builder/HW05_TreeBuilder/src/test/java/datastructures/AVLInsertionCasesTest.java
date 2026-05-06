/* ─────────────────────  AVLInsertionCasesTest.java  ───────────────────── */

package datastructures;

import org.junit.jupiter.api.Test;

class AVLInsertionCasesTest {

    /** Helper inserts the sequence and checks AVL invariants. */
    private static void check(Integer... seq) {
        AVLTree<Integer> t = new AVLTree<>();
        TreeTestUtils.bulkInsert(t, seq);
        TreeTestUtils.assertIsBST(t.getRoot());
        TreeTestUtils.assertAVLBalanced(t.getRoot());
    }

    // LL rotation: strictly descending
    @Test void insertLL() { check(40, 30, 20);}
    @Test void insertLLMultiple() { check(8,7,6,5,4,3,2,1);}

    // RR rotation: strictly ascending
    @Test void insertRR() { check(10, 20, 30); }
    @Test void insertRRLongerChain() { check(20,15,32,45,50); }

    // LR rotation: left‑right zig
    @Test void insertLR() { check(30, 10, 20); }
    // LR rotation: left‑right zig
    @Test void insertLRLonger() { check(60,30,80,10,50,42); }
    @Test void insertLRLongerAgain() { check(44,30,76,16,39,37); }

    // RL rotation: right‑left zag
    @Test void insertRL() { check(10, 30, 20); }
    @Test void insertRLLonger() { check(34,23,47,39,57,35); }

    // Cascade 1: height imbalance propagates up one level (double rotation)
    @Test void cascadeOnce() { check(10, 5, 15, 3, 7, 13); }

    // Cascade 2: deeper height cascade requiring multiple fixes
    @Test void cascadeTwice() { check(50, 30, 70, 10, 40, 60, 80, 5); }
}