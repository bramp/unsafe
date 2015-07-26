package net.bramp.unsafe.collection;

import net.bramp.unsafe.examples.FourLongs;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;

import static net.bramp.unsafe.collection.UnsafeListBenchmark.defaultOptionsBuilder;

/**
 * Tests with arrays of FourLongs (4 longs).
 */
public class UnsafeListFourLongsBenchmark {

    @State(Scope.Thread)
    public static class ArrayListState extends AbstractArrayListState<FourLongs> {

        @Override
        public FourLongs newInstance() {
            return new FourLongs(r.nextLong(), r.nextLong(), r.nextLong(), r.nextLong());
        }

        @Benchmark
        public void testListIterate(final Blackhole bh) {
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(i).a);
            }
        }
    }

    @State(Scope.Thread)
    public static class UnsafeListState extends AbstractUnsafeListState<FourLongs> {

        @Benchmark
        public void testListIterate(final Blackhole bh) {
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(i).a);
            }
        }

        @Benchmark
        public void testListIterateInPlace(final Blackhole bh) {
            final FourLongs p = new FourLongs(0, 0, 0, 0);
            for (int i = 0; i < size; i++) {
                bh.consume(list.get(p, i).a);
            }
        }

        @Override
        public Class<FourLongs> testClass() {
            return FourLongs.class;
        }

        @Override
        public FourLongs newInstance(FourLongs p) {
            p.a = r.nextLong();
            p.b = r.nextLong();
            p.c = r.nextLong();
            p.d = r.nextLong();
            return p;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = defaultOptionsBuilder()
            .include(UnsafeListFourLongsBenchmark.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }
}
