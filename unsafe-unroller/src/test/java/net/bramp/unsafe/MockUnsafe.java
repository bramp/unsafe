package net.bramp.unsafe;

public class MockUnsafe {

    public long getLong(long srcAddress) {
        System.out.printf("getLong(%d)%n", srcAddress);
        return srcAddress;
    }

    public long getLong(Object src, long offset) {
        System.out.printf("getLong(%s, %d)%n", src, offset);
        return offset;
    }

    public void putLong(Object dest, long destOffset, long value) {
        System.out.printf("putLong(%s, %d, %d)%n", dest, destOffset, value);
    }
}
