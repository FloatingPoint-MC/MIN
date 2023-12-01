package cn.floatingpoint.min.system.hyt.packet.impl;

import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import cn.floatingpoint.min.system.ui.hyt.germ.GermModButton;
import cn.floatingpoint.min.system.ui.hyt.germ.GuiButtonPage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;

public class GermModPacket implements CustomPacket {
    @Override
    public String getChannel() {
        return "germplugin-netease";
    }

    // 什么？你问我为什么取消警告？那你删掉试试。
    @SuppressWarnings("all")
    @Override
    public void process(ByteBuf byteBuf) {
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        int packetId = packetBuffer.readInt();
        //System.out.println(packetId);
        if (packetId == 73) {
            // Gui
            PacketBuffer packetBuffer1 = new PacketBuffer(packetBuffer.copy());
            String identity = packetBuffer1.readString(Short.MAX_VALUE);
            if (identity.equalsIgnoreCase("gui")) {
                String guiUuid = packetBuffer1.readString(Short.MAX_VALUE);
                String yml = packetBuffer1.readString(9999999);
                Yaml yaml = new Yaml();
                Map<String, Object> objectMap = yaml.load(yml);
                if (objectMap == null) return;
                objectMap = (Map<String, Object>) objectMap.get(guiUuid);
                if (objectMap == null) return;
                ArrayList<GermModButton> buttons = new ArrayList<>();
                for (String key : objectMap.keySet()) {
                    if (key.equalsIgnoreCase("options") || key.endsWith("_bg")) continue;
                    Map<String, Object> context = (Map<String, Object>) objectMap.get(key);
                    for (String k : context.keySet()) {
                        if (!k.equalsIgnoreCase("scrollableParts")) continue;
                        context = (Map<String, Object>) context.get("scrollableParts");
                        for (String uuid : context.keySet()) {
                            Map<String, Object> scrollableSubMap = (Map<String, Object>) context.get(uuid);
                            if (scrollableSubMap.containsKey("relativeParts")) {
                                scrollableSubMap = (Map<String, Object>) scrollableSubMap.get("relativeParts");
                                for (String k1 : scrollableSubMap.keySet()) {
                                    scrollableSubMap = (Map<String, Object>) scrollableSubMap.get(k1);
                                    if (scrollableSubMap.containsKey("texts")) {
                                        String buttonText = ((ArrayList<String>) scrollableSubMap.get("texts")).get(0);
                                        buttons.add(new GermModButton(key + "$" + uuid + "$" + k1, buttonText));
                                        break;
                                    }
                                }
                            }
                        }
                        mc.player.connection.sendPacket(new CPacketCustomPayload("germmod-netease",
                                new PacketBuffer(Unpooled.buffer()
                                        .writeInt(4)
                                        .writeInt(0)
                                        .writeInt(0))
                                        .writeString(guiUuid)
                                        .writeString(guiUuid)
                                        .writeString(guiUuid)
                        ));
                        mc.displayGuiScreen(new GuiButtonPage(guiUuid, buttons));
                        return;
                    }
                }
            }
        } else if (packetId == 737) {
            // Damage Display
            //if (Managers.moduleManager.renderModules.get("DamageParticles").isEnabled()) {
            //    String damage = packetBuffer.readString(30000);
            //    System.out.println("Damage:" + damage);
            //}
        } else if (packetId == -1) {
            //System.out.println(packetBuffer.readString(Short.MAX_VALUE));
        } else if (packetId == 2141) { // 大喇叭
            //System.out.println(packetBuffer.readString(Short.MAX_VALUE));
        } else if (packetId == 78) {
            //System.out.println(packetBuffer.readString(Short.MAX_VALUE));
            //System.out.println(packetBuffer.readInt());
            //System.out.println(packetBuffer.readString(Short.MAX_VALUE));
        } else {
            int size = packetBuffer.readableBytes();
            if (size > 0) {
                //System.out.println("Unknown packet id: " + packetId + ", size=" + size);
            }
        }
    }
}
