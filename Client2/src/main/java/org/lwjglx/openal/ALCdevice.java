package org.lwjglx.openal;

/**
 * The ALCdevice class represents a device opened in OpenAL space.
 *
 * ALC introduces the notion of a Device. A Device can be, depending on the implementation, a hardware device, or a
 * daemon/OS service/actual server. This mechanism also permits different drivers (and hardware) to coexist within the
 * same system, as well as allowing several applications to share system resources for audio, including a single
 * hardware output device. The details are left to the implementation, which has to map the available backends to unique
 * device specifiers.
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision$ $Id$
 */
public final class ALCdevice {

    /** Address of actual device */
    public final long device;

    /**
     * Creates a new instance of ALCdevice
     *
     * @param device address of actual device
     */
    ALCdevice(long device) {
        this.device = device;
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object device) {
        if (device instanceof ALCdevice) {
            return ((ALCdevice) device).device == this.device;
        }
        return super.equals(device);
    }
}
