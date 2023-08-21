package org.lwjglx.util;

/**
 * Write interface for Points
 * 
 * @author $Author$
 * @version $Revision$ $Id$
 */
public interface WritablePoint {

    void setLocation(int x, int y);

    void setLocation(ReadablePoint p);

    void setX(int x);

    void setY(int y);
}
