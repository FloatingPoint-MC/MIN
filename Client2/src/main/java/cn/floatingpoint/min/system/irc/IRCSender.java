package cn.floatingpoint.min.system.irc;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.runnable.Runnable;
import cn.floatingpoint.min.utils.client.ChatUtil;
import cn.floatingpoint.min.utils.client.WebUtil;
import cn.floatingpoint.min.utils.math.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-19 16:27:02
 */
public class IRCSender {
    private static final TimeHelper timer = new TimeHelper();
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void sendMessage(String message) {
        if (!IRCMessageGrabber.enabled.get()) {
            TextComponentString textComponents = new TextComponentString("\247b[MIN-IRC] \247cIRC未连接！");
            ChatUtil.printToChat(textComponents);
            return;
        }
        if (!timer.isDelayComplete(3000L)) {
            ChatUtil.printToChatWithPrefix("\2477" + Managers.i18NManager.getTranslation("chat.slowdown"));
        } else {
            timer.reset();
            MIN.runAsync(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = WebUtil.getJSONFromPost("https://minserver.vlouboos.repl.co/irc/add?username=" + mc.player.getName() + "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
                        if (jsonObject.has("Code")) {
                            int code = jsonObject.getInt("Code");
                            if (code == 0) {
                                IRCMessageGrabber.grabMessage();
                            } else if (code == -2) {
                                ChatUtil.printToChat(new TextComponentString("\247m--------------------------------------------"));
                                ChatUtil.printToChat(new TextComponentString(""));
                                TextComponentString textComponents = new TextComponentString("   \247b[MIN-IRC] \247c你已被禁言！");
                                ChatUtil.printToChat(textComponents);
                                textComponents = new TextComponentString("\2477   原因: \247f" + jsonObject.getString("Reason"));
                                ChatUtil.printToChat(textComponents);
                                ChatUtil.printToChat(new TextComponentString(""));
                                ChatUtil.printToChat(new TextComponentString("\247m--------------------------------------------"));
                            }
                        }
                    } catch (URISyntaxException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public byte priority() {
                    return 0;
                }
            });
        }
    }
}
