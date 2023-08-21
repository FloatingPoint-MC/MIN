package org.lwjglx.util;

import java.io.Serializable;

/**
 * A 2D integer Dimension class, which looks remarkably like an AWT one.
 *
 * @author $Author$
 * @version $Revision$ $Id$
 */
public final class Dimension implements Serializable, ReadableDimension, WritableDimension {

    private static final long serialVersionUID = 1L;

    /** The dimensions! */
    private int width, height;

    /**
     * Constructor for Dimension.
     */
    public Dimension() {
        super();
    }

    /**
     * Constructor for Dimension.
     */
    public Dimension(int w, int h) {
        this.width = w;
        this.height = h;
    }

    /**
     * Constructor for Dimension.
     */
    public Dimension(ReadableDimension d) {
        setSize(d);
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public void setSize(ReadableDimension d) {
        this.width = d.getWidth();
        this.height = d.getHeight();
    }

    /*
     * (Overrides)
     * @see com.shavenpuppy.jglib.ReadableDimension#getSize(com.shavenpuppy.jglib.Dimension)
     */
    public void getSize(WritableDimension dest) {
        dest.setSize(this);
    }

    /**
     * Checks whether two dimension objects have equal values.
     */
    public boolean equals(Object obj) {
        if (obj instanceof ReadableDimension) {
            ReadableDimension d = (ReadableDimension) obj;
            return (width == d.getWidth()) && (height == d.getHeight());
        }
        return false;
    }

    /**
     * Returns the hash code for this <code>Dimension</code>.
     *
     * @return a hash code for this <code>Dimension</code>
     */
    public int hashCode() {
        int sum = width + height;
        return sum * (sum + 1) / 2 + width;
    }

    /**
     * Returns a string representation of the values of this <code>Dimension</code> object's <code>height</code> and
     * <code>width</code> fields. This method is intended to be used only for debugging purposes, and the content and
     * format of the returned string may vary between implementations. The returned string may be empty but may not be
     * <code>null</code>.
     *
     * @return a string representation of this <code>Dimension</code> object
     */
    public String toString() {
        return getClass().getName() + "[width=" + width + ",height=" + height + "]";
    }

    /**
     * Gets the height.
     *
     * @return Returns a int
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height.
     *
     * @param height The height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the width.
     *
     * @return Returns a int
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width.
     *
     * @param width The width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
