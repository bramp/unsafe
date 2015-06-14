package net.bramp.unsafe.examples;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class EightLongs implements Comparable<EightLongs> {
    public long a, b, c, d;
    public long e, f, g, h;

    public EightLongs(long a, long b, long c, long d, long e, long f, long g, long h) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
    }

    @Override
    public String toString() {
        return "EightLongs{" +
                    "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d +
                    "e=" + e + ", f=" + f + ", g=" + g + ", h=" + h +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EightLongs fourLongs = (EightLongs) o;
        return Objects.equal(a, fourLongs.a) &&
                Objects.equal(b, fourLongs.b) &&
                Objects.equal(c, fourLongs.c) &&
                Objects.equal(d, fourLongs.d) &&
                Objects.equal(e, fourLongs.e) &&
                Objects.equal(f, fourLongs.f) &&
                Objects.equal(g, fourLongs.g) &&
                Objects.equal(h, fourLongs.h);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(a, b, c, d, e, f, g, h);
    }

    public int compareTo(EightLongs o) {
        return ComparisonChain.start()
                .compare(a, o.a)
                .compare(b, o.b)
                .compare(c, o.c)
                .compare(d, o.d)
                .compare(e, o.e)
                .compare(f, o.f)
                .compare(g, o.g)
                .compare(h, o.h)
                .result();
    }
}
