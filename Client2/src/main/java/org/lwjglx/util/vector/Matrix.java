package org.lwjglx.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 *
 * Base class for matrices. When a matrix is constructed it will be the identity matrix unless otherwise stated.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 3418 $ $Id: Matrix.java 3418 2010-09-28 21:11:35Z spasi $
 */
public abstract class Matrix implements Serializable {

    /**
     * Constructor for Matrix.
     */
    protected Matrix() {
        super();
    }

    /**
     * Set this matrix to be the identity matrix.
     * 
     * @return this
     */
    public abstract Matrix setIdentity();

    /**
     * Invert this matrix
     * 
     * @return this
     */
    public abstract Matrix invert();

    /**
     * Load from a float buffer. The buffer stores the matrix in column major (OpenGL) order.
     *
     * @param buf A float buffer to read from
     * @return this
     */
    public abstract Matrix load(FloatBuffer buf);

    /**
     * Negate this matrix
     * 
     * @return this
     */
    public abstract Matrix negate();

    /**
     * Store this matrix in a float buffer. The matrix is stored in column major (openGL) order.
     * 
     * @param buf The buffer to store this matrix in
     * @return this
     */
    public abstract Matrix store(FloatBuffer buf);

    /**
     * Transpose this matrix
     * 
     * @return this
     */
    public abstract Matrix transpose();

    /**
     * @return the determinant of the matrix
     */
    public abstract float determinant();
}
