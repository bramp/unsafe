package net.bramp.unsafe;


import sun.misc.Unsafe;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Methods adapted from
 *   http://mishadoff.com/blog/java-magic-part-4-sun-dot-misc-dot-unsafe/
 *   http://mydailyjava.blogspot.com/2013/12/sunmiscunsafe.html
 *   http://zeroturnaround.com/rebellabs/dangerous-code-how-to-be-unsafe-with-java-classes-objects-in-memory/
 * Other interesting reads
 *   http://www.codeinstructions.com/2008/12/java-objects-memory-structure.html
 */
public class UnsafeHelper {

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static final Unsafe unsafe = createUnsafe();

    private static Unsafe createUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Can't use unsafe", e);
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    /**
     * WARNING: This does not return a pointer, so be warned pointer arithmetic will not work.
     * @param obj
     * @return
     */
    public static long toAddress(Object obj) {
        Object[] array = new Object[] {obj};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        return normalize(unsafe.getInt(array, baseOffset));
        //return unsafe.getInt(array, baseOffset);
    }

    public static Object fromAddress(long address) {
        Object[] array = new Object[] {null};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        unsafe.putLong(array, baseOffset, address);
        return array[0];
    }

    final static long COPY_STRIDE = 8;

    public static void copyMemory(long srcAddress, Object dest) {
        copyMemory(null, srcAddress, dest, 0, sizeOf(dest));
    }

    /**
     * Copies the memory from srcAddress into dest
     *
     * This is our own implementation because Unsafe.copyMemory(Object src, .. Object dest, ...)
     * only works if dest in an array, so we wrote our only implementations. https://goo.gl/pBVlJv
     */
    public static void copyMemory(final Object src, long srcOffset, final Object dest, final long destOffset, final long len) {

        if (src != null)
            throw new RuntimeException("Src must be null");

        if (len % COPY_STRIDE != 0)
            throw new RuntimeException("Length (" + len + ") is not a multiple of stride");

        // TODO make this work if destOffset is not STRIDE aligned
        if (destOffset % COPY_STRIDE != 0)
            throw new RuntimeException("Dest offset (" + destOffset + ") is not stride aligned");


        long end = destOffset + len;
        for (long offset = destOffset; offset < end;) {
            unsafe.putLong(dest, offset, unsafe.getLong(srcOffset));
            offset += COPY_STRIDE;
            srcOffset += COPY_STRIDE;
        }


        /**
         * Unrolled loop
         _________________________________________________________________
         | Test                    | Trial| Time (s)| Extra               |
         |================================================================|
         | Create                  | 0    | 20.826  |                     |
         | Safelist Sum            | 0    | 0.213   | 223820105, -98712140|
         | Unsafelist Sum (inplace)| 0    | 0.641   | 223820105, -98712140|
         | Safelist Sum            | 1    | 0.234   | 223820105, -98712140|
         | Unsafelist Sum (inplace)| 1    | 0.172   | 223820105, -98712140|
         | Safelist Sum            | 2    | 0.269   | 223820105, -98712140|
         | Unsafelist Sum (inplace)| 2    | 0.145   | 223820105, -98712140|
         | Safelist Sum            | 3    | 0.227   | 223820105, -98712140|
         | Unsafelist Sum (inplace)| 3    | 0.152   | 223820105, -98712140|
         | Safelist Sum            | 4    | 0.259   | 223820105, -98712140|
         | Unsafelist Sum (inplace)| 4    | 0.163   | 223820105, -98712140|

        // TODO REMOVE DEBUG unrolled the loop
        unsafe.putLong(dest, destOffset,               unsafe.getLong(srcOffset));
        unsafe.putLong(dest, destOffset + COPY_STRIDE, unsafe.getLong(srcOffset + COPY_STRIDE));
         */
    }

