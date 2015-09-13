package net.bramp.unsafe;

import sun.misc.Unsafe;

public class LoopingForUnsafeCopier extends UnsafeCopier {

  public static final int COPY_STRIDE = 8;

  final long offset;
  final long count;

  /**
   * Copier designed to copy to a dest offset, and for length bytes.
   *
   * @param unsafe
   * @param offset
   * @param length
   */
  public LoopingForUnsafeCopier(Unsafe unsafe, long offset, long length) {
    super(unsafe);

    Preconditions.checkArgument(offset >= 0, "Offset must be >= 0");
    Preconditions.checkArgument(length >= 0, "Length must be >= 0");
    Preconditions.checkArgument(length % COPY_STRIDE == 0, "Lengths must be a multiple of 8 bytes");

    this.offset = offset;
    this.count = length / COPY_STRIDE;
  }

  public LoopingForUnsafeCopier(Unsafe unsafe, Class clazz) {
    this(unsafe, UnsafeHelper.firstFieldOffset(clazz), UnsafeHelper.sizeOfFields(clazz));
  }

  @Override
  public void copy(Object dest, long src) {
    Preconditions.checkArgument(src % COPY_STRIDE == 0, "Source address must be 8 byte aligned");

    long offset = this.offset;
    for (int i = 0; i < count; i++) {
      unsafe.putLong(dest, offset, unsafe.getLong(src));
      offset += COPY_STRIDE;
      src += COPY_STRIDE;
    }
  }
}
