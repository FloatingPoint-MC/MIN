package org.lwjglx.opengl;

import org.lwjglx.LWJGLException;
import org.lwjglx.PointerBuffer;

/**
 * The Drawable interface describes an OpenGL drawable with an associated Context.
 *
 * @author elias_naur
 */
public interface Drawable {

    /** Returns true if the Drawable's context is current in the current thread. */
    boolean isCurrent() throws LWJGLException;

    /**
     * If the Drawable's context is current in the current thread, no context will be current after a call to this
     * method.
     *
     * @throws LWJGLException
     */
    void releaseContext() throws LWJGLException;

    /** Destroys the Drawable. */
    void destroy();
}
