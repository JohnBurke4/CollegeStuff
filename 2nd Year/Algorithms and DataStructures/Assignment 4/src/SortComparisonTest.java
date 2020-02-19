import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for SortComparison.java
 *
 *  @author
 *  @version HT 2020
 */
@RunWith(JUnit4.class)
public class SortComparisonTest
{
    //~ Constructor ........................................................
    @Test
    public void testConstructor()
    {
        new SortComparison();
    }

    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Check that the methods work for empty arrays
     */
    @Test
    public void testEmpty()
    {
        double[] emptyArray = {};
        double[] emptyExpected = {};

        assertArrayEquals(emptyExpected, SortComparison.insertionSort(emptyArray), 0.0);
        assertArrayEquals(emptyExpected, SortComparison.selectionSort(emptyArray), 0.0);
        assertArrayEquals(emptyExpected, SortComparison.quickSort(emptyArray), 0.0);
        assertArrayEquals(emptyExpected, SortComparison.mergeSortIterative(emptyArray), 0.0);
        assertArrayEquals(emptyExpected, SortComparison.mergeSortRecursive(emptyArray), 0.0);
    }

    @Test
    public void testInsertion(){
        double[] test1 = {1.0};
        double[] test2 = {2.0, 3.5, 5.2};
        double[] test3 = {10.9, 3.0, 2.0, 4.6, -7.6, 10.9};
        double[] test4 = {1.2, 2.4, 3.5, 1.2};
        double[] test5 = {3.5, 2.9, 1.1, 0.9, -1.9};

        double[] expected1 = {1.0};
        double[] expected2 = {2.0, 3.5, 5.2};
        double[] expected3 = {-7.6, 2.0, 3.0, 4.6, 10.9, 10.9};
        double[] expected4 = {1.2, 1.2, 2.4, 3.5};
        double[] expected5 = {-1.9, 0.9, 1.1, 2.9, 3.5};

        assertArrayEquals(expected1, SortComparison.insertionSort(test1), 0.0);
        assertArrayEquals(expected2, SortComparison.insertionSort(test2), 0.0);
        assertArrayEquals(expected3, SortComparison.insertionSort(test3), 0.0);
        assertArrayEquals(expected4, SortComparison.insertionSort(test4), 0.0);
        assertArrayEquals(expected5, SortComparison.insertionSort(test5), 0.0);
    }

    @Test
    public void testSelection(){
        double[] test1 = {1.0};
        double[] test2 = {2.0, 3.5, 5.2};
        double[] test3 = {10.9, 3.0, 2.0, 4.6, -7.6, 10.9};
        double[] test4 = {1.2, 2.4, 3.5, 1.2};
        double[] test5 = {3.5, 2.9, 1.1, 0.9, -1.9};

        double[] expected1 = {1.0};
        double[] expected2 = {2.0, 3.5, 5.2};
        double[] expected3 = {-7.6, 2.0, 3.0, 4.6, 10.9, 10.9};
        double[] expected4 = {1.2, 1.2, 2.4, 3.5};
        double[] expected5 = {-1.9, 0.9, 1.1, 2.9, 3.5};

        assertArrayEquals(expected1, SortComparison.selectionSort(test1), 0.0);
        assertArrayEquals(expected2, SortComparison.selectionSort(test2), 0.0);
        assertArrayEquals(expected3, SortComparison.selectionSort(test3), 0.0);
        assertArrayEquals(expected4, SortComparison.selectionSort(test4), 0.0);
        assertArrayEquals(expected5, SortComparison.selectionSort(test5), 0.0);
    }

    @Test
    public void testQuick(){
        double[] test1 = {1.0};
        double[] test2 = {2.0, 3.5, 5.2};
        double[] test3 = {10.9, 3.0, 2.0, 4.6, -7.6, 10.9};
        double[] test4 = {1.2, 2.4, 3.5, 1.2};
        double[] test5 = {3.5, 2.9, 1.1, 0.9, -1.9};

        double[] expected1 = {1.0};
        double[] expected2 = {2.0, 3.5, 5.2};
        double[] expected3 = {-7.6, 2.0, 3.0, 4.6, 10.9, 10.9};
        double[] expected4 = {1.2, 1.2, 2.4, 3.5};
        double[] expected5 = {-1.9, 0.9, 1.1, 2.9, 3.5};

        assertArrayEquals(expected1, SortComparison.quickSort(test1), 0.0);
        assertArrayEquals(expected2, SortComparison.quickSort(test2), 0.0);
        assertArrayEquals(expected3, SortComparison.quickSort(test3), 0.0);
        assertArrayEquals(expected4, SortComparison.quickSort(test4), 0.0);
        assertArrayEquals(expected5, SortComparison.quickSort(test5), 0.0);
    }


    @Test
    public void testMergeRecursive(){
        double[] test1 = {1.0};
        double[] test2 = {2.0, 3.5, 5.2};
        double[] test3 = {10.9, 3.0, 2.0, 4.6, -7.6, 10.9};
        double[] test4 = {1.2, 2.4, 3.5, 1.2};
        double[] test5 = {3.5, 2.9, 1.1, 0.9, -1.9};

        double[] expected1 = {1.0};
        double[] expected2 = {2.0, 3.5, 5.2};
        double[] expected3 = {-7.6, 2.0, 3.0, 4.6, 10.9, 10.9};
        double[] expected4 = {1.2, 1.2, 2.4, 3.5};
        double[] expected5 = {-1.9, 0.9, 1.1, 2.9, 3.5};

        assertArrayEquals(expected1, SortComparison.mergeSortRecursive(test1), 0.0);
        assertArrayEquals(expected2, SortComparison.mergeSortRecursive(test2), 0.0);
        assertArrayEquals(expected3, SortComparison.mergeSortRecursive(test3), 0.0);
        assertArrayEquals(expected4, SortComparison.mergeSortRecursive(test4), 0.0);
        assertArrayEquals(expected5, SortComparison.mergeSortRecursive(test5), 0.0);
    }

    @Test
    public void testMergeIterative(){
        double[] test1 = {1.0};
        double[] test2 = {2.0, 3.5, 5.2};
        double[] test3 = {10.9, 3.0, 2.0, 4.6, -7.6, 10.9};
        double[] test4 = {1.2, 2.4, 3.5, 1.2};
        double[] test5 = {3.5, 2.9, 1.1, 0.9, -1.9};

        double[] expected1 = {1.0};
        double[] expected2 = {2.0, 3.5, 5.2};
        double[] expected3 = {-7.6, 2.0, 3.0, 4.6, 10.9, 10.9};
        double[] expected4 = {1.2, 1.2, 2.4, 3.5};
        double[] expected5 = {-1.9, 0.9, 1.1, 2.9, 3.5};

        assertArrayEquals(expected1, SortComparison.mergeSortIterative(test1), 0.0);
        assertArrayEquals(expected2, SortComparison.mergeSortIterative(test2), 0.0);
        assertArrayEquals(expected3, SortComparison.mergeSortIterative(test3), 0.0);
        assertArrayEquals(expected4, SortComparison.mergeSortIterative(test4), 0.0);
        assertArrayEquals(expected5, SortComparison.mergeSortIterative(test5), 0.0);
    }

    // TODO: add more tests here. Each line of code and ech decision in Collinear.java should
    // be executed at least once from at least one test.

    // ----------------------------------------------------------
    /**
     *  Main Method.
     *  Use this main method to create the experiments needed to answer the experimental performance questions of this assignment.
     *
     */
    public static void main(String[] args)
    {
        //TODO: implement this method
    }

}
