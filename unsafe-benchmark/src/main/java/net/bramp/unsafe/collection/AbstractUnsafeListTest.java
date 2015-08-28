package net.bramp.unsafe.collection;

import net.bramp.unsafe.UnsafeArrayList;
import net.bramp.unsafe.UnsafeHelper;
import net.bramp.unsafe.sort.InplaceQuickSort;
import net.bramp.unsafe.sort.Shuffle;

public abstract class AbstractUnsafeListTest<T extends Comparable<T>> extends AbstractListTest {

  protected UnsafeArrayList<T> list;

  /**
   * Returns the class under test being used within the generic list.
   *
   * @return the class under test.
   */
  public abstract Class<T> testClass();

  /**
   * Populates the fields of obj with random values.
   *
   * @param obj The object to populate
   * @return obj
   */
  public abstract T newInstance(T obj);

  @Override public void setup() throws IllegalAccessException, InstantiationException {
    final Class clazz = testClass();
    final T p = (T) UnsafeHelper.getUnsafe().allocateInstance(clazz); // Create a tmp instance

    list = new UnsafeArrayList<T>(clazz, size);
    for (int i = 0; i < size; i++) {
      // Reuse single point (since it gets copied into array)
      list.add(newInstance(p));
    }
  }

  @Override public long bytes() {
    return list.bytes();
  }

  @Override
  public void shuffle() {
    Shuffle.shuffleInplace(list, rand);
  }

  @Override
  public void sort() {
    InplaceQuickSort.quickSort(list);
  }
}
