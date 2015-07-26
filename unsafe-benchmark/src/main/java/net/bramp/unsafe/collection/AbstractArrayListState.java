package net.bramp.unsafe.collection;

import net.bramp.unsafe.UnsafeHelper;
import net.bramp.unsafe.sort.QuickSort;
import net.bramp.unsafe.sort.Shuffle;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
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

    @Setup(Level.Trial)
    public void setup() {
        r.setSeed(size); // TODO Use iteration some how

        list = new ArrayList<T>(size);
        for (int i = 0; i < size; i++) {
            // New Point for each entry
            list.add(newInstance());
        }
    }

    @Override
    public long bytes() {
        int size = list.size();
        int referenceSize = 4; // TODO calculate this correctly

        if (size > 0) {
            long elementSize = UnsafeHelper.sizeOf(list.get(0));
            return (referenceSize + elementSize) * size;
        }
        return 0;
    }

    @Setup(Level.Iteration)
    public void shuffle() throws IllegalAccessException, InstantiationException {
        // We shuffle to make the sort different each time, and to ensure the list starts randomised
        Shuffle.shuffle(list, r);
    }

    @Benchmark
    public void testListSort() {
        // We shuffle to make the sort different each time, and to ensure the list starts randomised
        Shuffle.shuffle(list, r);
        QuickSort.quickSort(list);
    }
}
