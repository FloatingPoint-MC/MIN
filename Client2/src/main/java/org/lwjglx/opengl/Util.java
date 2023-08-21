package org.lwjglx.opengl;

import static org.lwjgl.opengl.ARBImaging.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Simple utility class.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision$
 */
public final class Util {

    /** No c'tor */
    private Util() {}

    /**
     * Translate a GL error code to a String describing the error
     */
    public static String translateGLErrorString(int error_code) {
        switch (error_code) {
            case GL_NO_ERROR: {
                return "No error";
            }
            case GL_INVALID_ENUM: {
                return "Invalid enum";
            }
            case GL_INVALID_VALUE: {
                return "Invalid value";
            }
            case GL_INVALID_OPERATION: {
                return "Invalid operation";
            }
            case GL_STACK_OVERFLOW: {
                return "Stack overflow";
            }
            case GL_STACK_UNDERFLOW: {
                return "Stack underflow";
            }
            case GL_OUT_OF_MEMORY: {
                return "Out of memory";
            }
            case GL_TABLE_TOO_LARGE: {
                return "Table too large";
            }
            case GL_INVALID_FRAMEBUFFER_OPERATION: {
                return "Invalid framebuffer operation";
            }
            default: {
                return null;
            }
        }
    }
}
