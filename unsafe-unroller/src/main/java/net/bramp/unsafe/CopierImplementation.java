package net.bramp.unsafe;

import net.bramp.unsafe.bytebuddy.LongAdd;
import net.bramp.unsafe.bytebuddy.MethodVariableStore;

import com.google.common.base.Preconditions;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.constant.LongConstant;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import net.bytebuddy.implementation.bytecode.member.MethodInvocation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import net.bytebuddy.jar.asm.MethodVisitor;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class CopierImplementation implements ByteCodeAppender, Implementation {

  public static final long COPY_STRIDE = 8;

  final long offset;
  final long length;

  public CopierImplementation(long offset, long length) {
    this.offset = offset;
    this.length = length;
  }

  private StackManipulation buildStack() throws NoSuchFieldException, NoSuchMethodException {

    Preconditions.checkState(offset >= 0);
    Preconditions.checkState(length >= 0);

    // TODO Remove these limitations
    Preconditions
        .checkArgument(offset % COPY_STRIDE == 0, "We only support offsets aligned to 8 bytes");
    Preconditions
        .checkArgument(length % COPY_STRIDE == 0, "We only support lengths multiple of 8 bytes");

    final int iterations = (int) (length / COPY_STRIDE);
    if (iterations <= 0) {
      return MethodReturn.VOID;
    }

    final Field unsafeField = UnsafeCopier.class.getDeclaredField("unsafe");
    final Method getLongMethod = Unsafe.class.getMethod("getLong", long.class);
    final Method putLongMethod =
        Unsafe.class.getMethod("putLong", Object.class, long.class, long.class);

    final StackManipulation setupStack = new StackManipulation.Compound(
        LongConstant.forValue(offset),           // LDC offset
        MethodVariableStore.LONG.storeOffset(4)  // LSTORE 4
    );

    final StackManipulation copyStack = new StackManipulation.Compound(
        // unsafe.putLong(dest, destOffset, unsafe.getLong(src));
        MethodVariableAccess.REFERENCE.loadOffset(0), // ALOAD 0 this

        FieldAccess.forField(new FieldDescription.ForLoadedField(unsafeField)).getter(), // GETFIELD

        MethodVariableAccess.REFERENCE.loadOffset(1), // ALOAD 1 dest
        MethodVariableAccess.LONG.loadOffset(4),      // LLOAD 4 destOffset

        MethodVariableAccess.REFERENCE.loadOffset(0), // ALOAD 0 this
        FieldAccess.forField(new FieldDescription.ForLoadedField(unsafeField)).getter(), // GETFIELD

        MethodVariableAccess.LONG.loadOffset(2),      // LLOAD 2 src

        MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(getLongMethod)),
        MethodInvocation.invoke(new MethodDescription.ForLoadedMethod(putLongMethod))
    );

    final StackManipulation incrementStack = new StackManipulation.Compound(
        // destOffset += 8; src += 8;
        MethodVariableAccess.LONG.loadOffset(4), // LLOAD 4 destOffset
        LongConstant.forValue(COPY_STRIDE),      // LDC 8 strideWidth
        LongAdd.INSTANCE,                        // LADD
        MethodVariableStore.LONG.storeOffset(4), // LSTORE 4

        MethodVariableAccess.LONG.loadOffset(2), // LLOAD 2 src
        LongConstant.forValue(COPY_STRIDE),      // LDC 8 strideWidth
        LongAdd.INSTANCE,                        // LADD
        MethodVariableStore.LONG.storeOffset(2)  // LSTORE 2
    );

    // Construct a sequence of stack manipulations
    final StackManipulation[] stack = new StackManipulation[1 + 2 * iterations];
    stack[0] = setupStack;

    for (int i = 0; i < iterations; i++) {
      stack[i * 2 + 1] = copyStack;
      stack[i * 2 + 2] = incrementStack;
    }
    // Override the last incrementStack with a "return"
    stack[stack.length - 1] = MethodReturn.VOID;

    return new StackManipulation.Compound(stack);
  }

  public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext,
      MethodDescription instrumentedMethod) {

    if (!instrumentedMethod.getReturnType().represents(void.class)) {
      throw new IllegalArgumentException(instrumentedMethod + " must return void");
    }

    // TODO Check we have two arguments copy(Object dest, long src);

    try {
      StackManipulation stack = buildStack();
      StackManipulation.Size finalStackSize = stack.apply(methodVisitor, implementationContext);

      return new Size(finalStackSize.getMaximalSize(),
          instrumentedMethod.getStackSize() + 2); // 2 stack slots for a single local variable

    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  public InstrumentedType prepare(InstrumentedType instrumentedType) {
    return instrumentedType;
  }

  public ByteCodeAppender appender(Target implementationTarget) {
    return this;
  }
}
