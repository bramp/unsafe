package net.bramp.unsafe.collection;

import net.bramp.unsafe.sort.QuickSort;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

import java.util.ArrayList;

public abstract class AbstractArrayListState<T extends Comparable<T>> extends AbstractListState {
    ArrayList<T> list;

    /**
     * Create a new instance of the test class, with random fields
     *
     * @return
     */
    public abstract T newInstance();

    @Setup
    public void setup() {
        r.setSeed(size); // TODO Use iteration some how

        list = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            // New Point for each entry
            list.add(newInstance());
        }
    }

    @Benchmark
    public void testListSort() {
        QuickSort.quickSort(list);
    }
}
