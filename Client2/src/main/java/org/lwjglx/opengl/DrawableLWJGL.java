package org.lwjglx.opengl;

import org.lwjglx.LWJGLException;

/**
 * [INTERNAL USE ONLY]
 *
 * @author Spasi
 */
interface DrawableLWJGL extends Drawable {

    /**
     * [INTERNAL USE ONLY] Returns the Drawable's Context.
     *
     * @return the Drawable's Context
     */
    Context getContext();

    /**
     * [INTERNAL USE ONLY] Creates a new Context that is shared with the Drawable's Context.
     *
     * @return a Context shared with the Drawable's Context.
     */
    Context createSharedContext() throws LWJGLException;
}
