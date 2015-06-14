package net.bramp.unsafe.examples;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * 0x00000000: 01 00 00 00 00 00 00 00  AE C4 62 DF 00 00 00 00
 * 0x00000010: 01 00 00 00 00 00 00 00  02 00 00 00 00 00 00 00
 */
public class LongPoint implements Comparable<LongPoint> {
    public long x;
    public long y;

    public LongPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "Point(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongPoint longPoint = (LongPoint) o;
        return Objects.equal(x, longPoint.x) &&
                 Objects.equal(y, longPoint.y);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    public int compareTo(LongPoint o) {
        return ComparisonChain.start()
            .compare(x, o.x)
            .compare(y, o.y)
            .result();
    }
}
