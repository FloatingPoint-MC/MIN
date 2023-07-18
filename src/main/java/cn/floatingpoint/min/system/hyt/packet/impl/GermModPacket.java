package cn.floatingpoint.min.system.hyt.packet.impl;

import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

public class GermModPacket implements CustomPacket {
    @Override
    public String getChannel() {
        return "germplugin-netease";
    }

    @Override
    public void process(ByteBuf byteBuf) {
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        int packetId = packetBuffer.readInt();
        System.out.println(packetId);
    }
}