    /**
     * Copies from src to dest one field at a time
     * @param srcAddress
     * @param dest
     */
    public static void copyMemoryFieldByField(long srcAddress, Object dest) {

        Class c = dest.getClass();
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    final Class type = f.getType();

                    if (!type.isPrimitive()) {
                        // TODO maybe support Wrapper classes
                        throw new RuntimeException("Only primitives are supported");
                    }

                    final long offset = unsafe.objectFieldOffset(f);
                    final long src = srcAddress + offset;

                    if (type == int.class) {
                        unsafe.putInt(dest, offset, unsafe.getInt(src));

                    } else if (type == long.class) {
                        unsafe.putLong(dest, offset, unsafe.getLong(src));

                    } else {
                        throw new RuntimeException("Not built yet " + type);
                    }
                }
            }
            c = c.getSuperclass();
        }
    }

    public static long jvm7_32_sizeOf(Object object){
        // This is getting the size out of the class header (at offset 12)
        return unsafe.getAddress(normalize(unsafe.getInt(object, 4L)) + 12L);
    }


    public static long headerSize(Object o) {
        return headerSize(o.getClass());
    }

    public static long sizeOf(Object o) {
        Class c = o.getClass();

        long len = sizeOf(c);

        if (c.isArray()) {
            // TODO Do extra work
            // TODO move into sizeof(Object)
            // (8) first longs and doubles; then
            // (4) ints and floats; then
            // (2) chars and shorts; then
            // (1) bytes and booleans, and last the
            // (4-8) references.
            Object[] array = (Object[])o;
            len += array.length * 8;
        }

        return len;
    }

    private static long roundUpTo8(final long x) {
        return ((x + 7) / 8) * 8;
    }

    /**
     * Returns the size of the header for an instance of this class (in bytes)
     *
     * More information http://www.codeinstructions.com/2008/12/java-objects-memory-structure.html
     *              and http://stackoverflow.com/a/17348396/88646
     *  ,------------------+------------------+------------------ +---------------.
     *  |    mark word(8)  | klass pointer(4) |  array size (opt) |    padding    |
     *  `------------------+------------------+-------------------+---------------'
     *
     * @param c
     * @return
     */
    public static long headerSize(Class c) {
        // TODO Should be calculated based on the platform
        // TODO maybe unsafe.addressSize() would help?
        long len = 12; // JVM_64 has a 12 byte header 8 + 4 (with compressed pointers on)
        if (c.isArray()) {
            len += 4;
        }
        return len;
    }

    /**
     * Returns the offset of the first field in the range [headerSize, sizeOf].
     * @param c
     * @return
     */
    public static long firstFieldOffset(Class c) {
        long minSize = roundUpTo8(headerSize(c));

        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    long offset = unsafe.objectFieldOffset(f);
                    if (offset < minSize) {
                        minSize = offset;
                    }
                }
            }
            c = c.getSuperclass();
        }

        return minSize;
    }

    /**
     * Returns the size of an instance of this class (in bytes)
     * Instances include a header + all fields + padded to 8 bytes.
     * If this is an array, it does not include the size of the elements.
     * @param c
     * @return
     */
    public static long sizeOf(Class c) {
        long maxSize = headerSize(c);

        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    long offset = unsafe.objectFieldOffset(f);
                    if (offset > maxSize) {
                        // Assume 1 byte of the field width. This is ok as it gets padded out at the end
                        maxSize = offset + 1;
                    }
                }
            }
            c = c.getSuperclass();
        }

        // The whole class always pads to a 8 bytes boundary, so we round up to 8 bytes.
        return roundUpTo8(maxSize);
    }

    private static long normalize(int value) {
        if(value >= 0) return value;
        return (~0L >>> 32) & value;
    }

    /**
     * Returns the object as a byte array, including header, padding and all fields.
     * @param o
     * @return
     */
    public static byte[] toByteArray(Object o) {
        int len = (int)sizeOf(o);
        byte[] bytes = new byte[len];
        unsafe.copyMemory(o, 0, bytes, Unsafe.ARRAY_BYTE_BASE_OFFSET, bytes.length);
        return bytes;
    }

    /**
     * Prints out the object (including header, padding, and all fields) as hex.
     * @param out PrintStream to print the hex to
     * @param o The object to print
     *
     * Some examples:
    /**
     * Longs are always 8 byte aligned, so 4 bytes of padding
     * 0x00000000: 01 00 00 00 00 00 00 00  9B 81 61 DF 00 00 00 00
     * 0x00000010: EF CD AB 89 67 45 23 01
     *&#47;
    static class Class8 {
        long l = 0x0123456789ABCDEFL;
    }

    /**
     * 0x00000000: 01 00 00 00 00 00 00 00  8A BF 62 DF 67 45 23 01
     *&#47;
    static class Class4 {
        int i = 0x01234567;
    }

    /**
     * 0x00000000: 01 00 00 00 00 00 00 00  28 C0 62 DF 34 12 00 00
     *&#47;
    static class Class2 {
        short s = 0x01234;
    }

    /**
     * 0x00000000: 01 00 00 00 00 00 00 00  E3 C0 62 DF 12 00 00 00
     *&#47;
    static class Class1 {
        byte b = 0x12;
    }

    /**
     * 0x00000000: 01 00 00 00 00 00 00 00  96 C1 62 DF 12 00 00 00
     * 0x00000010: EF CD AB 89 67 45 23 01
     *&#47;
    static class ClassMixed18 {
        byte b = 0x12;
        long l = 0x0123456789ABCDEFL;
    }

    /**
     * 0x00000000: 01 00 00 00 00 00 00 00  4C C2 62 DF 12 00 00 00
     * 0x00000010: EF CD AB 89 67 45 23 01
     *&#47;
    static class ClassMixed81 {
        long l = 0x0123456789ABCDEFL;
        byte b = 0x12;
    }

    public static void printMemoryLayout() {
        UnsafeHelper.hexDump(System.out, new Class8());
        UnsafeHelper.hexDump(System.out, new Class4());
        UnsafeHelper.hexDump(System.out, new Class2());
        UnsafeHelper.hexDump(System.out, new Class1());
        UnsafeHelper.hexDump(System.out, new ClassMixed18());
        UnsafeHelper.hexDump(System.out, new ClassMixed81());
    }
    */
    public static void hexDump(PrintStream out, Object o) {
        // TODO Change this to use hexDumpAddress instead
        byte[] b = toByteArray(o);
        hexDumpBytes(out, 0, b);
    }

    public static void hexDumpBytes(PrintStream out, long offset, byte[] bytes) {
        final int lineWidth = 16;
        char[] line = new char[lineWidth * 3];

        for (int i = 0; i < bytes.length; i+= lineWidth) {
            int len = Math.min(bytes.length - i, 16);

            for (int j = 0; j < len; j++) {
                int v = bytes[i + j] & 0xFF;
                line[j * 3] = hexArray[v >>> 4];
                line[j * 3 + 1] = hexArray[v & 0x0F];
                line[j * 3 + 2] = ' ';
            }

            int len1 = Math.min(len, 8)*3;
            int len2 = Math.min(len-8, 8)*3;
            out.printf("0x%08X: %s %s\n", offset + (long)i, new String(line, 0, len1), new String(line, 8*3, len2));
        }
    }

    public static void hexDumpAddress(PrintStream out, long address, long length) {
        byte[] bytes = new byte[16];
        while (length > 0) {
            long chunk = Math.min(bytes.length, length);
            unsafe.copyMemory(null, address, bytes, Unsafe.ARRAY_BYTE_BASE_OFFSET, chunk);
            hexDumpBytes(out, address, bytes);
            length -= chunk;
            address += chunk;
        }
    }
}
