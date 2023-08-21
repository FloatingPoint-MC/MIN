package org.lwjglx.opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjglx.LWJGLException;
import org.lwjglx.PointerBuffer;

/**
 * <p/>
 * Context encapsulates an OpenGL context.
 * <p/>
 * <p/>
 * This class is thread-safe.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision$ $Id$
 */
public final class ContextGL implements Context {

    public long glfwWindow;
    public final boolean shared;

    public ContextGL(long glfwWindow, boolean shared) {
        this.glfwWindow = glfwWindow;
        this.shared = shared;
    }

    public void releaseCurrent() {
        GLFW.glfwMakeContextCurrent(0);
        GL.setCapabilities(null);
    }

    public synchronized void update() {}

    public static void swapBuffers() {
        GLFW.glfwSwapBuffers(Display.getWindow());
    }

    public synchronized void makeCurrent() throws LWJGLException {
        GLFW.glfwMakeContextCurrent(glfwWindow);
        GL.createCapabilities();
    }

    public synchronized boolean isCurrent() throws LWJGLException {
        return GLFW.glfwGetCurrentContext() == glfwWindow;
    }

    public static void setSwapInterval(int value) {
        GLFW.glfwSwapInterval(value);
    }

    public synchronized void forceDestroy() throws LWJGLException {
        destroy();
    }

    public synchronized void destroy() throws LWJGLException {
        if (shared && glfwWindow > 0) {
            GLFW.glfwDestroyWindow(glfwWindow);
            glfwWindow = -1;
        }
    }

    public synchronized void setCLSharingProperties() throws LWJGLException {}
}
