package applications;

import java.util.Arrays;

import algorithms.Sorting;

public class Sorting_Driver {
    public static void main(String[] args) {
        int[] values = {50, 30, 20, 40, 70, 60, 80};
        Sorting sorter = new Sorting();

        System.out.println("Sorting Driver (bubble) Begin:");
        int[] return_value = sorter.bubble(values);
        System.out.println("Done Sorting...");
        System.out.println("Sorted Array (bubble) Result: " + Arrays.toString(return_value) + "\n" );

        System.out.println("Sorting Driver (selection) Begin:");
        return_value = sorter.selection(values);
        System.out.println("Done Sorting...");
        System.out.println("Sorted Array (selection) Result: " + Arrays.toString(return_value) + "\n" );

        int[] n_values = {12, 11, 13, 5, 6};
        System.out.println("\nNew Unsorted Array (insertion): " + Arrays.toString(n_values));
        System.out.println("Sorting Driver (insertion) Begin:");
        return_value = sorter.insertion(n_values);
        System.out.println("Done Sorting...");
        System.out.println("Sorted Array (insertion) Result: " + Arrays.toString(return_value) + "\n" );

        //                 l                       r
        //                 0   1   2   3   4   5   6
        int[] m_values = {38, 27, 43,  3,  9, 82, 10};
        System.out.println("\nNew Unsorted Array (merge): " + Arrays.toString(m_values));
        int l = 0;                      // leftmost     pointer
        int r = m_values.length - 1;    // rightmost    pointer
        return_value =   sorter.mergeSortRecurse(m_values, l, r);
        System.out.println("Done Sorting...");
        System.out.println("Sorted Array (merge) Result: " + Arrays.toString(return_value) + "\n" );

        // now trace this array
        int[] mm_values = {4, 7, 14, 1, 3, 9, 17};
        System.out.println("\nNew Unsorted Array (merge): " + Arrays.toString(mm_values));
        l = 0;                          // leftmost     pointer
        r = mm_values.length - 1;       // rightmost    pointer
        return_value =   sorter.mergeSortRecurse(mm_values, l, r);
        System.out.println("Done Sorting...");
        System.out.println("Sorted Array (merge) Result: " + Arrays.toString(return_value) + "\n" );


        // now trace this array
        int[] maux_values = {4, 7, 14, 1, 3, 9, 17};
        System.out.println("\nNew Unsorted Array (mergeAux): " + Arrays.toString(maux_values));
        return_value =   sorter.MergeSortIntAux( maux_values );
        System.out.println("Done Sorting...");
        System.out.println("Sorted Array (mergeAux) Result: " + Arrays.toString(return_value) + "\n" );


    } // main

}
