package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractArrayListTest;
import net.bramp.unsafe.examples.LongPoint;
import org.openjdk.jmh.infra.Blackhole;


public class ArrayListLongPoint extends AbstractArrayListTest<LongPoint> {

  @Override public LongPoint newInstance() {
    return new LongPoint(rand.nextLong(), rand.nextLong());
  }

  @Override public void iterate(final Blackhole bh) {
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(i).x);
    }
  }
}
