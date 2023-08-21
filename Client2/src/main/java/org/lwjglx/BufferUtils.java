package org.lwjglx;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * Some often-used Buffer code for creating native buffers of the appropriate size.
 *
 * @author $Author: spasi $
 * @version $Revision: 3528 $ $Id: BufferUtils.java 3528 2011-05-11 00:12:52Z spasi $
 */
public final class BufferUtils {

    /**
     * Construct a direct native-ordered bytebuffer with the specified size.
     * 
     * @param size The size, in bytes
     * @return a ByteBuffer
     */
    public static ByteBuffer createByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }
    /**
     * Construct a direct native-order intbuffer with the specified number of elements.
     * 
     * @param size The size, in ints
     * @return an IntBuffer
     */
    public static IntBuffer createIntBuffer(int size) {
        return createByteBuffer(size << 2).asIntBuffer();
    }

    /**
     * Construct a direct native-order floatbuffer with the specified number of elements.
     * 
     * @param size The size, in floats
     * @return a FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(int size) {
        return createByteBuffer(size << 2).asFloatBuffer();
    }

    /**
     * @return n, where buffer_element_size=2^n.
     */
    public static int getElementSizeExponent(Buffer buf) {
        if (buf instanceof ByteBuffer) return 0;
        else if (buf instanceof ShortBuffer || buf instanceof CharBuffer) return 1;
        else if (buf instanceof FloatBuffer || buf instanceof IntBuffer) return 2;
        else if (buf instanceof LongBuffer || buf instanceof DoubleBuffer) return 3;
        else throw new IllegalStateException("Unsupported buffer type: " + buf);
    }

    /**
     * A helper function which is used to get the byte offset in an arbitrary buffer based on its position
     * 
     * @return the position of the buffer, in BYTES
     */
    public static int getOffset(Buffer buffer) {
        return buffer.position() << getElementSizeExponent(buffer);
    }
}
