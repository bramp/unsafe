package net.bramp.unsafe;

import sun.misc.Unsafe;

/**
 * Dummy copier, used mainly to write sample code, to turn into byte code
 */
public class DummyUnsafeCopier extends UnsafeCopier {

    public DummyUnsafeCopier(Unsafe unsafe) {
        super(unsafe);
    }

    /**
     *    L0
         LINENUMBER 16 L0
         LDC 10
         LSTORE 4
         L1
         LINENUMBER 17 L1
         ALOAD 0
         GETFIELD net/bramp/unsafe/DummyUnsafeCopier.unsafe : Lsun/misc/Unsafe;
         ALOAD 1
         LLOAD 4
         ALOAD 0
         GETFIELD net/bramp/unsafe/DummyUnsafeCopier.unsafe : Lsun/misc/Unsafe;
         LLOAD 2
         INVOKEVIRTUAL sun/misc/Unsafe.getLong (J)J
         INVOKEVIRTUAL sun/misc/Unsafe.putLong (Ljava/lang/Object;JJ)V
         L2
         LINENUMBER 18 L2
         LLOAD 4
         LDC 8
         LADD
         LSTORE 4
         LLOAD 2
         LDC 8
         LADD
         LSTORE 2
         L3
         LINENUMBER 19 L3
         ALOAD 0
         GETFIELD net/bramp/unsafe/DummyUnsafeCopier.unsafe : Lsun/misc/Unsafe;
         ALOAD 1
         LLOAD 4
         ALOAD 0
         GETFIELD net/bramp/unsafe/DummyUnsafeCopier.unsafe : Lsun/misc/Unsafe;
         LLOAD 2
         INVOKEVIRTUAL sun/misc/Unsafe.getLong (J)J
         INVOKEVIRTUAL sun/misc/Unsafe.putLong (Ljava/lang/Object;JJ)V
         L4
         LINENUMBER 20 L4
         LLOAD 4
         LDC 8
         LADD
         LSTORE 4
         LLOAD 2
         LDC 8
         LADD
         LSTORE 2
         L5
         LINENUMBER 21 L5
         RETURN
         L6
         LOCALVARIABLE this Lnet/bramp/unsafe/DummyUnsafeCopier; L0 L6 0
         LOCALVARIABLE dest Ljava/lang/Object; L0 L6 1
         LOCALVARIABLE src J L0 L6 2
         LOCALVARIABLE destOffset J L1 L6 4
         MAXSTACK = 7
         MAXLOCALS = 6
     * @param dest
     * @param src
     */
    @Override
    public void copy(Object dest, long src) {
        long destOffset = 10;
        unsafe.putLong(dest, destOffset, unsafe.getLong(src));
        destOffset += 8; src += 8;
        unsafe.putLong(dest, destOffset, unsafe.getLong(src));
        destOffset += 8; src += 8;
    }
}
