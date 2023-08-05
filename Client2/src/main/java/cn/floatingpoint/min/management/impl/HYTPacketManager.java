package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import cn.floatingpoint.min.system.hyt.packet.impl.GermModPacket;
import cn.floatingpoint.min.system.hyt.packet.impl.VexViewPacket;

import java.util.HashSet;

public class HYTPacketManager implements Manager {
    public final HashSet<CustomPacket> packets = new HashSet<>();

    @Override
    public String getName() {
        return "HYT Packet Manager";
    }

    @Override
    public void init() {
        packets.add(new GermModPacket());
        //packets.add(new Hyt0Packet());
        packets.add(new VexViewPacket());
    }
}
