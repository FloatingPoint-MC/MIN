package cn.floatingpoint.min.utils.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 11:16:33
 */
public class ChatUtil {
    public static void printToChatWithPrefix(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
    }

    public static void printToChat(ITextComponent textComponent) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(textComponent);
    }
}
