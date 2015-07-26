package net.bramp.unsafe.collection;

import org.openjdk.jmh.annotations.*;

import java.util.Random;

@State(Scope.Thread)
public abstract class AbstractListState implements BenchmarkInterface {
    final Random r = new Random(0);

    @Param("40000000")
    public int size;

    public void setSize(int size) {
        this.size = size;
    }
}
