package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractUnsafeListTest;
import net.bramp.unsafe.examples.FourLongs;
import org.openjdk.jmh.infra.Blackhole;


public class UnsafeListFourLongs extends AbstractUnsafeListTest<FourLongs> {

    @Override
    public Class<FourLongs> testClass() {
        return FourLongs.class;
    }

    @Override
    public FourLongs newInstance(FourLongs p) {
        p.a = r.nextLong();
        p.b = r.nextLong();
        p.c = r.nextLong();
        p.d = r.nextLong();
        return p;
    }
    public void iterate(final Blackhole bh) {
        for (int i = 0; i < size; i++) {
            bh.consume(list.get(i).a);
        }
    }

    public void iterateInPlace(final Blackhole bh) {
        final FourLongs p = new FourLongs(0, 0, 0, 0);
        for (int i = 0; i < size; i++) {
            bh.consume(list.get(p, i).a);
        }
    }


}
