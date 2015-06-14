package net.bramp.unsafe;

import sun.misc.Unsafe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract class that all copiers are based on
 */
public abstract class UnsafeCopier {

    final protected Unsafe unsafe;

    public UnsafeCopier(Unsafe unsafe) {
        this.unsafe = checkNotNull(unsafe);
    }

    /**
     * Copies from a src memory address into the fields of the dest object
     * TODO Change this so it is just copying src address to the dest fields
     * @param dest
     * @param src
     */
    public abstract void copy(Object dest, long src);

    // TODO
    //public abstract void copy(long dest, Object src, long srcOffset);
}
