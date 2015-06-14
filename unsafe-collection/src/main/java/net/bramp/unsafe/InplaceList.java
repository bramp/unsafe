package net.bramp.unsafe;

import java.util.List;

public interface InplaceList<E> extends List<E> {

    /**
     * Copies the element at index into dest
     * @param dest
     * @param index
     * @return
     */
    public E get(E dest, int index);

    public void swap(int i, int j);
}
