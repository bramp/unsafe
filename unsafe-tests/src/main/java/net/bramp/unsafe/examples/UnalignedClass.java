package net.bramp.unsafe.examples;

import com.google.common.collect.ComparisonChain;

/**
 * Class whose fields are unaligned.
 *
 * <pre>
 * new UnalignedClass(-1)
 * 0x00000000: 01 00 00 00 00 00 00 00  C1 0C 65 DF FF FF 00 00
 *                                                  ^^ ^^ char c
 * </pre>
 */
public class UnalignedClass implements Comparable<UnalignedClass> {
  public final char c;

  public UnalignedClass(char c) {
    this.c = c;
  }

  public UnalignedClass(int i) {
    this.c = (char)i;
  }

  @Override
  public String toString() {
    return "UnalignedClass{c=" + (int)c + "}";
  }

  @Override
  public int compareTo(UnalignedClass obj) {
    return ComparisonChain.start().compare(c, obj.c).result();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof UnalignedClass && c == ((UnalignedClass)obj).c;
  }
}
