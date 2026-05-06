/* ─────────────────────────  AVLTreeGeneralTest.java  ───────────────────── */
package datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AVLTreeGeneralTest {

    private static <T extends Comparable<T>>
    void checkSequence(T... keys) {
        AVLTree<T> t = new AVLTree<>();
        TreeTestUtils.bulkInsert(t, keys);
        TreeTestUtils.assertIsBST(t.getRoot());
        TreeTestUtils.assertAVLBalanced(t.getRoot());

        // quick membership check
        for (T k : keys) assertTrue(t.contains(k));
    }

    /** Medium mixed integer insert → should balance. */
    @Test
    void intsMixedOrder() {
        checkSequence(25, 80, 10, 30, 90, 5, 50, 60, 70, 40);
    }

    /** Simple string insert to confirm generics behave. */
    @Test
    void stringsAlphabet() {
        checkSequence("delta", "alpha", "charlie", "bravo", "echo");
    }

    /** Random fuzz on 200 integers (seeded for repeatability). */
    @Test
    void randomFuzz200() {
        Integer[] vals = TreeTestUtils.randomInts(200, 2720L);
        checkSequence(vals);
    }
}