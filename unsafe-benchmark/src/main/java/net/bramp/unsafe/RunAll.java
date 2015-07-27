package net.bramp.unsafe;

import net.bramp.unsafe.collection.ArrayListBenchmark;
import net.bramp.unsafe.copier.UnrolledCopierBenchmark;
import org.openjdk.jmh.runner.RunnerException;


public class RunAll {
    public static void main(String[] args) throws RunnerException {
        UnrolledCopierBenchmark.main(args);
        ArrayListBenchmark.main(args);
    }
}