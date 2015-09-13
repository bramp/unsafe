package net.bramp.unsafe;

import net.bramp.unsafe.examples.FourLongs;
import net.bramp.unsafe.examples.UnalignedClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.InvocationTargetException;

import static net.bramp.unsafe.UnsafeHelper.firstFieldOffset;
import static net.bramp.unsafe.UnsafeHelper.sizeOfFields;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * TODO Test the write doesn't overrun or underun
 */
public abstract class AbstractUnsafeCopierTest {

  final static Unsafe unsafe = UnsafeHelper.getUnsafe();
  final static long fourLongOffset = firstFieldOffset(FourLongs.class);

  /**
   * Copies the objects fields into a allocated space in memory
   *
   * @param src
   * @return The address of the memory, must be freed with unsafe.freeMemory(..)
   */
  protected static long copyToMemory(Object src, long srcOffset) {
    final long offset = UnsafeHelper.firstFieldOffset(src) + srcOffset;
    final long size = UnsafeHelper.sizeOfFields(src) - srcOffset;
    final long dest = unsafe.allocateMemory(size);

    unsafe.copyMemory(src, offset, null, dest, size);
    return dest;
  }

  /**
   * Uses copier to copy from src to dest
   * @param copier
   * @param src
   * @param dest
   * @param <T>
   * @return
   */
  protected static<T> T test(UnsafeCopier copier, T src, T dest) {
    return test(copier, src, 0, dest);
  }

  /**
   * Uses copier to copy from src to dest, starting at a offset within src.
   * @param copier
   * @param src
   * @param srcOffset
   * @param dest
   * @param <T>
   * @return
   */
  protected static<T> T test(UnsafeCopier copier, T src, long srcOffset, T dest) {
    final long srcMemory = copyToMemory(src, srcOffset);
    try {
      copier.copy(dest, srcMemory);
    } finally {
      unsafe.freeMemory(srcMemory);
    }
    return dest;
  }

  protected abstract UnsafeCopier createCopier(Class clazz) throws Exception;
  protected abstract UnsafeCopier createCopier(long offset, long length) throws Exception;
  protected abstract boolean supportsUnaligned();
  protected abstract int stride();

  /**
   * Ensure the test classes looks how we want
   */
  @BeforeClass
  public static void checkAssumptions() {
    // FourLongs is aligned and a multiple of 8 bytes
    assertTrue(firstFieldOffset(FourLongs.class) % 8 == 0);
    assertEquals(4 * 8, sizeOfFields(FourLongs.class));

    // UnalignedClass is not 8 byte aligned, and not a multiple of 8.
    assertTrue(firstFieldOffset(UnalignedClass.class) % 8 != 0);
    assertTrue(sizeOfFields(UnalignedClass.class) % 8 != 0);
  }

  @Test public void testZeroIterations() throws Exception {
    UnsafeCopier copier = createCopier(fourLongOffset, 0);
    FourLongs dest = test(copier, new FourLongs(1, 2, 3, 4), new FourLongs());
    assertEquals("Dest should remain unchanged with zero copies", new FourLongs(0, 0, 0, 0), dest);
  }

  @Test public void testOneIterations() throws Exception {
    UnsafeCopier copier = createCopier(fourLongOffset, 8);
    FourLongs dest = test(copier, new FourLongs(1, 2, 3, 4), new FourLongs());
    assertEquals("Dest should have just the first field altered", new FourLongs(1, 0, 0, 0), dest);
  }

  @Test public void testTwoIterations() throws Exception {
    UnsafeCopier copier = createCopier(fourLongOffset, 16);
    FourLongs dest = test(copier, new FourLongs(1, 2, 3, 4), new FourLongs());
    assertEquals("Dest should have just the two fields altered", new FourLongs(1, 2, 0, 0), dest);
  }

  @Test public void testFourIterations() throws Exception {
    UnsafeCopier copier = createCopier(FourLongs.class);
    FourLongs dest = test(copier, new FourLongs(1, 2, 3, 4), new FourLongs());
    assertEquals("Dest should have all the fields altered", new FourLongs(1, 2, 3, 4), dest);
  }

  @Test public void testSmallClass() throws Exception {
    Assume.assumeTrue(UnsafeHelper.sizeOfFields(UnalignedClass.class) % stride() == 0);

    UnsafeCopier copier = createCopier(UnalignedClass.class);
    UnalignedClass src = new UnalignedClass(0xabcd);
    UnalignedClass dest = test(copier, src, new UnalignedClass(-1));
    assertEquals("Dest should equal src", src, dest);
    assertEquals("Should have identical hexdumps", UnsafeHelper.hexDump(src),
        UnsafeHelper.hexDump(dest));
  }

  @Test public void testString() throws Exception {
    Assume.assumeTrue(UnsafeHelper.sizeOfFields(String.class) % stride() == 0);

    UnsafeCopier copier = createCopier(String.class);
    String src = new String("Test String").intern();
    String dest = test(copier, src, new String()); // Ensure we use "new String()" otherwise we replace the canonical "" instance.
    assertEquals("Dest should equal src", src, dest);
    assertEquals("Should have identical hexdumps", UnsafeHelper.hexDump(src), UnsafeHelper.hexDump(dest));
  }

  public static long all(long b) {
    long result = 0;
    while (b != 0) {
      result |= b;
      b = b << 4;
    }
    return result;
  }

  @Test public void testAll() throws Exception {
    assertEquals(0x1111111111111111L, all(1));
    assertEquals(0x2222222222222222L, all(2));
  }

  @Test public void testUnalignedBeginning() throws Exception {
    Assume.assumeTrue(supportsUnaligned());

    UnsafeCopier copier = createCopier(fourLongOffset + 4, 28);

    FourLongs src = new FourLongs(all(1), all(2), all(3), all(4));
    FourLongs dest = test(copier, src, 4, new FourLongs());

    assertEquals("Dest should have all the fields altered",
        new FourLongs(0x1111111100000000L, all(2), all(3), all(4)), dest);
  }


  @Test public void testUnalignedBoth() throws Exception {
    Assume.assumeTrue(supportsUnaligned());

    UnsafeCopier copier = createCopier(fourLongOffset + 4, 24);

    FourLongs src = new FourLongs(all(1), all(2), all(3), all(4));
    FourLongs dest = test(copier, src, 4, new FourLongs());
    assertEquals("Dest should have all the fields altered",
        new FourLongs(0x1111111100000000L, all(2), all(3), 0x0000000044444444L), dest);
  }

  @Test public void testUnalignedEnd() throws Exception {
    Assume.assumeTrue(supportsUnaligned());

    UnsafeCopier copier = createCopier(fourLongOffset, 28);

    FourLongs src = new FourLongs(all(1), all(2), all(3), all(4));
    FourLongs dest = test(copier, src, 0, new FourLongs());
    assertEquals("Dest should have all the fields altered",
        new FourLongs(all(1), all(2), all(3), 0x0000000044444444L), dest);
  }
}
