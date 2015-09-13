package net.bramp.unsafe;

import sun.misc.Unsafe;

/**
 * UnsafeCopier copying one byte at a time.
 */
public class LoopingByteUnsafeCopier extends UnsafeCopier {

  final long offset;
  final long offset_end;

  public LoopingByteUnsafeCopier(Unsafe unsafe, long offset, long length) {
    super(unsafe);

    Preconditions.checkArgument(offset >= 0, "Offset must be >= 0");
    Preconditions.checkArgument(length >= 0, "Length must be >= 0");

    this.offset = offset;
    this.offset_end = offset + length;
  }

  public LoopingByteUnsafeCopier(Unsafe unsafe, Class clazz) {
    this(unsafe, UnsafeHelper.firstFieldOffset(clazz), UnsafeHelper.sizeOfFields(clazz));
  }

  @Override
  public void copy(Object dest, long src) {
    long offset = this.offset;

    while (offset < offset_end) {
      unsafe.putByte(dest, offset, unsafe.getByte(src));
      offset++;
      src++;
    }
  }
}
