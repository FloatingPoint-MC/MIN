package org.lwjglx;

import org.lwjgl.system.Pointer;

/**
 * A common interface for classes that wrap pointer addresses.
 *
 * @author Spasi
 */
public interface PointerWrapper extends Pointer {

    long getPointer();

    @Override
    default long address() {
        return getPointer();
    }
}
