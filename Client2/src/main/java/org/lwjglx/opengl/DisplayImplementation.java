package org.lwjglx.opengl;

import org.lwjglx.LWJGLException;

interface DisplayImplementation extends InputImplementation {

    /**
     * Get the driver version. This is a vendor/adapter specific version string. If the version cannot be determined,
     * this function returns null.
     * 
     * @return a String
     */
    String getVersion();

    /**
     * Initialize and return the current display mode.
     */
    DisplayMode init() throws LWJGLException;

    /**
     * Implementation of setTitle(). This will read the window's title member and stash it in the native title of the
     * window.
     */
    void setTitle(String title);

    boolean isVisible();

    boolean isActive();

    boolean isDirty();

    /**
     * Updates the windows internal state. This must be called at least once per video frame to handle window close
     * requests, moves, paints, etc.
     */
    void update();

    /**
     * @return this method will return the width of the Display window.
     */
    int getWidth();

    /**
     * @return this method will return the height of the Display window.
     */
    int getHeight();

    /**
     * @return this method will return the top-left x position of the Display window.
     */
    int getX();

    /**
     * @return this method will return the top-left y position of the Display window.
     */
    int getY();
}
