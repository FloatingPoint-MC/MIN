package cn.floatingpoint.min.launcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Session;

import javax.swing.*;
import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-24 16:06:41
 */
public class Launcher {
    public static void launch(String[] args) {
        if (args.length == 0) {
            JFrame frame = new JFrame("MIN Client - 1.12.2");
            frame.setSize(854, 480);
            frame.setLocationRelativeTo(null);
            JTextField textField = new JTextField("请使用正规的Minecraft启动器启动本产品！\nPlease use a correct Minecraft Launcher to launch me!");
            frame.add(textField);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
            return;
        }
        if (Integer.parseInt(System.getProperty("java.version").split("\\.")[0]) < 9) {
            JFrame frame = new JFrame("MIN Client - 1.12.2");
            frame.setSize(854, 480);
            frame.setLocationRelativeTo(null);
            JTextField textField = new JTextField("请使用Java9以上版本启动本产品。");
            frame.add(textField);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
            return;
        }
        OptionParser optionparser = new OptionParser();
        optionparser.allowsUnrecognizedOptions();
        optionparser.accepts("demo");
        optionparser.accepts("fullscreen");
        optionparser.accepts("checkGlErrors");
        OptionSpec<String> server = optionparser.accepts("server").withRequiredArg();
        OptionSpec<Integer> port = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
        OptionSpec<File> gameDir = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        OptionSpec<File> assetsDir = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        OptionSpec<File> resourcePackDir = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        OptionSpec<String> proxyHost = optionparser.accepts("proxyHost").withRequiredArg();
        OptionSpec<Integer> proxyPort = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080").ofType(Integer.class);
        OptionSpec<String> proxyUser = optionparser.accepts("proxyUser").withRequiredArg();
        OptionSpec<String> proxyPass = optionparser.accepts("proxyPass").withRequiredArg();
        OptionSpec<String> username = optionparser.accepts("username").withRequiredArg().defaultsTo("狂笑的蛇将写散文");
        OptionSpec<String> uuid = optionparser.accepts("uuid").withRequiredArg();
        OptionSpec<String> accessToken = optionparser.accepts("accessToken").withRequiredArg().required();
        OptionSpec<String> version = optionparser.accepts("version").withRequiredArg().required();
        OptionSpec<Integer> width = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        OptionSpec<Integer> height = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        OptionSpec<String> userProperties = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> profileProperties = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}");
        OptionSpec<String> assetIndex = optionparser.accepts("assetIndex").withRequiredArg();
        OptionSpec<String> userType = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy");
        OptionSpec<String> versionType = optionparser.accepts("versionType").withRequiredArg().defaultsTo("release");
        OptionSet optionset = optionparser.parse(args);
        List<String> list = optionset.valuesOf(optionparser.nonOptions());

        if (!list.isEmpty()) {
            System.out.println("Completely ignored arguments: " + list);
        }

        String s = optionset.valueOf(proxyHost);
        Proxy proxy = Proxy.NO_PROXY;

        if (s != null) {
            try {
                proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(s, optionset.valueOf(proxyPort)));
            } catch (Exception ignored) {
            }
        }

        final String proxyUsername = optionset.valueOf(proxyUser);
        final String proxyPassword = optionset.valueOf(proxyPass);

        if (!proxy.equals(Proxy.NO_PROXY) && isNotEmpty(proxyUsername) && isNotEmpty(proxyPassword)) {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
                }
            });
        }

        int i = optionset.valueOf(width);
        int j = optionset.valueOf(height);
        boolean flag = optionset.has("fullscreen");
        boolean flag1 = optionset.has("checkGlErrors");
        String s3 = optionset.valueOf(version);
        Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
        PropertyMap userPropertyMap = JsonUtils.gsonDeserialize(gson, optionset.valueOf(userProperties), PropertyMap.class);
        PropertyMap profilePropertyMap = JsonUtils.gsonDeserialize(gson, optionset.valueOf(profileProperties), PropertyMap.class);
        String s4 = optionset.valueOf(versionType);
        File file = optionset.valueOf(gameDir);
        File assetsFile = optionset.has(assetsDir) ? optionset.valueOf(assetsDir) : new File(file, "assets/");
        File resourcePackFile = optionset.has(resourcePackDir) ? optionset.valueOf(resourcePackDir) : new File(file, "resourcepacks/");
        String s5 = optionset.has(uuid) ? uuid.value(optionset) : username.value(optionset);
        String s6 = optionset.has(assetIndex) ? assetIndex.value(optionset) : null;
        String s7 = optionset.valueOf(server);
        Integer integer = optionset.valueOf(port);
        Session session = new Session(username.value(optionset), s5, accessToken.value(optionset), userType.value(optionset));
        assert userPropertyMap != null;
        assert profilePropertyMap != null;
        GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, userPropertyMap, profilePropertyMap, proxy), new GameConfiguration.DisplayInformation(i, j, flag, flag1), new GameConfiguration.FolderInformation(file, resourcePackFile, assetsFile, s6), new GameConfiguration.GameInformation(s3, s4), new GameConfiguration.ServerInformation(s7, integer));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        new Minecraft(gameconfiguration).run();
    }

    /**
     * Returns true if the given string is neither null nor empty.
     */
    private static boolean isNotEmpty(String str) {
        return !str.isEmpty();
    }
}
