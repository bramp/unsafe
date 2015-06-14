package net.bramp.unsafe.collection;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Random;

@State(Scope.Thread)
public abstract class AbstractListState {
    final Random r = new Random(0);

    @Param("40000000")
    public int size;
}
