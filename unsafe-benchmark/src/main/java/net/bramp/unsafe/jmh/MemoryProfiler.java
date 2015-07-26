package net.bramp.unsafe.jmh;

import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.IterationParams;
import org.openjdk.jmh.profile.InternalProfiler;
import org.openjdk.jmh.profile.ProfilerResult;
import org.openjdk.jmh.results.AggregationPolicy;
import org.openjdk.jmh.results.IterationResult;
import org.openjdk.jmh.results.Result;

import java.util.Arrays;
import java.util.Collection;

public class MemoryProfiler implements InternalProfiler {

    final static String PREFIX = "\u00b7";

    @Override
    public void beforeIteration(BenchmarkParams benchmarkParams, IterationParams iterationParams) {}

    @Override
    public Collection<? extends Result> afterIteration(BenchmarkParams benchmarkParams, IterationParams iterationParams, IterationResult result) {
        final Runtime runtime = Runtime.getRuntime();

        final long max = runtime.maxMemory();
        final long total = runtime.totalMemory();
        final long free = runtime.freeMemory();
        final long used = total - free;

        return Arrays.asList(
            new ProfilerResult(PREFIX + "rt.mem.max",   max,   "bytes", AggregationPolicy.MAX),
            new ProfilerResult(PREFIX + "rt.mem.total", total, "bytes", AggregationPolicy.MAX),
            new ProfilerResult(PREFIX + "rt.mem.free",  free,  "bytes", AggregationPolicy.MAX),
            new ProfilerResult(PREFIX + "rt.mem.used",  used,  "bytes", AggregationPolicy.MAX)
        );
    }

    @Override
    public String getDescription() {
        return "Reports JVM memory usage";
    }
}
