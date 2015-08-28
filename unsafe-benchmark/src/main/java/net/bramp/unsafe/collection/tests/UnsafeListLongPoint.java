package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractUnsafeListTest;
import net.bramp.unsafe.examples.LongPoint;
import org.openjdk.jmh.infra.Blackhole;

public class UnsafeListLongPoint extends AbstractUnsafeListTest<LongPoint> {

  public Class<LongPoint> testClass() {
    return LongPoint.class;
  }

  @Override public LongPoint newInstance(LongPoint obj) {
    obj.x = rand.nextLong();
    obj.y = rand.nextLong();
    return obj;
  }

  @Override public void iterate(final Blackhole bh) {
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(i).x);
    }
  }

  @Override public void iterateInPlace(final Blackhole bh) {
    final LongPoint p = new LongPoint(0, 0);
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(p, i).x);
    }
  }
}
