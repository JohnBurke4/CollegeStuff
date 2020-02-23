// -------------------------------------------------------------------------

import org.junit.rules.Stopwatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Timer;

/**
 * This class contains static methods that implementing sorting of an array of numbers
 * using different sort algorithms.
 *
 * @author
 * @version HT 2020
 */

class SortComparison {

    /**
     * Sorts an array of doubles using InsertionSort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order.
     */
    static double[] insertionSort(double a[]) {
        for (int i = 1; i < a.length; i++) {
            int j = i;
            while (j > 0 && a[j] < a[j - 1]) {
                double temp = a[j];
                a[j] = a[j - 1];
                a[j - 1] = temp;
                j--;
            }
        }
        return a;
    }//end insertionsort

    /**
     * Sorts an array of doubles using Selection Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     */

    static double[] selectionSort(double a[]) {
        for (int i = 0; i < a.length - 1; i++) {
            int minIndex = i;
            double min = a[i];
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < min) {
                    min = a[j];
                    minIndex = j;
                }
            }
            a[minIndex] = a[i];
            a[i] = min;
        }
        return a;
    }//end selectionsort

    /**
     * Sorts an array of doubles using Quick Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     */
    static double[] quickSort(double a[]) {
        quickSort(a, 0, a.length - 1);
        return a;
    }//end quicksort

    private static void quickSort(double a[], int low, int high) {
        if (low < high) {
            int q = partition(a, low, high);
            quickSort(a, low, q - 1);
            quickSort(a, q + 1, high);
        }
    }

    private static int partition(double a[], int low, int high) {
        double partition = a[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (a[j] <= partition) {
                i++;
                double temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }
        double temp = a[++i];
        a[i] = a[high];
        a[high] = temp;
        return i;
    }

    /**
     * Sorts an array of doubles using Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */
    /**
     * Sorts an array of doubles using iterative implementation of Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param a: An unsorted array of doubles.
     * @return after the method returns, the array must be in ascending sorted order.
     */

    static double[] mergeSortIterative(double a[]) {
        for (int size = 1; size < a.length; size += size) {
            for (int i = 0; i < a.length - size; i += size + size) {
                merge(a, i, i + size - 1, Math.min(i + size + size - 1, a.length - 1));
            }
        }
        return a;
    }//end mergesortIterative


    /**
     * Sorts an array of doubles using recursive implementation of Merge Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     *
     * @param a: An unsorted array of doubles.
     * @return after the method returns, the array must be in ascending sorted order.
     */
    static double[] mergeSortRecursive(double a[]) {
        mergeSortRecursive(a, 0, a.length - 1);
        return a;
    }//end mergeSortRecursive


    private static void mergeSortRecursive(double a[], int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSortRecursive(a, low, mid);
            mergeSortRecursive(a, mid + 1, high);
            merge(a, low, mid, high);
        }
    }

    private static void merge(double a[], int low, int mid, int high) {
        int n1 = mid - low + 1;
        int n2 = high - mid;
        double L[] = new double[n1 + 1];
        double R[] = new double[n2 + 1];
        for (int i = 0; i < n1; i++) {
            L[i] = a[low + i];
        }

        for (int j = 0; j < n2; j++) {
            R[j] = a[mid + j + 1];
        }

        L[n1] = Double.POSITIVE_INFINITY;
        R[n2] = Double.POSITIVE_INFINITY;

        int i = 0;
        int j = 0;

        for (int k = low; k < high + 1; k++) {
            if (L[i] <= R[j]) {
                a[k] = L[i++];
            } else {
                a[k] = R[j++];
            }
        }


    }


    public static void main(String[] args) {
        double[] numbers10Rand = new double[10];
        double[] numbers100Rand = new double[100];
        double[] numbers1000Rand = new double[1000];
        double[] numbers1000Few = new double[1000];
        double[] numbers1000Near = new double[1000];
        double[] numbers1000Rev = new double[1000];
        double[] numbers1000Sort = new double[1000];
        BufferedReader reader;
        int index = 0;
        try {
            reader = new BufferedReader(new FileReader("src/numbers10.txt"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                numbers10Rand[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
//        for (int i = 0; i < 10; i++) {
//            System.out.println(test[i]);
//        }

        double[] testInsertion = numbers10Rand.clone();
        double[] testSelection = numbers10Rand.clone();
        double[] testQuick = numbers10Rand.clone();
        double[] testMergeRecur = numbers10Rand.clone();
        double[] testMergeIter = numbers10Rand.clone();

        long startTime = System.nanoTime();
        insertionSort(testInsertion);
        long endTime = System.nanoTime();
        System.out.println("Insertion sort time for 10 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for 10 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for 10 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for 10 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for 10 random numbers: " + ((endTime-startTime)/1000000.0) + "ms\n");

        index = 0;
        try {
            reader = new BufferedReader(new FileReader("src/numbers100.txt"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                numbers100Rand[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

        testInsertion = numbers100Rand.clone();
        testSelection = numbers100Rand.clone();
        testQuick = numbers100Rand.clone();
        testMergeRecur = numbers100Rand.clone();
        testMergeIter = numbers100Rand.clone();

        startTime = System.nanoTime();
        insertionSort(testInsertion);
        endTime = System.nanoTime();
        System.out.println("Insertion sort time for 100 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for 100 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for 100 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for 100 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for 100 random numbers: " + ((endTime-startTime)/1000000.0) + "ms\n");

        index = 0;
        try {
            reader = new BufferedReader(new FileReader("src/numbers1000.txt"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                numbers1000Rand[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        testInsertion = numbers1000Rand.clone();
        testSelection = numbers1000Rand.clone();
        testQuick = numbers1000Rand.clone();
        testMergeRecur = numbers1000Rand.clone();
        testMergeIter = numbers1000Rand.clone();

        startTime = System.nanoTime();
        insertionSort(testInsertion);
        endTime = System.nanoTime();
        System.out.println("Insertion sort time for 1000 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for 1000 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for 1000 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for 1000 random numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for 1000 random numbers: " + ((endTime-startTime)/1000000.0) + "ms\n");

        index = 0;
        try {
            reader = new BufferedReader(new FileReader("src/numbers1000Duplicates.txt"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                numbers1000Few[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

        testInsertion = numbers1000Few.clone();
        testSelection = numbers1000Few.clone();
        testQuick = numbers1000Few.clone();
        testMergeRecur = numbers1000Few.clone();
        testMergeIter = numbers1000Few.clone();

        startTime = System.nanoTime();
        insertionSort(testInsertion);
        endTime = System.nanoTime();
        System.out.println("Insertion sort time for 1000 few unique numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for 1000 few unique numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for 1000 few unique numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for 1000 few unique numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for 1000 few unique numbers: " + ((endTime-startTime)/1000000.0) + "ms\n");

        index = 0;
        try {
            reader = new BufferedReader(new FileReader("src/numbersNearlyOrdered1000.txt"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                numbers1000Near[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

        testInsertion = numbers1000Near.clone();
        testSelection = numbers1000Near.clone();
        testQuick = numbers1000Near.clone();
        testMergeRecur = numbers1000Near.clone();
        testMergeIter = numbers1000Near.clone();

        startTime = System.nanoTime();
        insertionSort(testInsertion);
        endTime = System.nanoTime();
        System.out.println("Insertion sort time for 1000 nearly ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for 1000 nearly ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for 1000 nearly ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for 1000 nearly ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for 1000 nearly ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms\n");

        index = 0;
        try {
            reader = new BufferedReader(new FileReader("src/numbersReverse1000.txt"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                numbers1000Rev[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

        testInsertion = numbers1000Rev.clone();
        testSelection = numbers1000Rev.clone();
        testQuick = numbers1000Rev.clone();
        testMergeRecur = numbers1000Rev.clone();
        testMergeIter = numbers1000Rev.clone();

        startTime = System.nanoTime();
        insertionSort(testInsertion);
        endTime = System.nanoTime();
        System.out.println("Insertion sort time for 1000 reverse ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for 1000 reverse ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for 1000 reverse ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for 1000 reverse ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for 1000 reverse ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms\n");

        index = 0;
        try {
            reader = new BufferedReader(new FileReader("src/numbersSorted1000.txt"));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                numbers1000Sort[index++] = Double.parseDouble(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error");
        }

        testInsertion = numbers1000Sort.clone();
        testSelection = numbers1000Sort.clone();
        testQuick = numbers1000Sort.clone();
        testMergeRecur = numbers1000Sort.clone();
        testMergeIter = numbers1000Sort.clone();

        startTime = System.nanoTime();
        insertionSort(testInsertion);
        endTime = System.nanoTime();
        System.out.println("Insertion sort time for 1000 ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        selectionSort(testSelection);
        endTime = System.nanoTime();
        System.out.println("Selection sort time for 1000 ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        quickSort(testQuick);
        endTime = System.nanoTime();
        System.out.println("Quick sort time for 1000 ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortRecursive(testMergeRecur);
        endTime = System.nanoTime();
        System.out.println("Merge sort recursive time for 1000 ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms");

        startTime = System.nanoTime();
        mergeSortIterative(testMergeIter);
        endTime = System.nanoTime();
        System.out.println("Merge sort iterative time for 1000 ordered numbers: " + ((endTime-startTime)/1000000.0) + "ms\n");


    }

}//end class

