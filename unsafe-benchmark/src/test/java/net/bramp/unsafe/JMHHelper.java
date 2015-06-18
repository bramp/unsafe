package net.bramp.unsafe;

import org.openjdk.jmh.infra.Blackhole;

public class JMHHelper {

    /**
     * Nasty hack of a class to make a blackhole.
     * The Blackhole constructor checks the call stack to ensure only jmh code is constructing it.
     * if (el.getMethodName().startsWith("_jmh_tryInit_") && el.getClassName().contains("generated"))
     */
    private static class Blackhole_generated {
        static Blackhole _jmh_tryInit_NewBlackhole() {
            return new Blackhole();
        }
    }

    /**
     * Creates a new JMH Blackhole object
     * @return
     */
    public static Blackhole newBlackhole() {
        return Blackhole_generated._jmh_tryInit_NewBlackhole();
    }

}
