package net.bramp.unsafe;

import sun.misc.Unsafe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Abstract class that all copiers are based on.
 */
public abstract class UnsafeCopier {

  protected final Unsafe unsafe;

  public UnsafeCopier(Unsafe unsafe) {
    this.unsafe = checkNotNull(unsafe);
  }

  /**
   * Copies from a src memory address into the offset of the dest object.
   *
   * @param dest dest object
   * @param src src must be 8 byte aligned
   */
  public abstract void copy(Object dest, long src);
}
