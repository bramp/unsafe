package net.bramp.unsafe.collection;

import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

public abstract class AbstractListTest {
  protected final Random rand = new Random(0);
  protected int size;

  public void setSize(int size) {
    this.size = size;
  }

  public void setRandomSeed(long seed) {
    rand.setSeed(seed);
  }

  /**
   * Setups the test.
   */
  public abstract void setup() throws Exception;

  /**
   * Shuffles the test list.
   */
  public abstract void shuffle();

  /**
   * Sorts the test list.
   */
  public abstract void sort();

  /**
   * Iterates through the test list.
   * @param bh The blackhole to throw your data into.
   */
  public abstract void iterate(final Blackhole bh);

  /**
   * Iterates through the test list (if it is a InplaceList).
   * @param bh The blackhole to throw your data into.
   */
  public abstract void iterateInPlace(final Blackhole bh);

  /**
   * Calculates the size of the data structure under test.
   *
   * @return the size in bytes
   */
  public abstract long bytes();
}
