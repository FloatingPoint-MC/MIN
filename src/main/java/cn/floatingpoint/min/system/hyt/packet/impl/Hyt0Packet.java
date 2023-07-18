package cn.floatingpoint.min.system.hyt.packet.impl;

import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import cn.floatingpoint.min.system.hyt.world.HYTWorldChunkProvider;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

public class Hyt0Packet implements CustomPacket {
    public static String worldName;

    @Override
    public String getChannel() {
        return "hyt0";
    }

    @Override
    public void process(ByteBuf byteBuf) {
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        byte a3 = packetBuffer.readByte();
        if (a3 == 0) {
            worldName = packetBuffer.readString(123456);
            return;
        }
        if (a3 == 1) {
            if (mc.world == null) {
                return;
            }
            new HYTWorldChunkProvider(mc.world);
            mc.world.setChunkProvider(HYTWorldChunkProvider.instance);
            mc.world.setClientChunkProvider(HYTWorldChunkProvider.instance);
            int total = packetBuffer.readShort();
            if (total == 0) {
                for (int x = -16; x < 16;x += 16) {
                    for (int z = -16; z < 16;z += 16) {
                        mc.world.getChunkProvider().loadChunk(x, z);
                        mc.world.markBlockRangeForRenderUpdate(x << 4, 0, z << 4, (x << 4) + 15, 256, (z << 4) + 15);
                    }
                }
            }
            int current = 0;
            while (current < total) {
                int x = packetBuffer.readInt();
                int z = packetBuffer.readInt();
                mc.world.doPreChunk(x, z, true);
                current++;
            }
        }
    }
}
