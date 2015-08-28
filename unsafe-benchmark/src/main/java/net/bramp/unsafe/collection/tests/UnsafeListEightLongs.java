package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractUnsafeListTest;
import net.bramp.unsafe.examples.EightLongs;
import org.openjdk.jmh.infra.Blackhole;

public class UnsafeListEightLongs extends AbstractUnsafeListTest<EightLongs> {

  @Override public Class<EightLongs> testClass() {
    return EightLongs.class;
  }

  @Override public EightLongs newInstance(EightLongs obj) {
    obj.a = rand.nextLong();
    obj.b = rand.nextLong();
    obj.c = rand.nextLong();
    obj.d = rand.nextLong();
    obj.e = rand.nextLong();
    obj.f = rand.nextLong();
    obj.g = rand.nextLong();
    obj.h = rand.nextLong();
    return obj;
  }

  @Override public void iterate(final Blackhole bh) {
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(i).a);
    }
  }

  @Override public void iterateInPlace(final Blackhole bh) {
    final EightLongs p = new EightLongs(0, 0, 0, 0, 0, 0, 0, 0);
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(p, i).a);
    }
  }

}
