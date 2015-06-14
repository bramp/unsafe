package net.bramp.unsafe.examples;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class FourLongs implements Comparable<FourLongs> {
    public long a, b, c, d;

    public FourLongs(long a, long b, long c, long d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public String toString() {
        return "FourLongs{" + "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FourLongs fourLongs = (FourLongs) o;
        return Objects.equal(a, fourLongs.a) &&
                Objects.equal(b, fourLongs.b) &&
                Objects.equal(c, fourLongs.c) &&
                Objects.equal(d, fourLongs.d);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(a, b, c, d);
    }

    public int compareTo(FourLongs o) {
        return ComparisonChain.start()
                .compare(a, o.a)
                .compare(b, o.b)
                .compare(c, o.c)
                .compare(d, o.d)
                .result();
    }
}
