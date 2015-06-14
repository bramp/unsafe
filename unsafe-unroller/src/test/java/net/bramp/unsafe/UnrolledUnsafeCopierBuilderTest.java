package net.bramp.unsafe;

import net.bramp.unsafe.examples.FourLongs;
import org.junit.Test;
import sun.misc.Unsafe;

import static org.junit.Assert.assertEquals;

/**
 * TODO Test the write doesn't overrun or underun
 */
public class UnrolledUnsafeCopierBuilderTest {

    final static long fieldOffset = UnsafeHelper.firstFieldOffset(FourLongs.class);
    final static long objectSize  = UnsafeHelper.sizeOf(FourLongs.class);

    static Unsafe unsafe = UnsafeHelper.getUnsafe();

    @Test(expected = NullPointerException.class)
    public void testNullClass() throws Exception {
        new UnrolledUnsafeCopierBuilder().of(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoSetup() throws Exception {
        new UnrolledUnsafeCopierBuilder().build(null);
    }

    /**
     * Copies the TestClass to an allocated space in memory
     * @param obj
     * @return The address of the memory, must be freed with unsafe.freeMemory(..)
     */
    private long copyToMemory(FourLongs obj) {
        long dest = unsafe.allocateMemory(objectSize);
        unsafe.copyMemory(obj, 0, null, dest, objectSize);
        return dest;
    }

    @Test
    public void testZeroIterations() throws Exception {

        FourLongs dest = new FourLongs(0, 0, 0, 0);
        long src = copyToMemory(new FourLongs(1, 2, 3, 4));
        try {
            UnsafeCopier copier = new UnrolledUnsafeCopierBuilder()
                .of(FourLongs.class)
                .length(0)
                .build(unsafe);

            copier.copy(dest, src + fieldOffset);
        } finally {
            unsafe.freeMemory(src);
        }

        assertEquals("Dest should remain unchanged with zero copies", new FourLongs(0, 0, 0, 0), dest);
    }

    @Test
    public void testOneIterations() throws Exception {

        FourLongs dest = new FourLongs(0, 0, 0, 0);
        long src = copyToMemory(new FourLongs(1, 2, 3, 4));
        try {
            UnsafeCopier copier = new UnrolledUnsafeCopierBuilder()
                .of(FourLongs.class)
                .length(8)
                .build(unsafe);

            copier.copy(dest, src + fieldOffset);
        } finally {
            unsafe.freeMemory(src);
        }

        assertEquals("Dest should have just the first field altered", new FourLongs(1, 0, 0, 0), dest);
    }


    @Test
    public void testTwoIterations() throws Exception {
        FourLongs dest = new FourLongs(0, 0, 0, 0);
        long src = copyToMemory(new FourLongs(1, 2, 3, 4));
        try {
            UnsafeCopier copier = new UnrolledUnsafeCopierBuilder()
                .of(FourLongs.class)
                .length(16)
                .build(unsafe);

            copier.copy(dest, src + fieldOffset);
        } finally {
            unsafe.freeMemory(src);
        }

        assertEquals("Dest should have just the two fields altered", new FourLongs(1, 2, 0, 0), dest);
    }

    @Test
    public void testFourIterations() throws Exception {
        FourLongs dest = new FourLongs(0, 0, 0, 0);
        long src = copyToMemory(new FourLongs(1, 2, 3, 4));
        try {
            UnsafeCopier copier = new UnrolledUnsafeCopierBuilder()
                .of(FourLongs.class)
                .build(unsafe);

            copier.copy(dest, src + fieldOffset);
        } finally {
            unsafe.freeMemory(src);
        }

        assertEquals("Dest should have all the fields altered", new FourLongs(1, 2, 3, 4), dest);
    }
}