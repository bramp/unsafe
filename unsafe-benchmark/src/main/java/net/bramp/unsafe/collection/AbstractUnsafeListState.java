package net.bramp.unsafe.collection;

import net.bramp.unsafe.UnsafeArrayList;
import net.bramp.unsafe.UnsafeHelper;
import net.bramp.unsafe.sort.InplaceQuickSort;
import net.bramp.unsafe.sort.Shuffle;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
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

    @Setup(Level.Trial)
    public void setup() throws IllegalAccessException, InstantiationException {
        r.setSeed(size);

        final Class clazz = testClass();
        final T p = (T)UnsafeHelper.getUnsafe().allocateInstance(clazz); // Create a tmp instance

        list = new UnsafeArrayList<T>(clazz, size);
        for (int i = 0; i < size; i++) {
            // Reuse single point (since it gets copied into array)
            list.add(newInstance(p));
        }
    }

    @Setup(Level.Iteration)
    public void shuffle() throws IllegalAccessException, InstantiationException {
        // We shuffle to make the sort different each time, and to ensure the list starts randomised
        Shuffle.shuffleInplace(list, r);
    }


    @Benchmark
    public void testListSortInPlace() {
        InplaceQuickSort.quickSort(list);
    }
}
