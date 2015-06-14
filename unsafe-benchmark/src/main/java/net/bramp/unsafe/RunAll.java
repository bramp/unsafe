package net.bramp.unsafe;

import net.bramp.unsafe.collection.UnsafeListEightLongsBenchmark;
import net.bramp.unsafe.collection.UnsafeListFourLongsBenchmark;
import net.bramp.unsafe.collection.UnsafeListLongPointBenchmark;
import net.bramp.unsafe.copier.UnrolledCopierBenchmark;
import org.openjdk.jmh.runner.RunnerException;


public class RunAll {
    public static void main(String[] args) throws RunnerException {
        UnrolledCopierBenchmark.main(args);
        UnsafeListLongPointBenchmark.main(args);
        UnsafeListFourLongsBenchmark.main(args);
        UnsafeListEightLongsBenchmark.main(args);
    }
}