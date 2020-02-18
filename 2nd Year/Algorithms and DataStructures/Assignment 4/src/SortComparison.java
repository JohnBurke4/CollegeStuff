// -------------------------------------------------------------------------

/**
 *  This class contains static methods that implementing sorting of an array of numbers
 *  using different sort algorithms.
 *
 *  @author
 *  @version HT 2020
 */

class SortComparison {

    /**
     * Sorts an array of doubles using InsertionSort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order.
     *
     */
    static double [] insertionSort (double a[]){
        for(int i = 1; i < a.length; i++){
            int j = i;
            while (j > 0 && a[j] < a[j-1]){
                double temp = a[j];
                a[j] = a[j-1];
                a[j-1] = temp;
                j--;
            }
        }
        return a;
    }//end insertionsort

    /**
     * Sorts an array of doubles using Selection Sort.
     * This method is static, thus it can be called as SortComparison.sort(a)
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */

    static double [] selectionSort (double a[]){
        for(int i = 0; i < a.length-1; i++){
            int minIndex = i;
            double min = a[i];
            for (int j = i+1; j < a.length; j++){
                if (a[j] < min){
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
     * @param a: An unsorted array of doubles.
     * @return array sorted in ascending order
     *
     */
    static double [] quickSort (double a[]){
        quickSort(a, 0, a.length-1);
        return a;
    }//end quicksort

    private static void quickSort(double a[], int low, int high){
        if (low < high){
            int q = partition(a, low, high);
            quickSort(a, low, q-1);
            quickSort(a, q+1, high);
        }
    }

    private static int partition(double a[], int low, int high){
        double partition = a[high];
        int i = low-1;
        for (int j = low; j < high; j++){
            if (a[j] <= partition){
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

}//end class

