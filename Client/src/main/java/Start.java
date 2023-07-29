import java.util.Arrays;

import cn.floatingpoint.min.launcher.Launcher;
import net.minecraft.entity.player.EntityPlayer;

public class Start
{
    public static void main(String[] args)
    {
        String username = "狂笑的蛇将写散文";
        Launcher.launch(concat(new String[] {"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.12", "--userProperties", "{}", "--username", username, "--uuid", EntityPlayer.getOfflineUUID(username).toString()}, args));
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
