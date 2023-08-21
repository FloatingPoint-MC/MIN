package org.lwjglx.opengl;

/**
 *
 * This class encapsulates the properties for a given display mode. This class is not instantiable, and is aquired from
 * the <code>Display.
 * getAvailableDisplayModes()</code> method.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision$ $Id$
 */
public final class DisplayMode {

    /** properties of the display mode */
    private final int width, height, bpp, freq;

    /**
     * Construct a display mode. DisplayModes constructed through the public constructor can only be used to specify the
     * dimensions of the Display in windowed mode. To get the available DisplayModes for fullscreen modes, use
     * Display.getAvailableDisplayModes().
     *
     * @param width  The Display width.
     * @param height The Display height.
     * @see Display
     */
    public DisplayMode(int width, int height) {
        this(width, height, 0, 0);
    }

    DisplayMode(int width, int height, int bpp, int freq) {
        this.width = width;
        this.height = height;
        this.bpp = bpp;
        this.freq = freq;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBitsPerPixel() {
        return bpp;
    }

    public int getFrequency() {
        return freq;
    }

    /**
     * Tests for <code>DisplayMode</code> equality
     *
     * @see Object#equals(Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof DisplayMode)) {
            return false;
        }

        DisplayMode dm = (DisplayMode) obj;
        return dm.width == width && dm.height == height && dm.bpp == bpp && dm.freq == freq;
    }

    /**
     * Retrieves the hashcode for this object
     *
     * @see Object#hashCode()
     */
    public int hashCode() {
        return width ^ height ^ freq ^ bpp;
    }

    /**
     * Retrieves a String representation of this <code>DisplayMode</code>
     *
     * @see Object#toString()
     */
    public String toString() {
        return width +
                " x " +
                height +
                " x " +
                bpp +
                " @" +
                freq +
                "Hz";
    }
}
