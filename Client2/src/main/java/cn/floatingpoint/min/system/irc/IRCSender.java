package cn.floatingpoint.min.system.irc;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.utils.client.ChatUtil;
import cn.floatingpoint.min.utils.client.WebUtil;
import cn.floatingpoint.min.utils.math.TimeHelper;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

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
            return;
        }
        if (!timer.isDelayComplete(3000L)) {
            ChatUtil.printToChatWithPrefix("\2477" + Managers.i18NManager.getTranslation("chat.slowdown"));
        } else {
            timer.reset();
            MIN.runAsync(() -> {
                try {
                    JSONObject jsonObject = WebUtil.getJSONFromPost("https://minserver.vlouboos.repl.co/irc/add?username=" + mc.player.getName() + "&message=" + URLEncoder.encode(message, "UTF-8"));
                    if (jsonObject.has("Code") && jsonObject.getInt("Code") == 0) {
                        IRCMessageGrabber.grabMessage();
                    }
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
