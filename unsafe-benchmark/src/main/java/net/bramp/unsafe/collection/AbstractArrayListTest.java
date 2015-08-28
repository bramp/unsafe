package net.bramp.unsafe.collection;

import net.bramp.unsafe.UnsafeHelper;
import net.bramp.unsafe.sort.QuickSort;
import net.bramp.unsafe.sort.Shuffle;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;

public abstract class AbstractArrayListTest<T extends Comparable<T>> extends AbstractListTest {

  protected ArrayList<T> list;

  /**
   * Creates a new instance of the test class, with fields populated with random values.
   *
   * @return the new instance
   */
  public abstract T newInstance();

  @Override public void setup() {
    list = new ArrayList<T>(size);

    for (int i = 0; i < size; i++) {
      // New Point for each entry
      list.add(newInstance());
    }
  }

  @Override public long bytes() {
    int size = list.size();
    int referenceSize = 4; // TODO calculate this correctly

    if (size > 0) {
      long elementSize = UnsafeHelper.sizeOf(list.get(0));
      return (referenceSize + elementSize) * size;
    }
    return 0;
  }

  @Override public void shuffle() {
    Shuffle.shuffle(list, rand);
  }

  @Override public void sort() {
    Shuffle.shuffle(list, rand);
    QuickSort.quickSort(list);
  }

  @Override public void iterateInPlace(Blackhole bh) {
    throw new RuntimeException("Test not applicable to ArrayList");
  }
}
