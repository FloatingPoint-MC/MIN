package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketPlayerPosLook implements Packet<INetHandlerPlayClient>
{
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private Set<EnumFlags> flags;
    private int teleportId;

    public SPacketPlayerPosLook()
    {
    }

    public SPacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set<EnumFlags> flagsIn, int teleportIdIn)
    {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        this.yaw = yawIn;
        this.pitch = pitchIn;
        this.flags = flagsIn;
        this.teleportId = teleportIdIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.flags = EnumFlags.unpack(buf.readUnsignedByte());
        this.teleportId = buf.readVarInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeByte(EnumFlags.pack(this.flags));
        buf.writeVarInt(this.teleportId);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handlePlayerPosLook(this);
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public int getTeleportId()
    {
        return this.teleportId;
    }

    public Set<EnumFlags> getFlags()
    {
        return this.flags;
    }

    public enum EnumFlags
    {
        X(0),
        Y(1),
        Z(2),
        Y_ROT(3),
        X_ROT(4);

        private final int bit;

        EnumFlags(int bitIn)
        {
            this.bit = bitIn;
        }

        private int getMask()
        {
            return 1 << this.bit;
        }

        private boolean isSet(int flags)
        {
            return (flags & this.getMask()) == this.getMask();
        }

        public static Set<EnumFlags> unpack(int flags)
        {
            Set<EnumFlags> set = EnumSet.noneOf(EnumFlags.class);

            for (EnumFlags spacketplayerposlook$enumflags : values())
            {
                if (spacketplayerposlook$enumflags.isSet(flags))
                {
                    set.add(spacketplayerposlook$enumflags);
                }
            }

            return set;
        }

        public static int pack(Set<EnumFlags> flags)
        {
            int i = 0;

            for (EnumFlags spacketplayerposlook$enumflags : flags)
            {
                i |= spacketplayerposlook$enumflags.getMask();
            }

            return i;
        }
    }
}
