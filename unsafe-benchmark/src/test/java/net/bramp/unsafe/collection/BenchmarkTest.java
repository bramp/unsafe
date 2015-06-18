package net.bramp.unsafe.collection;

import com.google.common.collect.Ordering;
import net.bramp.unsafe.JMHHelper;
import org.junit.Test;
import org.openjdk.jmh.infra.Blackhole;

import static org.junit.Assert.*;

public class BenchmarkTest {

    public static final Blackhole bh = JMHHelper.newBlackhole();

    @Test
    public void testArrayListBenchmarks() throws InstantiationException, IllegalAccessException {
        UnsafeListLongPointBenchmark.ArrayListState state = new UnsafeListLongPointBenchmark.ArrayListState();
        state.size = 1000;

        state.setup();
        state.testListIterate(bh);
        state.testListSort();
        state.shuffle();
        state.testListSort();

        assertTrue(Ordering.natural().isOrdered(state.list));
    }

    @Test
    public void testUnsafeListBenchmarks() throws InstantiationException, IllegalAccessException {
        UnsafeListLongPointBenchmark.UnsafeListState state = new UnsafeListLongPointBenchmark.UnsafeListState();
        state.size = 1000;

        state.setup();
        state.testListIterate(bh);
        state.testListIterateInPlace(bh);
        state.testListSortInPlace();
        state.shuffle();
        state.testListSortInPlace();

        assertTrue(Ordering.natural().isOrdered(state.list));
    }
}