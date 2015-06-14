package net.bramp.unsafe.sort;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import net.bramp.unsafe.InplaceList;
import net.bramp.unsafe.UnsafeArrayList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QuickSortTest {

    private final static ImmutableList<ImmutableList<Long>> testdata = ImmutableList.of(
            ImmutableList.<Long>of(),
            ImmutableList.of(1L),
            ImmutableList.of(1L, 2L),
            ImmutableList.of(2L, 1L),
            ImmutableList.of(1L, 23L, 1L, 31L, 1L, 21L, 36L, 1L, 72L, 1L)
    );

    @Test
    public void testQuickSort() throws Exception {

        for(ImmutableList<Long> data : testdata) {
            List<Long> array = new ArrayList<Long>(data);
            QuickSort.quickSort(array);
            assertTrue(Ordering.natural().isOrdered(array));
        }
    }

    @Test
    public void testInplaceQuickSort() throws Exception {

        for(ImmutableList<Long> data : testdata) {
            InplaceList<Long> array = new UnsafeArrayList<Long>(Long.class, data);
            InplaceQuickSort.quickSort(array);
            assertTrue(Ordering.natural().isOrdered(array));
        }
    }
}