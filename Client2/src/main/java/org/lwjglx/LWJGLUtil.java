package org.lwjglx;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.lwjgl.system.Platform;

/**
 * <p>
 * Internal library methods
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 3608 $ $Id: LWJGLUtil.java 3608 2011-08-10 16:05:46Z spasi $
 */
public class LWJGLUtil {

    public static final int PLATFORM_LINUX = 1;
    public static final int PLATFORM_MACOSX = 2;
    public static final int PLATFORM_WINDOWS = 3;
    public static final String PLATFORM_LINUX_NAME = "linux";
    public static final String PLATFORM_MACOSX_NAME = "macosx";
    public static final String PLATFORM_WINDOWS_NAME = "windows";

    /** Debug flag. */
    public static final boolean DEBUG = getPrivilegedBoolean("org.lwjgl.util.Debug");

    private static final int PLATFORM;

    static {
        switch (Platform.get()) {
            case WINDOWS: {
                PLATFORM = PLATFORM_WINDOWS;
                break;
            }
            case LINUX: {
                PLATFORM = PLATFORM_LINUX;
                break;
            }
            case MACOSX: {
                PLATFORM = PLATFORM_MACOSX;
                break;
            }
            default: {
                throw new LinkageError("Unknown platform: " + Platform.get());
            }
        }
    }

    /**
     * @see #PLATFORM_WINDOWS
     * @see #PLATFORM_LINUX
     * @see #PLATFORM_MACOSX
     * @return the current platform type
     */
    public static int getPlatform() {
        return PLATFORM;
    }

    /**
     * @see #PLATFORM_WINDOWS_NAME
     * @see #PLATFORM_LINUX_NAME
     * @see #PLATFORM_MACOSX_NAME
     * @return current platform name
     */
    public static String getPlatformName() {
        switch (LWJGLUtil.getPlatform()) {
            case LWJGLUtil.PLATFORM_LINUX: {
                return PLATFORM_LINUX_NAME;
            }
            case LWJGLUtil.PLATFORM_MACOSX: {
                return PLATFORM_MACOSX_NAME;
            }
            case LWJGLUtil.PLATFORM_WINDOWS: {
                return PLATFORM_WINDOWS_NAME;
            }
            default: {
                return "unknown";
            }
        }
    }

    /**
     * Gets a boolean property as a privileged actions.
     */
    public static boolean getPrivilegedBoolean(final String property_name) {
        return AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean(property_name));
    }

    /**
     * Prints the given message to System.err if DEBUG is true.
     *
     * @param msg Message to print
     */
    public static void log(CharSequence msg) {
        if (DEBUG) {
            System.err.println("[LWJGL] " + msg);
        }
    }
}
