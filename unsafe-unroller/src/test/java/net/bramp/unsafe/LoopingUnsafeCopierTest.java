package net.bramp.unsafe;


public class LoopingUnsafeCopierTest extends AbstractUnsafeCopierTest {

  @Override
  protected UnsafeCopier createCopier(Class clazz) throws Exception {
    return new LoopingUnsafeCopier(unsafe, clazz);
  }

  @Override
  protected UnsafeCopier createCopier(long offset, long length) throws Exception {
    return new LoopingUnsafeCopier(unsafe, offset, length);
  }

  @Override
  protected boolean supportsUnaligned() {
    return false;
  }

  @Override
  protected int stride() {
    return LoopingUnsafeCopier.COPY_STRIDE;
  }
}
