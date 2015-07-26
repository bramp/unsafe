package net.bramp.unsafe;

import com.google.common.base.Throwables;
import sun.misc.Unsafe;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;

/**
 * ArrayList implemented using Unsafe operations
 * @See http://www.docjar.com/docs/api/sun/misc/Unsafe.html
 *
 * This is not thread safe
 */
public class UnsafeArrayList<T> extends AbstractList<T> implements InplaceList<T>, RandomAccess {

    private static final int DEFAULT_CAPACITY = 10;

    final Class<T> type;
    final long firstFieldOffset; // Offset to the first field
    final long elementSize;      // Size of all the fields in the object

    final T tmp;

    final Unsafe unsafe;
    final UnsafeCopier copier;

    private long base = 0;

    private int size = 0;
    private int capacity = 0;

    /**
     *
     * @param type Must match the parameterised type
     */
    public UnsafeArrayList(Class<T> type) {
        this(type, DEFAULT_CAPACITY);
    }

    public UnsafeArrayList(Class<T> type, Collection<? extends T> c) {
        this(type, c.size());
        addAll(c);
    }

    public UnsafeArrayList(Class<T> type, int initialCapacity) {
        this.type = type;
        this.firstFieldOffset = UnsafeHelper.firstFieldOffset(type);
        this.elementSize      = UnsafeHelper.sizeOf(type) - firstFieldOffset;
        this.unsafe           = UnsafeHelper.getUnsafe();

        // TODO Check if the class has any non-primitive fields. If so, throw an exception.
        // new RuntimeException("Storing classes which contain references is dangerous, as the garbage collector will lose track of them thus is not supported")

        try {
            copier = new UnrolledUnsafeCopierBuilder()
                    .offset(firstFieldOffset)
                    .length(elementSize)
                    .build(this.unsafe);

            // Temp working space
            tmp = (T) unsafe.allocateInstance(type);

        } catch (Exception e) {
            throw Throwables.propagate(e);
        }

        setCapacity(initialCapacity);
    }

    private void setCapacity(int capacity) {
        this.capacity = capacity;
        base = unsafe.reallocateMemory(base, elementSize * capacity);
    }

    public void ensureCapacity(int capacity) {
        // If we don't have enough room, grow by 2x
        if (capacity > this.capacity) {
            setCapacity(capacity + (capacity >> 1));
        }
    }

    protected void checkBounds(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }


    @Override
    public T get(int index) {
        try {
            return get((T) unsafe.allocateInstance(type), index);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private long offset(int index) {
        return base + index * elementSize;
    }

    /**
     * Gets and copies into dest
     * @param dest
     * @param index
     * @return
     */
    public T get(T dest, int index) {
        checkBounds(index);
        // We would rather do
        //   UnsafeHelper.copyMemory(null, offset(index), dest, firstFieldOffset, elementSize);
        // but this is unsupported by the Unsafe class
        copier.copy(dest, offset(index));
        return dest;
    }

    @Override
    public T set(int index, T element) {
        checkBounds(index);

        // TODO checkIsX()
        // TODO If we try and store an subclass of type, we will slice of some fields. Instead we should throw an exception.

        unsafe.copyMemory(element, firstFieldOffset, null, offset(index), elementSize);

        return null; // TODO
    }

    /**
     * Swaps two elements
     * TODO TEST
     * @param i
     * @param j
     */
    public void swap(int i, int j) {
        if (i == j)
            return;

        copier.copy(tmp, offset(i));
        unsafe.copyMemory(null, offset(j), null, offset(i), elementSize);
        unsafe.copyMemory(tmp, firstFieldOffset, null, offset(j), elementSize);
    }

    @Override
    public void add(int index, T element) {
        if (index != size) {
            throw new RuntimeException("We only support adding at the end");
        }

        final int oldSize = size++;
        ensureCapacity(size);
        set(oldSize, element);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the size of this object in bytes
     * @return
     */
    public long bytes() {
        return UnsafeHelper.sizeOf(this) + elementSize * capacity;
    }
}
