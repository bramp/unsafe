package net.bramp.unsafe;

import com.google.common.collect.ImmutableList;
import net.bramp.unsafe.collection.AbstractListTest;
import net.bramp.unsafe.collection.ArrayListBenchmark;

import java.util.*;

import static net.bramp.unsafe.MemoryUtils.buildJavaArg;
import static net.bramp.unsafe.MemoryUtils.memoryUsage;
import static net.bramp.unsafe.MemoryUtils.pidMemoryUsage;

/**
 * Finds out how much memory each benchmark would take
 */
public class MemoryTest {

    final static List<Integer> sizes = ImmutableList.of(4000, 8000, 400000, 800000, 40000000, 80000000);

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
                for (String benchmark : ArrayListBenchmark.benchmarks.keySet()) {

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
            AbstractListTest benchmark = ArrayListBenchmark.benchmarks.get(args[0]);
            int size = Integer.parseInt(args[1]);

            benchmark.setSize(size);
            benchmark.setup();

            long bytes = benchmark.bytes();

            System.out.printf("%s\t%d\t%d\t%s\t%s\n", args[0], size, bytes, memoryUsage(), pidMemoryUsage());
        }

    }
}
