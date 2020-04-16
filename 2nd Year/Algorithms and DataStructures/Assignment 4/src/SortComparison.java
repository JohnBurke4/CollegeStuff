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
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            double temp = a[min];
            a[min] = a[i];
            a[i] = temp;
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

}//end class

