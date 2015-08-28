/**
 * Taken from http://java67.blogspot.com/2014/07/quicksort-algorithm-in-java-in-place-example.html
 */

package net.bramp.unsafe.sort;


import java.util.List;

/**
 * Java program to Sort integer array using QuickSort algorithm using recursion.
 * Recursive QuickSort algorithm, partitioned list into two parts by a pivot,
 * and then recursively sorts each list.
 *
 * @author Javin Paul
 */
public class QuickSort {

  /**
   * Sorts given array using QuickSort
   *
   * @param array to be sorted.
   * @param <E>   generic type of list
   */
  public static <E extends Comparable<E>> void quickSort(List<E> array) {
    if (array.isEmpty()) {
      return;
    }
    recursiveQuickSort(array, 0, array.size() - 1);
  }

  /**
   * Recursive quicksort logic.
   *
   * @param array    input array
   * @param startIdx start index of the array
   * @param endIdx   end index of the array
   * @param <E>      generic type of list
   */
  public static <E extends Comparable<E>> void recursiveQuickSort(List<E> array, int startIdx,
      int endIdx) {

    int idx = partition(array, startIdx, endIdx);

    // Recursively call quicksort with left part of the partitioned array
    if (startIdx < idx - 1) {
      recursiveQuickSort(array, startIdx, idx - 1);
    }

    // Recursively call quick sort with right part of the partitioned array
    if (endIdx > idx) {
      recursiveQuickSort(array, idx, endIdx);
    }
  }

  /**
   * Divides array from pivot, left side contains elements less than
   * Pivot while right side contains elements greater than pivot.
   *
   * @param array array to partitioned
   * @param left  lower bound of the array
   * @param right upper bound of the array
   * @param <E>   generic type of list
   * @return the partition index
   */
  public static <E extends Comparable<E>> int partition(List<E> array, int left, int right) {
    final int mid = left + ((right - left) / 2);
    E pivot = array.get(mid); // taking mid point as pivot

    while (left <= right) {
      //searching number which is greater than pivot, bottom up
      while (array.get(left).compareTo(pivot) < 0) {
        left++;
      }
      //searching number which is less than pivot, top down
      while (array.get(right).compareTo(pivot) > 0) {
        right--;
      }

      // swap the values
      if (left <= right) {
        E tmp = array.get(left);
        array.set(left, array.get(right));
        array.set(right, tmp);

        //increment left index and decrement right index
        left++;
        right--;
      }
    }
    return left;
  }
}
