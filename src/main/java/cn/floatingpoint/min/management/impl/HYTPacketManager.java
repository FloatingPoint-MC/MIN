package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import cn.floatingpoint.min.system.hyt.packet.impl.GermModPacket;
import cn.floatingpoint.min.system.hyt.packet.impl.Hyt0Packet;
import cn.floatingpoint.min.system.hyt.packet.impl.VexViewPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class HYTPacketManager implements Manager {
    public final HashMap<Integer, CustomPacket> packets = new HashMap<>();

    @Override
    public String getName() {
        return "HYT Packet Manager";
    }

    @Override
    public void init() {
        packets.put(0, new GermModPacket());
        packets.put(1, new Hyt0Packet());
        packets.put(2, new VexViewPacket());
    }
}
