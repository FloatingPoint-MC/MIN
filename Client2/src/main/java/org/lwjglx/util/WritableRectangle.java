package org.lwjglx.util;

/**
 * Write interface for Rectangles
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public interface WritableRectangle extends WritablePoint, WritableDimension {

    /**
     * Sets the bounds of the rectangle
     * 
     * @param x      Position of rectangle on x axis
     * @param y      Position of rectangle on y axis
     * @param width  Width of rectangle
     * @param height Height of rectangle
     */
    void setBounds(int x, int y, int width, int height);

    /**
     * Sets the bounds of the rectangle
     * 
     * @param src
     */
    void setBounds(ReadableRectangle src);
}
