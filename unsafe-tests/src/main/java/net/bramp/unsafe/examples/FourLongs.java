package net.bramp.unsafe.examples;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class FourLongs implements Comparable<FourLongs> {
  public long a, b, c, d;

  /**
   * Creates a new FourLongs.
   *
   * @param a 1st field
   * @param b 2nd field
   * @param c 3rd field
   * @param d 4th field
   */
  public FourLongs(long a, long b, long c, long d) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

  @Override public String toString() {
    return "FourLongs{" + "a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + '}';
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    FourLongs fourLongs = (FourLongs) obj;
    return Objects.equal(a, fourLongs.a)
        && Objects.equal(b, fourLongs.b)
        && Objects.equal(c, fourLongs.c)
        && Objects.equal(d, fourLongs.d);
  }

  @Override public int hashCode() {
    return Objects.hashCode(a, b, c, d);
  }

  @Override public int compareTo(FourLongs obj) {
    return ComparisonChain.start()
        .compare(a, obj.a)
        .compare(b, obj.b)
        .compare(c, obj.c)
        .compare(d, obj.d)
        .result();
  }
}
