package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractArrayListTest;
import net.bramp.unsafe.examples.EightLongs;
import org.openjdk.jmh.infra.Blackhole;


public class ArrayListEightLongs extends AbstractArrayListTest<EightLongs> {

  @Override public EightLongs newInstance() {
    return new EightLongs(rand.nextLong(), rand.nextLong(), rand.nextLong(), rand.nextLong(),
        rand.nextLong(), rand.nextLong(), rand.nextLong(), rand.nextLong());
  }

  @Override public void iterate(final Blackhole bh) {
    for (int i = 0; i < size; i++) {
      bh.consume(list.get(i).a);
    }
  }
}
