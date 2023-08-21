package org.lwjglx.util.glu;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

/**
 * Util.java
 * <p/>
 * <p/>
 * Created 7-jan-2004
 *
 * @author Erik Duijs
 */
public class Util {

    /**
     * Return ceiling of integer division
     *
     * @param a
     * @param b
     *
     * @return int
     */
    protected static int ceil(int a, int b) {
        return (a % b == 0 ? a / b : a / b + 1);
    }

    /**
     * Normalize vector
     *
     * @param v
     *
     * @return float[]
     */
    protected static float[] normalize(float[] v) {
        float r;

        r = (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        if (r == 0.0) return v;

        r = 1.0f / r;

        v[0] *= r;
        v[1] *= r;
        v[2] *= r;

        return v;
    }

    /**
     * Calculate cross-product
     *
     * @param v1
     * @param v2
     * @param result
     */
    protected static void cross(float[] v1, float[] v2, float[] result) {
        result[0] = v1[1] * v2[2] - v1[2] * v2[1];
        result[1] = v1[2] * v2[0] - v1[0] * v2[2];
        result[2] = v1[0] * v2[1] - v1[1] * v2[0];
    }

    /**
     * Method compPerPix.
     *
     * @param format
     *
     * @return int
     */
    protected static int compPerPix(int format) {
        /* Determine number of components per pixel */
        switch (format) {
            case GL_COLOR_INDEX:
            case GL_STENCIL_INDEX:
            case GL_DEPTH_COMPONENT:
            case GL_RED:
            case GL_GREEN:
            case GL_BLUE:
            case GL_ALPHA:
            case GL_LUMINANCE: {
                return 1;
            }
            case GL_LUMINANCE_ALPHA: {
                return 2;
            }
            case GL_RGB:
            case GL_BGR: {
                return 3;
            }
            case GL_RGBA:
            case GL_BGRA: {
                return 4;
            }
            default: {
                return -1;
            }
        }
    }

    /**
     * @param format OpenGL image format
     * @return Index of the alpha channel, or -1 if not found
     */
    protected static int formatAlphaIndex(int format) {
        switch (format) {
            case GL_ALPHA: {
                return 0;
            }
            case GL_LUMINANCE_ALPHA: {
                return 1;
            }
            case GL_RGBA:
            case GL_BGRA: {
                return 3;
            }
            default: {
                return -1;
            }
        }
    }

    /**
     * Method nearestPower.
     * <p/>
     * Compute the nearest power of 2 number. This algorithm is a little strange, but it works quite well.
     *
     * @param value
     *
     * @return int
     */
    protected static int nearestPower(int value) {
        int i;

        i = 1;

        /* Error! */
        if (value == 0) return -1;

        for (;;) {
            if (value == 1) {
                return i;
            } else if (value == 3) {
                return i << 2;
            }
            value >>= 1;
            i <<= 1;
        }
    }

    /**
     * Method bytesPerPixel.
     *
     * @param format
     * @param type
     *
     * @return int
     */
    protected static int bytesPerPixel(int format, int type) {
        int n, m;

        switch (format) {
            case GL_COLOR_INDEX:
            case GL_STENCIL_INDEX:
            case GL_DEPTH_COMPONENT:
            case GL_RED:
            case GL_GREEN:
            case GL_BLUE:
            case GL_ALPHA:
            case GL_LUMINANCE: {
                n = 1;
                break;
            }
            case GL_LUMINANCE_ALPHA: {
                n = 2;
                break;
            }
            case GL_RGB:
            case GL_BGR: {
                n = 3;
                break;
            }
            case GL_RGBA:
            case GL_BGRA: {
                n = 4;
                break;
            }
            default: {
                n = 0;
                break;
            }
        }

        switch (type) {
            case GL_UNSIGNED_BYTE: {
                m = 1;
                break;
            }
            case GL_BYTE: {
                m = 1;
                break;
            }
            case GL_BITMAP: {
                m = 1;
                break;
            }
            case GL_UNSIGNED_SHORT: {
                m = 2;
                break;
            }
            case GL_SHORT: {
                m = 2;
                break;
            }
            case GL_UNSIGNED_INT: {
                m = 4;
                break;
            }
            case GL_INT: {
                m = 4;
                break;
            }
            case GL_FLOAT: {
                m = 4;
                break;
            }
            default: {
                m = 0;
                break;
            }
        }

        return n * m;
    }
}
