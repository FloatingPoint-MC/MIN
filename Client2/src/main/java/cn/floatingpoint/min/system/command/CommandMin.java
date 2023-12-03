package cn.floatingpoint.min.system.command;

import cn.floatingpoint.min.system.ui.hyt.party.VexViewButton;
import cn.floatingpoint.min.system.ui.hyt.party.GuiHandleInvitation;
import cn.floatingpoint.min.utils.client.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 11:55:51
 */
public class CommandMin {
    public static boolean execute(String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("hyt")) {
                if (args.length != 1) {
                    if (args[1].equalsIgnoreCase("party")) {
                        if (args.length != 2) {
                            if (args[2].equalsIgnoreCase("handle")) {
                                if (args.length == 5) {
                                    try {
                                        String accept = args[3];
                                        String deny = args[4];
                                        Minecraft.getMinecraft().displayGuiScreen(new GuiHandleInvitation(new VexViewButton("同意", accept), new VexViewButton("拒绝", deny)));
                                    } catch (Exception ignore) {
                                    }
                                }
                            }
                        }
                    }
                }
            //} else if (args[0].equalsIgnoreCase("irc")) {
            //    if (args.length == 1) {
            //        ChatUtil.printToChat(new TextComponentString("\247b[MIN-IRC] \247cUsage: /min irc <IRC Command>"));
            //    } else {
//
            //    }
            }
        }
        return false;
    }
}
