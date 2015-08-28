package net.bramp.unsafe;

import java.util.List;

public interface InplaceList<E> extends List<E> {

  /**
   * Copies the element at index into dest.
   *
   * @param dest  The destination object
   * @param index The index of the object to get
   * @return The fetched object
   */
  E get(E dest, int index);

  /**
   * Swaps two elements
   * @param i
   * @param j
   */
  void swap(int i, int j);
}
