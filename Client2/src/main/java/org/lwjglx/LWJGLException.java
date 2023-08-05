package org.lwjglx;

/**
 * <p>
 * This exception is supplied to make exception handling more generic for LWJGL specific exceptions
 * </p>
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$ $Id$
 */
public class LWJGLException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Plain c'tor
     */
    public LWJGLException() {
        super();
    }

    /**
     * Creates a new LWJGLException
     *
     * @param msg String identifier for exception
     */
    public LWJGLException(String msg) {
        super(msg);
    }

    /**
     * @param message
     * @param cause
     */
    public LWJGLException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public LWJGLException(Throwable cause) {
        super(cause);
    }
}
