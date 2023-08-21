package org.lwjglx.util;

/**
 * Write interface for Colors
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public interface WritableColor {

    /**
     * Set a color
     */
    void set(int r, int g, int b, int a);

    /**
     * Set a color
     */
    void set(byte r, byte g, byte b, byte a);

    /**
     * Set a color
     */
    void set(int r, int g, int b);

    /**
     * Set a color
     */
    void set(byte r, byte g, byte b);

    /**
     * Set the Red component
     */
    void setRed(int red);

    /**
     * Set the Green component
     */
    void setGreen(int green);

    /**
     * Set the Blue component
     */
    void setBlue(int blue);

    /**
     * Set the Alpha component
     */
    void setAlpha(int alpha);

    /**
     * Set the Red component
     */
    void setRed(byte red);

    /**
     * Set the Green component
     */
    void setGreen(byte green);

    /**
     * Set the Blue component
     */
    void setBlue(byte blue);

    /**
     * Set the Alpha component
     */
    void setAlpha(byte alpha);


    /**
     * Set this color's color by copying another color
     * 
     * @param src The source color
     */
    void setColor(ReadableColor src);
}
