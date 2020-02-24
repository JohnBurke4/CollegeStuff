import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

//-------------------------------------------------------------------------

/**                         Insertion       Selection       QuickSort       Merge Recursive        Merge Iterative
 *  10 Random               0.0123ms        0.0067ms        0.0132ms        0.0303ms               0.0574ms
 *  100 Random              0.1314ms        0.1234ms        0.0379ms        0.2539ms               0.1177ms
 *  1000 Random             6.9917ms        3.9377ms        0.6187ms        0.6963ms               0.2086ms
 *  1000 Few Unique         4.7169ms        1.0475ms        0.1947ms        0.1961ms               0.1713ms
 *  1000 Nearly Ordered     0.0303ms        0.2244ms        0.1577ms        0.1508ms               0.1466ms
 *  1000 Reverse Order      0.4309ms        0.3470ms        0.5240ms        0.1146ms               0.1738ms
 *  1000 Sorted             0.0028ms        0.3099ms        0.8265ms        0.1899ms               0.2618ms
 *
 *  a. The order of input has a great impact on Insertion Sort, Selection Sort and QuickSort. Both types of merge sort make the same number of comparisons
 *      regardless of inputs. For the other three, the number of comparisons and swaps depends on the order.
 *
 *  b. Insertion sort has the biggest difference between best and worst performance. This is as when sorted, only n comparisons are needed and no items 'bubble' giving
 *      it a running time of O(n). Otherwise every element has to be compared against each other giving a time of O(n^2)
 *
 *  c. Worst algorithm for scalability is Insertion Sort and best algorithm is Merge Sort Iterative based on my times.
 *
 *  d. Based on the times observed, difference between Merge Recursive and Iterative was only observed for the random
 *      ordered files. Here the iterative version of the algorithm was slightly faster.
 *
 *  e. Fastest:
 *      10 Random: Selection Sort
 *      100 Random: Quicksort
 *      1000 Random: Merge Iterative
 *      1000 Few Unique: Merge Iterative
 *      1000 Nearly Ordered: Insertion Sort
 *      1000 Reverse Order: Merge Recursive
 *      1000 Sorted: Insertion Sort
 */

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
    public static void main(String[] args) {
        timeAndPrintAllAlgorithms("numbers10.txt", 10);
        timeAndPrintAllAlgorithms("numbers100.txt", 100);
        timeAndPrintAllAlgorithms("numbers1000.txt", 1000);
        timeAndPrintAllAlgorithms("numbers10Duplicates.txt", 1000);
        timeAndPrintAllAlgorithms("numbersNearlyOrdered1000.txt", 1000);
        timeAndPrintAllAlgorithms("numbersReverse1000.txt", 1000);
        timeAndPrintAllAlgorithms("numbersSorted1000.txt", 1000);
    }

    public static void timeAndPrintAllAlgorithms(String filename, int filesize){
        double[] numbers = new double[filesize];
        int index = 0;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/" + filename));
            String line = reader.readLine();
            while (line != null) {
                numbers[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Line not properly read");
        }

        double[] testInsertion = numbers.clone();
        double[] testSelection = numbers.clone();
        double[] testQuick = numbers.clone();
        double[] testMergeRecur = numbers.clone();
        double[] testMergeIter = numbers.clone();

        long startTime = System.nanoTime();
        SortComparison.insertionSort(testInsertion);
        long endTime = System.nanoTime();
        System.out.println("Insertion sort time for " + filename + " is " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        SortComparison.selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for " + filename + " is " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        SortComparison.quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for " + filename + " is " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        SortComparison.mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for " + filename + " is " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        SortComparison.mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for " + filename + " is " + ((endTime-startTime)/1000000.0) + "ms\n");
    }

}
