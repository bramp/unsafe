package net.bramp.unsafe;


public class LoopingByteUnsafeCopierTest extends AbstractUnsafeCopierTest {

  @Override
  protected UnsafeCopier createCopier(Class clazz) throws Exception {
    return new LoopingByteUnsafeCopier(unsafe, clazz);
  }

  @Override
  protected UnsafeCopier createCopier(long offset, long length) throws Exception {
    return new LoopingByteUnsafeCopier(unsafe, offset, length);
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
