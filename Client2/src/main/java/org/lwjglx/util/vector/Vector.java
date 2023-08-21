package org.lwjglx.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 *
 * Base class for vectors.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 3418 $ $Id: Vector.java 3418 2010-09-28 21:11:35Z spasi $
 */
public abstract class Vector implements Serializable, ReadableVector {

    /**
     * Constructor for Vector.
     */
    protected Vector() {
        super();
    }

    /**
     * @return the length of the vector
     */
    public final float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * @return the length squared of the vector
     */
    public abstract float lengthSquared();

    /**
     * Load this vector from a FloatBuffer
     * 
     * @param buf The buffer to load it from, at the current position
     * @return this
     */
    public abstract Vector load(FloatBuffer buf);

    /**
     * Negate a vector
     * 
     * @return this
     */
    public abstract Vector negate();

    /**
     * Normalise this vector
     * 
     * @return this
     */
    public final Vector normalise() {
        float len = length();
        if (len != 0.0f) {
            float l = 1.0f / len;
            return scale(l);
        } else throw new IllegalStateException("Zero length vector");
    }

    /**
     * Store this vector in a FloatBuffer
     * 
     * @param buf The buffer to store it in, at the current position
     * @return this
     */
    public abstract Vector store(FloatBuffer buf);

    /**
     * Scale this vector
     * 
     * @param scale The scale factor
     * @return this
     */
    public abstract Vector scale(float scale);
}
