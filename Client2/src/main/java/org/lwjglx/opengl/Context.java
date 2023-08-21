package org.lwjglx.opengl;

import org.lwjglx.LWJGLException;

/**
 * @author Spasi
 * @since 14/5/2011
 */
interface Context {

    boolean isCurrent() throws LWJGLException;

    void makeCurrent() throws LWJGLException;

    void releaseCurrent() throws LWJGLException;
}
