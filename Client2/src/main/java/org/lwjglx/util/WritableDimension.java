package org.lwjglx.util;

/**
 * Write interface for Dimensions
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 *
 */
public interface WritableDimension {

    void setSize(int w, int h);

    void setSize(ReadableDimension d);

    /**
     * Sets the height.
     * 
     * @param height The height to set
     */
    void setHeight(int height);

    /**
     * Sets the width.
     * 
     * @param width The width to set
     */
    void setWidth(int width);
}
