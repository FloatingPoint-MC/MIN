package org.lwjglx.util;

/**
 * Readonly interface for Points
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public interface ReadablePoint {

    /**
     * @return int
     */
    int getX();

    /**
     * @return int
     */
    int getY();

    /**
     * Copy this ReadablePoint into a destination Point
     * 
     * @param dest The destination Point, or null, to create a new Point
     */
    void getLocation(WritablePoint dest);
}
