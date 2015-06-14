package net.bramp.unsafe.examples;

import com.google.common.collect.ComparisonChain;

/**
 * 0x00000000: 01 00 00 00 00 00 00 00  A1 C3 62 DF 01 00 00 00
 * 0x00000010: 02 00 00 00 00 00 00 00
 */
public class IntPoint implements Comparable<IntPoint> {
    public final int x;
    public final int y;

    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "IntPoint(" + x + "," + y + ")";
    }

    public int compareTo(IntPoint o) {
        return ComparisonChain.start()
                .compare(x, o.x)
                .compare(y, o.y)
                .result();
    }
}
