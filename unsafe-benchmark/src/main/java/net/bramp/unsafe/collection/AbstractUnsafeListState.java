package net.bramp.unsafe.collection;

import net.bramp.unsafe.UnsafeArrayList;
import net.bramp.unsafe.sort.InplaceQuickSort;
import net.bramp.unsafe.sort.Shuffle;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public abstract class AbstractUnsafeListState<T extends Comparable<T>> extends AbstractListState {
    UnsafeArrayList<T> list;

    /**
     * Class used within the generic list
     *
     * @return
     */
    public abstract Class<T> testClass();

    /**
     * Create a new instance of the test class, with random fields
     *
     * @param obj
     * @return
     */
    public abstract T newInstance(T obj);

    @Setup
    public void setup() throws IllegalAccessException, InstantiationException {
        r.setSeed(size);

        list = new UnsafeArrayList<T>(testClass(), size);
        final T p = testClass().newInstance();
        for (int i = 0; i < size; i++) {
            // Reuse single point (since it gets copied into array)
            list.add(newInstance(p));
        }
    }

    @Benchmark
    public void testListSortInPlace() {
        Shuffle.shuffle(list, r); // We shuffle to make the sort different each time
        InplaceQuickSort.quickSort(list);
    }
}
