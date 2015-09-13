package net.bramp.unsafe.examples;

import com.google.common.collect.ComparisonChain;

/**
 * Simple test class which stores two ints, x and y.
 *
 * <pre>
 * 0x00000000: 01 00 00 00 00 00 00 00  A1 C3 62 DF 01 00 00 00
 * 0x00000010: 02 00 00 00 00 00 00 00
 * </pre>
 */
public class IntPoint implements Comparable<IntPoint> {
  public final int x;
  public final int y;

  public IntPoint(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "IntPoint(" + x + "," + y + ")";
  }

  @Override
  public int compareTo(IntPoint obj) {
    return ComparisonChain.start().compare(x, obj.x).compare(y, obj.y).result();
  }
}
