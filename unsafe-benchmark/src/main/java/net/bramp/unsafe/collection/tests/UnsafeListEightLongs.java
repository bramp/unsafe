package net.bramp.unsafe.collection.tests;

import net.bramp.unsafe.collection.AbstractUnsafeListTest;
import net.bramp.unsafe.examples.EightLongs;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class UnsafeListEightLongs extends AbstractUnsafeListTest<EightLongs> {

    @Override
    public Class<EightLongs> testClass() {
        return EightLongs.class;
    }

    @Override
    public EightLongs newInstance(EightLongs p) {
        p.a = r.nextLong();
        p.b = r.nextLong();
        p.c = r.nextLong();
        p.d = r.nextLong();
        p.e = r.nextLong();
        p.f = r.nextLong();
        p.g = r.nextLong();
        p.h = r.nextLong();
        return p;
    }

    public void iterate(final Blackhole bh) {
        for (int i = 0; i < size; i++) {
            bh.consume(list.get(i).a);
        }
    }

    public void iterateInPlace(final Blackhole bh) {
        final EightLongs p = new EightLongs(0, 0, 0, 0, 0, 0, 0, 0);
        for (int i = 0; i < size; i++) {
            bh.consume(list.get(p, i).a);
        }
    }

}
