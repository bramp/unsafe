package net.bramp.unsafe.collection;

import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

public abstract class AbstractListTest {
    final protected Random r = new Random(0);
    protected int size;

    public void setSize(int size) {
        this.size = size;
    }
    public void setRandomSeed(long seed) {
        this.r.setSeed(seed);
    }

    public abstract void setup() throws Exception;
    public abstract void shuffle();
    public abstract void sort();
    public abstract void iterate(final Blackhole bh);
    public abstract void iterateInPlace(final Blackhole bh);

    /**
     * Returns the size of the internal data structures
     */
    public abstract long bytes();
}
