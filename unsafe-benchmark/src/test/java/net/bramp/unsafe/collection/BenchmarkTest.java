package net.bramp.unsafe.collection;

import com.google.common.collect.Ordering;
import net.bramp.unsafe.JMHHelper;
import net.bramp.unsafe.collection.tests.ArrayListLongPoint;
import net.bramp.unsafe.collection.tests.UnsafeListLongPoint;
import org.junit.Test;
import org.openjdk.jmh.infra.Blackhole;

import static org.junit.Assert.*;

public class BenchmarkTest {

    public static final Blackhole bh = JMHHelper.newBlackhole();

    @Test
    public void testArrayListBenchmarks() throws InstantiationException, IllegalAccessException {
        ArrayListLongPoint state = new ArrayListLongPoint();
        state.size = 1000;

        state.setup();
        state.iterate(bh);
        state.sort();
        state.shuffle();
        state.sort();

        assertTrue(Ordering.natural().isOrdered(state.list));
    }

    @Test
    public void testUnsafeListBenchmarks() throws InstantiationException, IllegalAccessException {
        UnsafeListLongPoint state = new UnsafeListLongPoint();
        state.size = 1000;

        state.setup();
        state.iterate(bh);
        state.iterateInPlace(bh);
        state.sort();
        state.shuffle();
        state.sort();

        assertTrue(Ordering.natural().isOrdered(state.list));
    }
}