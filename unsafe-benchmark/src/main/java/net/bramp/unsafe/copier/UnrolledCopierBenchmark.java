package net.bramp.unsafe.copier;

import net.bramp.unsafe.UnrolledUnsafeCopierBuilder;
import net.bramp.unsafe.UnsafeCopier;
import net.bramp.unsafe.UnsafeHelper;
import net.bramp.unsafe.examples.FourLongs;

import com.google.common.base.Preconditions;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import sun.misc.Unsafe;

import java.lang.reflect.InvocationTargetException;


/**
 * <pre>
 * Benchmark                                  Mode  Cnt    Score    Error   Units
 * UnrolledCopierBenchmark.testHandUnrolled  thrpt   25  430.376 ± 27.448  ops/us
 * UnrolledCopierBenchmark.testLoop          thrpt   25  218.056 ± 11.123  ops/us
 * UnrolledCopierBenchmark.testUnrolled      thrpt   25  437.139 ± 22.811  ops/us
 * UnrolledCopierBenchmark.testDuff          thrpt   20  192.119 ± 9.602   ops/us
 * </pre>
 */
public class UnrolledCopierBenchmark {

  public static final int COPY_STRIDE = 8;

  abstract static class UnsafeState {
    static final Unsafe unsafe = UnsafeHelper.getUnsafe();

    // Must be multiple of COPY_STRIDE
    static final long destOffset = UnsafeHelper.firstFieldOffset(FourLongs.class);
    static final long destSize = UnsafeHelper.sizeOf(FourLongs.class) - destOffset;

    FourLongs dest;
    long src;

    // The class under test
    protected UnsafeCopier copier;

    @Setup public void setupMemory() {
      src = unsafe.allocateMemory(destSize);
      dest = new FourLongs(0, 0, 0, 0);

      // Write 1, 2, 3, 4, into the memory
      int i = 0;
      for (int offset = 0; offset < destSize; offset += COPY_STRIDE) {
        unsafe.putLong(src + offset, ++i);
      }
    }

    @TearDown public void cleanupMemory() {
      unsafe.freeMemory(src);

      // Ensure all memory is good.
      Preconditions
          .checkState(new FourLongs(1, 2, 3, 4).equals(dest), "Incorrect test object: %s", dest);
    }

    // TODO add check in teardown to ensure the dest memory is correct
    @Benchmark public void test() {
      copier.copy(dest, src);
    }
  }


  @State(Scope.Thread) public static class UnrolledState extends UnsafeState {

    @Setup public void setup()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
      copier = new UnrolledUnsafeCopierBuilder().of(FourLongs.class).build(unsafe);
    }
  }


  @State(Scope.Thread) public static class LoopState extends UnsafeState {
    @Setup public void setup()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
      // Construct a copier, that will look 4 times
      copier = new UnsafeCopier(unsafe) {

        final long dstEnd = destOffset + destSize;

        @Override public void copy(Object dest, long src) {
          long dstOffset = destOffset;

          while (dstOffset < dstEnd) {
            unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
            dstOffset += COPY_STRIDE;
            src += COPY_STRIDE;
          }
        }
      };
    }
  }


  @State(Scope.Thread) public static class HandUnrolledState extends UnsafeState {

    @Setup public void setup()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
      Preconditions.checkState(destSize / COPY_STRIDE == 4);

      copier = new UnsafeCopier(unsafe) {

        @Override public void copy(Object dest, long src) {
          long dstOffset = destOffset;

          unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
          dstOffset += COPY_STRIDE;
          src += COPY_STRIDE;

          unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
          dstOffset += COPY_STRIDE;
          src += COPY_STRIDE;

          unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
          dstOffset += COPY_STRIDE;
          src += COPY_STRIDE;

          unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
        }
      };
    }
  }


  // TODO Fix DuffState it doesn't work
  @State(Scope.Thread) public static class DuffState extends UnsafeState {

    @Setup public void setup()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException,
        IllegalAccessException {
      Preconditions.checkState(destSize / COPY_STRIDE == 4);

      final int iterations = (int) (destSize / COPY_STRIDE);

      copier = new UnsafeCopier(unsafe) {

        @Override public void copy(Object dest, long src) {
          long dstOffset = destOffset;

          int n = iterations / 8;
          int s = iterations % 8;

          do {
            switch (s) { // iterations % 8
              case 0:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
                // fall through
              case 7:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
                // fall through
              case 6:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
                // fall through
              case 5:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
                // fall through
              case 4:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
                // fall through
              case 3:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
                // fall through
              case 2:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
                // fall through
              case 1:
                unsafe.putLong(dest, dstOffset, unsafe.getLong(src));
                dstOffset += COPY_STRIDE;
                src += COPY_STRIDE;
            }
          } while (--n > 0);
        }
      };
    }
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(UnrolledCopierBenchmark.class.getSimpleName())
        .mode(Mode.Throughput).warmupIterations(1).warmupTime(TimeValue.seconds(10))
        .measurementIterations(5).measurementTime(TimeValue.seconds(10)).forks(5)
            //.addProfiler(StackProfiler.class)
            //.addProfiler(GCProfiler.class)
        .build();

    new Runner(opt).run();
  }
}
