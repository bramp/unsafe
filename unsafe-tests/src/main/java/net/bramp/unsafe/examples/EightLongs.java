package net.bramp.unsafe.examples;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class EightLongs implements Comparable<EightLongs> {

  public long a, b, c, d;
  public long e, f, g, h;

  public EightLongs() {
    this(0,0,0,0,0,0,0,0);
  }

  /**
   * Creates a new EightLongs.
   *
   * @param a 1st field
   * @param b 2nd field
   * @param c 3rd field
   * @param d 4th field
   * @param e 5th field
   * @param f 6th field
   * @param g 7th field
   * @param h 8th field
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
    return "EightLongs{" +
        "a=0x" + Long.toHexString(a) + ", " +
        "b=0x" + Long.toHexString(b) + ", " +
        "c=0x" + Long.toHexString(c) + ", " +
        "d=0x" + Long.toHexString(d) + ", " +
        "e=0x" + Long.toHexString(e) + ", " +
        "f=0x" + Long.toHexString(f) + ", " +
        "g=0x" + Long.toHexString(g) + ", " +
        "h=0x" + Long.toHexString(h) + '}';
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    EightLongs eightLongs = (EightLongs) obj;
    return Objects.equal(a, eightLongs.a)
        && Objects.equal(b, eightLongs.b)
        && Objects.equal(c, eightLongs.c)
        && Objects.equal(d, eightLongs.d)
        && Objects.equal(e, eightLongs.e)
        && Objects.equal(f, eightLongs.f)
        && Objects.equal(g, eightLongs.g)
        && Objects.equal(h, eightLongs.h);
  }

  @Override public int hashCode() {
    return Objects.hashCode(a, b, c, d, e, f, g, h);
  }

  @Override public int compareTo(EightLongs obj) {
    return ComparisonChain.start()
        .compare(a, obj.a).compare(b, obj.b)
        .compare(c, obj.c).compare(d, obj.d)
        .compare(e, obj.e).compare(f, obj.f)
        .compare(g, obj.g).compare(h, obj.h)
        .result();
  }
}
