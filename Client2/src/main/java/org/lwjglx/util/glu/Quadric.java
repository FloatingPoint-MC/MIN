package org.lwjglx.util.glu;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjglx.util.glu.GLU.*;

/**
 * Quadric.java
 *
 *
 * Created 22-dec-2003
 * 
 * @author Erik Duijs
 */
public class Quadric {

    protected int drawStyle;
    protected int orientation;
    protected boolean textureFlag;
    protected int normals;

    /**
     * Constructor for Quadric.
     */
    public Quadric() {
        super();

        drawStyle = GLU_FILL;
        orientation = GLU_OUTSIDE;
        textureFlag = false;
        normals = GLU_SMOOTH;
    }

    /**
     * Call glNormal3f after scaling normal to unit length.
     *
     * @param x
     * @param y
     * @param z
     */
    protected void normal3f(float x, float y, float z) {
        float mag;

        mag = (float) Math.sqrt(x * x + y * y + z * z);
        if (mag > 0.00001F) {
            x /= mag;
            y /= mag;
            z /= mag;
        }
        glNormal3f(x, y, z);
    }

    /**
     * specifies what kind of orientation is desired for. The orientation values are as follows:
     *
     * GLU.OUTSIDE: Quadrics are drawn with normals pointing outward.
     *
     * GLU.INSIDE: Normals point inward. The default is GLU.OUTSIDE.
     *
     * Note that the interpretation of outward and inward depends on the quadric being drawn.
     *
     * @param orientation The orientation to set
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * Returns the orientation.
     * 
     * @return int
     */
    public int getOrientation() {
        return orientation;
    }


    protected void TXTR_COORD(float x, float y) {
        if (textureFlag) glTexCoord2f(x, y);
    }

    protected float sin(float r) {
        return (float) Math.sin(r);
    }

    protected float cos(float r) {
        return (float) Math.cos(r);
    }
}
