package org.lwjglx.openal;

/**
 * The ALCcontext class represents a context opened in OpenAL space.
 *
 * All operations of the AL core API affect a current AL context. Within the scope of AL, the ALC is implied - it is not
 * visible as a handle or function parameter. Only one AL Context per process can be current at a time. Applications
 * maintaining multiple AL Contexts, whether threaded or not, have to set the current context accordingly. Applications
 * can have multiple threads that share one more or contexts. In other words, AL and ALC are threadsafe.
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$ $Id$
 */
public final class ALCcontext {

    /** Address of actual context */
    final long context;

    /** Whether this context is valid */
    private boolean valid;

    /**
     * Creates a new instance of ALCcontext
     *
     * @param context address of actual context
     */
    ALCcontext(long context) {
        this.context = context;
        this.valid = true;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object context) {
        if (context instanceof ALCcontext) {
            return ((ALCcontext) context).context == this.context;
        }
        return super.equals(context);
    }

    /**
     * @return true if this context is still valid
     */
    public boolean isValid() {
        return valid;
    }
}
