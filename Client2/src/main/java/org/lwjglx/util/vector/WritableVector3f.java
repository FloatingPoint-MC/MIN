package org.lwjglx.util.vector;

/**
 * Writable interface to Vector3fs
 * 
 * @author $author$
 * @version $revision$ $Id: WritableVector3f.java 3418 2010-09-28 21:11:35Z spasi $
 */
public interface WritableVector3f extends WritableVector2f {

    /**
     * Set the Z value
     * 
     * @param z
     */
    void setZ(float z);

    /**
     * Set the X,Y,Z values
     * 
     * @param x
     * @param y
     * @param z
     */
    void set(float x, float y, float z);
}
