package net.bramp.unsafe;

public class UnrolledUnsafeCopierTest extends AbstractUnsafeCopierTest {

  @Override
  protected UnsafeCopier createCopier(Class clazz) throws Exception {
    return new UnrolledUnsafeCopierBuilder()
        .of(clazz)
        .build(unsafe);
  }

  @Override
  protected UnsafeCopier createCopier(long offset, long length) throws Exception {
    return new UnrolledUnsafeCopierBuilder()
        .offset(offset)
        .length(length)
        .build(unsafe);
  }

  @Override
  protected boolean supportsUnaligned() {
    return true;
  }

  @Override
  protected int stride() {
    return 1;
  }
}
