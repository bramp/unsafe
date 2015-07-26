package net.bramp.unsafe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.bramp.unsafe.collection.BenchmarkInterface;
import net.bramp.unsafe.collection.UnsafeListEightLongsBenchmark;
import net.bramp.unsafe.collection.UnsafeListFourLongsBenchmark;
import net.bramp.unsafe.collection.UnsafeListLongPointBenchmark;
import org.openjdk.jmh.util.Utils;
import sun.management.VMManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static net.bramp.unsafe.MemoryUtils.buildJavaArg;
import static net.bramp.unsafe.MemoryUtils.memoryUsage;
import static net.bramp.unsafe.MemoryUtils.pidMemoryUsage;

/**
 * Finds out how much memory each benchmark would take
 */
public class MemoryTest {

    final static List<Integer> sizes = ImmutableList.<Integer>of(4000, 8000, 400000, 800000, 40000000, 80000000);
    final static Map<String, BenchmarkInterface> benchmarks = ImmutableMap.<String, BenchmarkInterface>builder()
            .put("ArrayList\t2", new UnsafeListLongPointBenchmark.ArrayListState())
            .put("UnsafeArrayList\t2", new UnsafeListLongPointBenchmark.UnsafeListState())
            .put("ArrayList\t4", new UnsafeListFourLongsBenchmark.ArrayListState())
            .put("UnsafeArrayList\t4", new UnsafeListFourLongsBenchmark.UnsafeListState())
            .put("ArrayList\t8", new UnsafeListEightLongsBenchmark.ArrayListState())
            .put("UnsafeArrayList\t8", new UnsafeListEightLongsBenchmark.UnsafeListState())
            .build();

    static Process childProcess;

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    if (childProcess != null) {
                        childProcess.destroy();
                    }
                }
            });

            System.out.printf("List\tSize\tLength\tBytes\tMax\tTotal\tFree\tUsed\tRSS\tVSZ\n");

            // Loop through starting each sub process
            for (Integer size : sizes) {
                for (String benchmark : benchmarks.keySet()) {

                    List<String> javaArgs = buildJavaArg();
                    javaArgs.add(benchmark);
                    javaArgs.add(Integer.toString(size));

                    childProcess = new ProcessBuilder()
                        .command(javaArgs)
                        .inheritIO()
                        .start();

                    childProcess.waitFor();
                }
            }

        } else {
            BenchmarkInterface benchmark = benchmarks.get(args[0]);
            int size = Integer.parseInt(args[1]);

            benchmark.setSize(size);
            benchmark.setup();

            long bytes = benchmark.bytes();

            System.out.printf("%s\t%d\t%d\t%s\t%s\n", args[0], size, bytes, memoryUsage(), pidMemoryUsage());
        }

    }
}
