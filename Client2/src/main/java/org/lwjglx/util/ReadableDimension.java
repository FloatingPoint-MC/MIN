package org.lwjglx.util;

/**
 * Readonly interface for Dimensions
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public interface ReadableDimension {

    /**
     * Get the width
     * 
     * @return int
     */
    int getWidth();

    /**
     * Get the height
     * 
     * @return int
     */
    int getHeight();

    /**
     * Copy this ReadableDimension into a destination Dimension
     * 
     * @param dest The destination
     */
    void getSize(WritableDimension dest);
}
