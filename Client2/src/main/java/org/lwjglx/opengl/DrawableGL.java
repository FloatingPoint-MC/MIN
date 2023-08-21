package org.lwjglx.opengl;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;

import org.lwjgl.glfw.GLFW;
import org.lwjglx.LWJGLException;
import org.lwjglx.LWJGLUtil;

/** @author Spasi */
public class DrawableGL implements DrawableLWJGL {

    /** The OpenGL Context. */
    protected ContextGL context;

    protected DrawableGL() {
        context = new ContextGL(Display.getWindow(), false);
    }

    public ContextGL getContext() {
        synchronized (GlobalLock.lock) {
            return context;
        }
    }

    public ContextGL createSharedContext() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            long hiddenWindow = GLFW.glfwCreateWindow(16, 16, "MC - Shared Drawable", 0, this.context.glfwWindow);
            if (hiddenWindow == 0) {
                throw new LWJGLException("Couldn't create shared context hidden window");
            }
            return new ContextGL(hiddenWindow, true);
        }
    }

    public boolean isCurrent() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            checkDestroyed();
            return context.isCurrent();
        }
    }

    public void releaseContext() throws LWJGLException {
        synchronized (GlobalLock.lock) {
            checkDestroyed();
            if (context.isCurrent()) context.releaseCurrent();
        }
    }

    public void destroy() {
        synchronized (GlobalLock.lock) {
            if (context == null) return;

            try {
                releaseContext();

                context.forceDestroy();
                context = null;
            } catch (LWJGLException e) {
                LWJGLUtil.log("Exception occurred while destroying Drawable: " + e);
            }
        }
    }

    protected final void checkDestroyed() {
        if (context == null) throw new IllegalStateException("The Drawable has no context available.");
    }
}
