package cn.floatingpoint.min.system.hyt.packet.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-18 15:04:40
 */
public class Hyt0Packet implements CustomPacket {
    @Override
    public String getChannel() {
        return "hyt0";
    }

    @Override
    public void process(ByteBuf byteBuf) {
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        byte status = packetBuffer.readByte();
        if (status == 0) {
            String worldName = packetBuffer.readString(123456);
            if (worldName.equals("lobby1")) {
                Managers.clientManager.vexGui = true;
            }
        }
    }
}
