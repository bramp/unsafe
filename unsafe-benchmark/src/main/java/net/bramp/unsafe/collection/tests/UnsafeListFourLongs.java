package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractUnsafeListTest;
import net.bramp.unsafe.examples.FourLongs;
import org.openjdk.jmh.infra.Blackhole;


public class UnsafeListFourLongs extends AbstractUnsafeListTest<FourLongs> {

  @Override public Class<FourLongs> testClass() {
    return FourLongs.class;
  }

  @Override public FourLongs newInstance(FourLongs obj) {
    obj.a = rand.nextLong();
    obj.b = rand.nextLong();
    obj.c = rand.nextLong();
    obj.d = rand.nextLong();
    return obj;
  }

  @Override public void iterate(final Blackhole bh) {
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(i).a);
    }
  }

  @Override public void iterateInPlace(final Blackhole bh) {
    final FourLongs p = new FourLongs(0, 0, 0, 0);
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(p, i).a);
    }
  }


}
