package net.bramp.unsafe.collection;

import net.bramp.unsafe.examples.EightLongs;
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
        Options opt = defaultOptionsBuilder()
            .include(UnsafeListEightLongsBenchmark.class.getSimpleName())
            .build();

        new Runner(opt).run();
    }
}
