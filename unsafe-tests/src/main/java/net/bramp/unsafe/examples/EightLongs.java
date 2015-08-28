package net.bramp.unsafe.examples;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class EightLongs implements Comparable<EightLongs> {

  public long a, b, c, d;
  public long e, f, g, h;

  /**
   * Creates a new EightLongs.
   *
   * @param a
   * @param b
   * @param c
   * @param d
   * @param e
   * @param f
   * @param g
   * @param h
   */
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

  @Override public String toString() {
    return "EightLongs{"
        + "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d
        + "e=" + e + ", f=" + f + ", g=" + g + ", h=" + h
        + '}';
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    EightLongs fourLongs = (EightLongs) obj;
    return Objects.equal(a, fourLongs.a)
        && Objects.equal(b, fourLongs.b)
        && Objects.equal(c, fourLongs.c)
        && Objects.equal(d, fourLongs.d)
        && Objects.equal(e, fourLongs.e)
        && Objects.equal(f, fourLongs.f)
        && Objects.equal(g, fourLongs.g)
        && Objects.equal(h, fourLongs.h);
  }

  @Override public int hashCode() {
    return Objects.hashCode(a, b, c, d, e, f, g, h);
  }

  public int compareTo(EightLongs obj) {
    return ComparisonChain.start()
        .compare(a, obj.a).compare(b, obj.b)
        .compare(c, obj.c).compare(d, obj.d)
        .compare(e, obj.e).compare(f, obj.f)
        .compare(g, obj.g).compare(h, obj.h)
        .result();
  }
}
