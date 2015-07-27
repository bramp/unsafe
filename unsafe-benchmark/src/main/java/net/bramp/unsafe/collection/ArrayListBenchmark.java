package net.bramp.unsafe.collection;


import com.google.common.collect.ImmutableMap;
import net.bramp.unsafe.collection.tests.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.Map;

@State(Scope.Thread)
public class ArrayListBenchmark {

    public final static Map<String, AbstractListTest> benchmarks = ImmutableMap.<String, AbstractListTest>builder()
            .put("ArrayList<LongPoint>", new ArrayListLongPoint())
            .put("UnsafeArrayList<LongPoint>", new UnsafeListLongPoint())
            .put("ArrayList<FourLongs>", new ArrayListFourLongs())
            .put("UnsafeArrayList<FourLongs>", new UnsafeListFourLongs())
            .put("ArrayList<EightLongs>", new ArrayListEightLongs())
            .put("UnsafeArrayList<EightLongs>", new UnsafeListEightLongs())
            .build();

    AbstractListTest test; // Holds the code under test

    @Param("40000000")
    public int size;

    @Param("null")
    public String clazz;

    @Setup(Level.Trial)
    public void setup() throws Exception {
        test = benchmarks.get(clazz);
        test.setSize(size);
        test.setRandomSeed(size);  // TODO Use iteration some how
        test.setup();
    }

    @Setup(Level.Iteration)
    public void shuffle() throws IllegalAccessException, InstantiationException {
        // We shuffle to make the sort different each time, and to ensure the list starts randomised
        test.shuffle();
    }

    @Benchmark
    public void testSort() {
        // We shuffle to make the sort different each time, and to ensure the list starts randomised
        test.sort();
    }

    @Benchmark
    public void testIterate(final Blackhole bh) {
        test.iterate(bh);
    }

    @Benchmark
    public void testListIterateInPlace(final Blackhole bh) {
        test.iterateInPlace(bh);
    }

    public static void main(String[] args) throws RunnerException {

        for (String benchmark : benchmarks.keySet()) {
            Options opt = new OptionsBuilder()
                .include(ArrayListBenchmark.class.getSimpleName())

                // Warmup, and then run test iterations
                .warmupIterations(2)
                .measurementIterations(5)

                // Each iteration call the test repeatedly for ~60 seconds
                .mode(Mode.AverageTime)
                .warmupTime(TimeValue.seconds(10))
                .measurementTime(TimeValue.seconds(60))

                // The size of the list
                .param("size", "80000000", "20000000", "5000000")
                .param("clazz", benchmark)

                .forks(1)
                .jvmArgs("-Xmx16g")
                //.addProfiler(MemoryProfiler.class)
                //.addProfiler(StackProfiler.class)
                //.addProfiler(GCProfiler.class)

                .build();

            new Runner(opt).run();
        }
    }
}
