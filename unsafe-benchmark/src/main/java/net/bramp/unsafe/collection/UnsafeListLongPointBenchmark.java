package net.bramp.unsafe.collection;

import net.bramp.unsafe.examples.LongPoint;
import net.bramp.unsafe.jmh.MemoryProfiler;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import static net.bramp.unsafe.collection.UnsafeListBenchmark.defaultOptionsBuilder;

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

        Options opt = defaultOptionsBuilder()
            .include(UnsafeListLongPointBenchmark.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }
}
