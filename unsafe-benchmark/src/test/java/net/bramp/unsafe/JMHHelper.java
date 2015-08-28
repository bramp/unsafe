package net.bramp.unsafe;

import org.openjdk.jmh.infra.Blackhole;

/**
 * Nasty hack to make a JMH Blackhole.
 *
 * <p>The Blackhole constructor checks the call stack to ensure only jmh code is constructing it.
 * {@code
 * if (el.getMethodName().startsWith("_jmh_tryInit_") && el.getClassName().contains("generated"))
 * }
 */
@SuppressWarnings({"abbreviationaswordinname"})
public class JMHHelper {

  @SuppressWarnings({"typename", "methodname"})
  private static class Blackhole_generated {
    static Blackhole _jmh_tryInit_NewBlackhole() {
      return new Blackhole();
    }
  }

  /**
   * Creates a new JMH Blackhole object.
   *
   * @return a JMH Blackhole object
   */
  public static Blackhole newBlackhole() {
    return Blackhole_generated._jmh_tryInit_NewBlackhole();
  }

}
