package cn.floatingpoint.min.system.hyt.npc;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-17 21:16:15
 */
public class Sender {
    public static void sendPacket(int packetId, String context) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("germmod-netease", (new PacketBuffer(Unpooled.buffer().writeInt(packetId))).writeString(context)));
    }
}
