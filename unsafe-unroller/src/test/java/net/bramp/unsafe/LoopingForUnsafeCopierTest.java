package net.bramp.unsafe;


public class LoopingForUnsafeCopierTest extends AbstractUnsafeCopierTest {

  @Override
  protected UnsafeCopier createCopier(Class clazz) throws Exception {
    return new LoopingForUnsafeCopier(unsafe, clazz);
  }

  @Override
  protected UnsafeCopier createCopier(long offset, long length) throws Exception {
    return new LoopingForUnsafeCopier(unsafe, offset, length);
  }

  @Override
  protected boolean supportsUnaligned() {
    return false;
  }

  @Override
  protected int stride() {
    return LoopingForUnsafeCopier.COPY_STRIDE;
  }
}
