package net.bramp.unsafe.collection;

import net.bramp.unsafe.UnsafeHelper;
import net.bramp.unsafe.sort.QuickSort;
import net.bramp.unsafe.sort.Shuffle;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;

public abstract class AbstractArrayListTest<T extends Comparable<T>> extends AbstractListTest {

    protected ArrayList<T> list;

    /**
     * Create a new instance of the test class, with random fields
     *
     * @return
     */
    public abstract T newInstance();

    public void setup() {
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

    public void shuffle() {
        Shuffle.shuffle(list, r);
    }

    public void sort() {
        Shuffle.shuffle(list, r);
        QuickSort.quickSort(list);
    }

    @Override
    public void iterateInPlace(Blackhole bh) {
        throw new RuntimeException("Test not applicable to ArrayList");
    }
}
