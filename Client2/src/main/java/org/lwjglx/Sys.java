package org.lwjglx;

import static org.lwjgl.glfw.GLFW.glfwInit;

import java.awt.Desktop;
import java.net.URI;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.Platform;

public class Sys {

    static {
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize glfw");
    }

    public static void initialize() {}

    /** Returns the LWJGL version. */
    public static String getVersion() {
        return Version.getVersion();
    }

    /**
     * Obtains the number of ticks that the hires timer does in a second. This method is fast; it should be called as
     * frequently as possible, as it recalibrates the timer.
     *
     * @return timer resolution in ticks per second or 0 if no timer is present.
     */
    public static long getTimerResolution() {
        return 1000;
    }

    /**
     * Gets the current value of the hires timer, in ticks. When the Sys class is first loaded the hires timer is reset
     * to 0. If no hires timer is present then this method will always return 0.
     * <p>
     * <strong>NOTEZ BIEN</strong> that the hires timer WILL wrap around.
     *
     * @return the current hires time, in ticks (always >= 0)
     */
    public static long getTime() {
        return (long) (GLFW.glfwGetTime() * 1000);
    }

    public static long getNanoTime() {
        return (long) (GLFW.glfwGetTime() * (1000L * 1000L * 1000L));
    }

    public static void openURL(String url) {
        if (!Desktop.isDesktopSupported()) return;

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.BROWSE)) return;

        try {
            desktop.browse(new URI(url));
        } catch (Exception ignored) {
        }
    }

    public static boolean is64Bit() {
        return Platform.getArchitecture().toString().endsWith("64");
    }
}
