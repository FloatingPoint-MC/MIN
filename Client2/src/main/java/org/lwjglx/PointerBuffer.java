package org.lwjglx;

import java.lang.reflect.Field;
import java.nio.*;

import javax.annotation.Nonnull;

@SuppressWarnings("all")
public class PointerBuffer extends org.lwjgl.PointerBuffer {

    private static final Field containerAccess;

    static {
        Class<org.lwjgl.PointerBuffer> pbClass = org.lwjgl.PointerBuffer.class;
        try {
            containerAccess = pbClass.getDeclaredField("container");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static ByteBuffer getContainer(org.lwjgl.PointerBuffer buffer) {
        try {
            return (ByteBuffer) containerAccess.get(buffer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public PointerBuffer(org.lwjgl.PointerBuffer ver3) {
        super(ver3.address(), getContainer(ver3), ver3.position(), ver3.position(), ver3.limit(), ver3.capacity());
    }

    @Nonnull
    public static PointerBuffer allocateDirect(int capacity) {
        return new PointerBuffer(org.lwjgl.PointerBuffer.allocateDirect(capacity));
    }
    @Nonnull
    public static PointerBuffer create(long address, int capacity) {
        return new PointerBuffer(org.lwjgl.PointerBuffer.create(address, capacity));
    }

    /**
     * Creates a new PointerBuffer using the specified ByteBuffer as its pointer data source.
     *
     * @param source the source buffer
     */
    @Nonnull
    public static PointerBuffer create(ByteBuffer source) {
        return new PointerBuffer(org.lwjgl.PointerBuffer.create(source));
    }
}
