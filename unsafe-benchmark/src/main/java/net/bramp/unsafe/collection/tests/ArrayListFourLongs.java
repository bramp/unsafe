package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractArrayListTest;
import net.bramp.unsafe.examples.FourLongs;
import org.openjdk.jmh.infra.Blackhole;

public class ArrayListFourLongs extends AbstractArrayListTest<FourLongs> {

  @Override public FourLongs newInstance() {
    return new FourLongs(rand.nextLong(), rand.nextLong(), rand.nextLong(), rand.nextLong());
  }

  @Override public void iterate(final Blackhole bh) {
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(i).a);
    }
  }
}
