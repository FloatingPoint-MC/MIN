package cn.floatingpoint.min.system.shortcut;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;

import java.util.ArrayList;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-25 19:07:26
 */
public record Shortcut(String name, int key, ArrayList<Action> actions) {
    public record Action(Type type, String context) {
        public void run() {
            if (type == Type.SEND_MESSAGE) {
                Minecraft.getMinecraft().player.sendChatMessage(context);
            } else if (type == Type.QUIT_NETWORK) {
                Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();
                Minecraft.getMinecraft().loadWorld(null);
                Minecraft.getMinecraft().displayGuiScreen(new GuiMultiplayer(Minecraft.getMinecraft().mainMenu));
            }
        }

        public enum Type {
            SEND_MESSAGE,
            QUIT_NETWORK
        }
    }
}
