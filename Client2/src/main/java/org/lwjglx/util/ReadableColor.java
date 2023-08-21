package org.lwjglx.util;

/**
 * Readonly interface for Colors
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public interface ReadableColor {

    /**
     * Return the red component (0..255)
     * 
     * @return int
     */
    int getRed();

    /**
     * Return the red component (0..255)
     * 
     * @return int
     */
    int getGreen();

    /**
     * Return the red component (0..255)
     * 
     * @return int
     */
    int getBlue();

    /**
     * Return the red component (0..255)
     * 
     * @return int
     */
    int getAlpha();

    /**
     * Return the red component
     * 
     * @return int
     */
    byte getRedByte();

    /**
     * Return the red component
     * 
     * @return int
     */
    byte getGreenByte();

    /**
     * Return the red component
     * 
     * @return int
     */
    byte getBlueByte();

    /**
     * Return the red component
     * 
     * @return int
     */
    byte getAlphaByte();
}
