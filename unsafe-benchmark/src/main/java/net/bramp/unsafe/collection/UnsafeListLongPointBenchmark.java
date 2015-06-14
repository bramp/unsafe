package net.bramp.unsafe.collection;

import net.bramp.unsafe.examples.LongPoint;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * Tests with arrays of LongPoints (2 longs).
 */
public class UnsafeListLongPointBenchmark {

    @State(Scope.Thread)
    public static class ArrayListState extends AbstractArrayListState<LongPoint> {

        @Override
        public LongPoint newInstance() {
            return new LongPoint(r.nextLong(), r.nextLong());
        }

        @Benchmark
        public void testListIterate(final Blackhole bh) {
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(i).x);
            }
        }
    }

    @State(Scope.Thread)
    public static class UnsafeListState extends AbstractUnsafeListState<LongPoint> {

        public Class<LongPoint> testClass() {
            return LongPoint.class;
        }

        @Override
        public LongPoint newInstance(LongPoint p) {
            p.x = r.nextLong();
            p.y = r.nextLong();
            return p;
        }

        @Benchmark
        public void testListIterate(final Blackhole bh) {
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(i).x);
            }
        }

        @Benchmark
        public void testListIterateInPlace(final Blackhole bh) {
            final LongPoint p = new LongPoint(0, 0);
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(p, i).x);
            }
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(UnsafeListLongPointBenchmark.class.getSimpleName())

            // Warmup, and then run test iterations
            .warmupIterations(2)
            .measurementIterations(5)

            // Each iteration call the test repeatedly for ~60 seconds
            .mode(Mode.AverageTime)
            .warmupTime(TimeValue.seconds(10))
            .measurementTime(TimeValue.seconds(10))

            // The size of the list
            .param("size", "4000", "400000", "40000000")  // 40000000 uses ~3.1GiB of RAM. TODO work out what I expect

            .forks(1)
            //.addProfiler(StackProfiler.class)
            //.addProfiler(GCProfiler.class)
            .build();

        new Runner(opt).run();
    }
}
