package net.bramp.unsafe.collection;

import net.bramp.unsafe.UnsafeArrayList;
import net.bramp.unsafe.examples.EightLongs;
import net.bramp.unsafe.sort.InplaceQuickSort;
import net.bramp.unsafe.sort.QuickSort;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;

/**
 * Tests with arrays of EightLongs (8 longs).
 */
public class UnsafeListEightLongsBenchmark {

    @State(Scope.Thread)
    public static class ArrayListState extends AbstractArrayListState<EightLongs> {

        @Override
        public EightLongs newInstance() {
            return new EightLongs(
                    r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong(),
                    r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong());
        }

        @Benchmark
        public void testListIterate(final Blackhole bh) {
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(i).a);
            }
        }
    }

    @State(Scope.Thread)
    public static class UnsafeListState extends AbstractUnsafeListState<EightLongs> {

        @Benchmark
        public void testListIterate(final Blackhole bh) {
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(i).a);
            }
        }

        @Benchmark
        public void testListIterateInPlace(final Blackhole bh) {
            final EightLongs p = new EightLongs(0, 0, 0, 0, 0, 0, 0, 0);
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(p, i).a);
            }
        }

        @Override
        public Class<EightLongs> testClass() {
            return EightLongs.class;
        }

        @Override
        public EightLongs newInstance(EightLongs p) {
            p.a = r.nextLong();
            p.b = r.nextLong();
            p.c = r.nextLong();
            p.d = r.nextLong();
            p.e = r.nextLong();
            p.f = r.nextLong();
            p.g = r.nextLong();
            p.h = r.nextLong();
            return p;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(UnsafeListEightLongsBenchmark.class.getSimpleName())

            // Warmup, and then run test iterations
            .warmupIterations(2)
            .measurementIterations(5)

            // Each iteration call the test repeatedly for ~60 seconds
            .mode(Mode.AverageTime)
            .warmupTime(TimeValue.seconds(10))
            .measurementTime(TimeValue.seconds(10))

            // The size of the list
            .param("size", "400", "40000", "4000000")

            .forks(1)
            //.addProfiler(StackProfiler.class)
            //.addProfiler(GCProfiler.class)
            .build();

        new Runner(opt).run();
    }
}
