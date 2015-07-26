package net.bramp.unsafe.collection;


import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class UnsafeListBenchmark {

    public static ChainedOptionsBuilder defaultOptionsBuilder() {
        return new OptionsBuilder()
            // Warmup, and then run test iterations
            .warmupIterations(2)
            .measurementIterations(5)

            // Each iteration call the test repeatedly for ~60 seconds
            .mode(Mode.AverageTime)
            .warmupTime(TimeValue.seconds(10))
            .measurementTime(TimeValue.seconds(60))

            // The size of the list
            .param("size", "5000000", "20000000", "80000000")

            .forks(1)
            .jvmArgs("-Xmx4096m");
            //.addProfiler(MemoryProfiler.class)
            //.addProfiler(StackProfiler.class)
            //.addProfiler(GCProfiler.class)
    }
}
