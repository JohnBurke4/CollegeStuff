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

}//end class

