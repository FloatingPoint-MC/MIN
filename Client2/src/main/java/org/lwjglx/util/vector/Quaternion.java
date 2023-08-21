package org.lwjglx.util.vector;

import java.nio.FloatBuffer;

/**
 *
 * Quaternions for LWJGL!
 *
 * @author fbi
 * @version $Revision: 3418 $ $Id: Quaternion.java 3418 2010-09-28 21:11:35Z spasi $
 */
public class Quaternion extends Vector implements ReadableVector4f {

    private static final long serialVersionUID = 1L;

    public float x, y, z, w;

    /**
     * C'tor. The quaternion will be initialized to the identity.
     */
    public Quaternion() {
        super();
        setIdentity();
    }

    /**
     * C'tor
     *
     */
    public Quaternion(float x, float y, float z, float w) {
        set(x, y, z, w);
    }

    /*
     * (non-Javadoc)
     * @see org.lwjgl.util.vector.WritableVector2f#set(float, float)
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /*
     * (non-Javadoc)
     * @see org.lwjgl.util.vector.WritableVector3f#set(float, float, float)
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /*
     * (non-Javadoc)
     * @see org.lwjgl.util.vector.WritableVector4f#set(float, float, float, float)
     */
    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Load from another Vector4f
     *
     * @param src The source vector
     * @return this
     */
    public Quaternion set(ReadableVector4f src) {
        x = src.getX();
        y = src.getY();
        z = src.getZ();
        w = src.getW();
        return this;
    }

    /**
     * Set this quaternion to the multiplication identity.
     * 
     * @return this
     */
    public Quaternion setIdentity() {
        return setIdentity(this);
    }

    /**
     * Set the given quaternion to the multiplication identity.
     * 
     * @param q The quaternion
     * @return q
     */
    public static Quaternion setIdentity(Quaternion q) {
        q.x = 0;
        q.y = 0;
        q.z = 0;
        q.w = 1;
        return q;
    }

    /**
     * @return the length squared of the quaternion
     */
    public float lengthSquared() {
        return x * x + y * y + z * z + w * w;
    }

    /**
     * The dot product of two quaternions
     *
     * @param left  The LHS quat
     * @param right The RHS quat
     * @return left dot right
     */
    public static float dot(Quaternion left, Quaternion right) {
        return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
    }

    /**
     * Calculate the conjugate of this quaternion and put it into the given one
     *
     * @param dest The quaternion which should be set to the conjugate of this quaternion
     */
    public Quaternion negate(Quaternion dest) {
        return negate(this, dest);
    }

    /**
     * Calculate the conjugate of this quaternion and put it into the given one
     *
     * @param src  The source quaternion
     * @param dest The quaternion which should be set to the conjugate of this quaternion
     */
    public static Quaternion negate(Quaternion src, Quaternion dest) {
        if (dest == null) dest = new Quaternion();

        dest.x = -src.x;
        dest.y = -src.y;
        dest.z = -src.z;
        dest.w = src.w;

        return dest;
    }

    /**
     * Calculate the conjugate of this quaternion
     */
    public Vector negate() {
        return negate(this, this);
    }

    /*
     * (non-Javadoc)
     * @see org.lwjgl.util.vector.Vector#load(java.nio.FloatBuffer)
     */
    public Vector load(FloatBuffer buf) {
        x = buf.get();
        y = buf.get();
        z = buf.get();
        w = buf.get();
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.lwjgl.vector.Vector#scale(float)
     */
    public Vector scale(float scale) {
        return scale(scale, this, this);
    }

    /**
     * Scale the source quaternion by scale and put the result in the destination
     * 
     * @param scale The amount to scale by
     * @param src   The source quaternion
     * @param dest  The destination quaternion, or null if a new quaternion is to be created
     * @return The scaled quaternion
     */
    public static Quaternion scale(float scale, Quaternion src, Quaternion dest) {
        if (dest == null) dest = new Quaternion();
        dest.x = src.x * scale;
        dest.y = src.y * scale;
        dest.z = src.z * scale;
        dest.w = src.w * scale;
        return dest;
    }

    /*
     * (non-Javadoc)
     * @see org.lwjgl.util.vector.ReadableVector#store(java.nio.FloatBuffer)
     */
    public Vector store(FloatBuffer buf) {
        buf.put(x);
        buf.put(y);
        buf.put(z);
        buf.put(w);

        return this;
    }

    /**
     * @return x
     */
    public final float getX() {
        return x;
    }

    /**
     * @return y
     */
    public final float getY() {
        return y;
    }

    /**
     * Set X
     *
     * @param x
     */
    public final void setX(float x) {
        this.x = x;
    }

    /**
     * Set Y
     *
     * @param y
     */
    public final void setY(float y) {
        this.y = y;
    }

    /**
     * Set Z
     *
     * @param z
     */
    public void setZ(float z) {
        this.z = z;
    }

    /*
     * (Overrides)
     * @see org.lwjgl.vector.ReadableVector3f#getZ()
     */
    public float getZ() {
        return z;
    }

    /**
     * Set W
     *
     * @param w
     */
    public void setW(float w) {
        this.w = w;
    }

    /*
     * (Overrides)
     * @see org.lwjgl.vector.ReadableVector3f#getW()
     */
    public float getW() {
        return w;
    }

    public String toString() {
        return "Quaternion: " + x + " " + y + " " + z + " " + w;
    }

    /**
     * Sets the value of this quaternion to the quaternion product of quaternions left and right (this = left * right).
     * Note that this is safe for aliasing (e.g. this can be left or right).
     *
     * @param left  the first quaternion
     * @param right the second quaternion
     */
    public static Quaternion mul(Quaternion left, Quaternion right, Quaternion dest) {
        if (dest == null) dest = new Quaternion();
        dest.set(
                left.x * right.w + left.w * right.x + left.y * right.z - left.z * right.y,
                left.y * right.w + left.w * right.y + left.z * right.x - left.x * right.z,
                left.z * right.w + left.w * right.z + left.x * right.y - left.y * right.x,
                left.w * right.w - left.x * right.x - left.y * right.y - left.z * right.z);
        return dest;
    }
}
