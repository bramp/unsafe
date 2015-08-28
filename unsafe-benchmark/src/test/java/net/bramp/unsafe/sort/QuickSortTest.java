package net.bramp.unsafe.sort;

import net.bramp.unsafe.InplaceList;
import net.bramp.unsafe.UnsafeArrayList;
import net.bramp.unsafe.examples.LongPoint;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class QuickSortTest {

  private static final ImmutableList<ImmutableList<Long>> testdata = ImmutableList
      .of(ImmutableList.<Long>of(), ImmutableList.of(1L), ImmutableList.of(1L, 2L),
          ImmutableList.of(2L, 1L), ImmutableList.of(1L, 23L, 1L, 31L, 1L, 21L, 36L, 1L, 72L, 1L));

  @Test public void testQuickSort() throws Exception {
    for (ImmutableList<Long> data : testdata) {
      List<Long> array = new ArrayList<Long>(data);
      QuickSort.quickSort(array);
      assertTrue(Ordering.natural().isOrdered(array));
    }
  }

  @Test public void testLargeQuickSort() throws Exception {

    final int size = 40000000;
    final Random r = new Random(size);

    ArrayList<LongPoint> list = new ArrayList<LongPoint>(size);
    for (int i = 0; i < size; i++) {
      list.add(new LongPoint(r.nextLong(), r.nextLong()));
    }

    // This is not guaranteed it is sorted, but chances are it is.
    assertFalse(Ordering.natural().isOrdered(list));

    QuickSort.quickSort(list);
    assertTrue(Ordering.natural().isOrdered(list));

    QuickSort.quickSort(list);
    assertTrue(Ordering.natural().isOrdered(list));
  }

  @Test public void testInplaceQuickSort() throws Exception {
    for (ImmutableList<Long> data : testdata) {
      InplaceList<Long> array = new UnsafeArrayList<Long>(Long.class, data);
      InplaceQuickSort.quickSort(array);
      assertTrue(Ordering.natural().isOrdered(array));
    }
  }
}
