package net.bramp.unsafe;

import com.google.common.base.Preconditions;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import sun.misc.Unsafe;

import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * This constructs a class specialised for copying the exact number of bytes specified.
 * It uses runtime byte code generation to unsafe a copy loop, for optimal performance.
 * The unrolled code looks like
 *   unsafe.putLong(dest, destOffset, unsafe.getLong(srcAddress));
 *   destOffset += 8; srcAddress += 8
 *   ...
 *   unsafe.putLong(dest, destOffset, unsafe.getLong(srcAddress));
 */
public class UnrolledUnsafeCopierBuilder {

    long offset = -1;
    long length = -1;

    public UnrolledUnsafeCopierBuilder() {}

    public UnrolledUnsafeCopierBuilder of(Class clazz) {
        offset = UnsafeHelper.firstFieldOffset(clazz);
        length = UnsafeHelper.sizeOf(clazz) - offset;
        return this;
    }

    public UnrolledUnsafeCopierBuilder offset(long offset) {
        checkArgument(offset >= 0);
        this.offset = offset;
        return this;
    }

    public UnrolledUnsafeCopierBuilder length(long length) {
        checkArgument(length >= 0);
        this.length = length;
        return this;
    }

    /**
     * Constructs a new Copier using the passed in Unsafe instance
     * @param unsafe
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    public UnsafeCopier build(Unsafe unsafe) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        checkArgument(offset >= 0);
        checkArgument(length >= 0);

        Class<?> dynamicType = new ByteBuddy()
                .subclass(UnsafeCopier.class)
                .method(named("copy"))
                .intercept(new CopierImplementation(offset, length))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        return (UnsafeCopier) dynamicType.getDeclaredConstructor(Unsafe.class).newInstance(unsafe);
    }

}
