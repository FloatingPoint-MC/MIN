package org.lwjglx.util;

import java.io.Serializable;

/**
 * A mutable Color class
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public final class Color implements ReadableColor, Serializable, WritableColor {

    private static final long serialVersionUID = 1L;

    /** Color components, publicly accessible */
    private byte red, green, blue, alpha;

    /**
     * Constructor for Color.
     */
    public Color() {
        this(0, 0, 0, 255);
    }

    /**
     * Constructor for Color. Alpha defaults to 255.
     */
    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    /**
     * Constructor for Color. Alpha defaults to 255.
     */
    public Color(byte r, byte g, byte b) {
        this(r, g, b, (byte) 255);
    }

    /**
     * Constructor for Color.
     */
    public Color(int r, int g, int b, int a) {
        set(r, g, b, a);
    }

    /**
     * Constructor for Color.
     */
    public Color(byte r, byte g, byte b, byte a) {
        set(r, g, b, a);
    }

    /**
     * Constructor for Color
     */
    public Color(ReadableColor c) {
        setColor(c);
    }

    /**
     * Set a color
     */
    public void set(int r, int g, int b, int a) {
        red = (byte) r;
        green = (byte) g;
        blue = (byte) b;
        alpha = (byte) a;
    }

    /**
     * Set a color
     */
    public void set(byte r, byte g, byte b, byte a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    /**
     * Set a color
     */
    public void set(int r, int g, int b) {
        set(r, g, b, 255);
    }

    /**
     * Set a color
     */
    public void set(byte r, byte g, byte b) {
        set(r, g, b, (byte) 255);
    }

    /**
     * Accessor
     */
    public int getRed() {
        return red & 0xFF;
    }

    /**
     * Accessor
     */
    public int getGreen() {
        return green & 0xFF;
    }

    /**
     * Accessor
     */
    public int getBlue() {
        return blue & 0xFF;
    }

    /**
     * Accessor
     */
    public int getAlpha() {
        return alpha & 0xFF;
    }

    /**
     * Set the Red component
     */
    public void setRed(int red) {
        this.red = (byte) red;
    }

    /**
     * Set the Green component
     */
    public void setGreen(int green) {
        this.green = (byte) green;
    }

    /**
     * Set the Blue component
     */
    public void setBlue(int blue) {
        this.blue = (byte) blue;
    }

    /**
     * Set the Alpha component
     */
    public void setAlpha(int alpha) {
        this.alpha = (byte) alpha;
    }

    /**
     * Set the Red component
     */
    public void setRed(byte red) {
        this.red = red;
    }

    /**
     * Set the Green component
     */
    public void setGreen(byte green) {
        this.green = green;
    }

    /**
     * Set the Blue component
     */
    public void setBlue(byte blue) {
        this.blue = blue;
    }

    /**
     * Set the Alpha component
     */
    public void setAlpha(byte alpha) {
        this.alpha = alpha;
    }

    /**
     * Stringify
     */
    public String toString() {
        return "Color [" + getRed() + ", " + getGreen() + ", " + getBlue() + ", " + getAlpha() + "]";
    }

    /**
     * Equals
     */
    public boolean equals(Object o) {
        return (o instanceof ReadableColor)
                && (((ReadableColor) o).getRed() == this.getRed())
                && (((ReadableColor) o).getGreen() == this.getGreen())
                && (((ReadableColor) o).getBlue() == this.getBlue())
                && (((ReadableColor) o).getAlpha() == this.getAlpha());
    }

    /**
     * Hashcode
     */
    public int hashCode() {
        return (red << 24) | (green << 16) | (blue << 8) | alpha;
    }

    /*
     * (Overrides)
     * @see com.shavenpuppy.jglib.ReadableColor#getAlphaByte()
     */
    public byte getAlphaByte() {
        return alpha;
    }

    /*
     * (Overrides)
     * @see com.shavenpuppy.jglib.ReadableColor#getBlueByte()
     */
    public byte getBlueByte() {
        return blue;
    }

    /*
     * (Overrides)
     * @see com.shavenpuppy.jglib.ReadableColor#getGreenByte()
     */
    public byte getGreenByte() {
        return green;
    }

    /*
     * (Overrides)
     * @see com.shavenpuppy.jglib.ReadableColor#getRedByte()
     */
    public byte getRedByte() {
        return red;
    }


    /**
     * Set this color's color by copying another color
     * 
     * @param src The source color
     */
    public void setColor(ReadableColor src) {
        red = src.getRedByte();
        green = src.getGreenByte();
        blue = src.getBlueByte();
        alpha = src.getAlphaByte();
    }
}
