package cn.floatingpoint.min.bootstrap;

import cn.floatingpoint.min.bootstrap.exceptions.InitiateException;
import cn.floatingpoint.min.util.WebUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-29 13:49:01
 */
public class Bootstrap extends JFrame {
    public static JLabel label;
    public static JProgressBar progressBar;
    public static Bootstrap instance;
    public File dir;
    public boolean canLaunch = false;
    private int length;

    public Bootstrap(String[] args) {
        instance = this;
        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser optionparser = new OptionParser();
        optionparser.allowsUnrecognizedOptions();
        optionparser.accepts("demo");
        optionparser.accepts("fullscreen");
        optionparser.accepts("checkGlErrors");
        optionparser.accepts("server").withRequiredArg();
        optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
        OptionSpec<File> fileOptionSpec = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        optionparser.accepts("proxyHost").withRequiredArg();
        optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
        optionparser.accepts("proxyUser").withRequiredArg();
        optionparser.accepts("proxyPass").withRequiredArg();
        optionparser.accepts("username").withRequiredArg().defaultsTo("SuperSkidder");
        optionparser.accepts("uuid").withRequiredArg();
        optionparser.accepts("accessToken").withRequiredArg().required();
        optionparser.accepts("version").withRequiredArg().required();
        optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}");
        optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}");
        optionparser.accepts("assetIndex").withRequiredArg();
        optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy");
        OptionSet optionset = optionparser.parse(args);
        File file = optionset.valueOf(fileOptionSpec);
        this.dir = (new File(file, "GameCore/MIN")).getAbsoluteFile();
        if (!this.dir.exists()) {
            while (!this.dir.mkdirs()) {
                this.dir = (new File(file, "GameCore/MIN")).getAbsoluteFile();
            }
        }

        this.setTitle("MIN Client Bootstrap");
        this.setSize(360, 100);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout());
        label = new JLabel("MIN Client is loading...");
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        this.add(label);
        this.add(progressBar);
        JLabel introduction = new JLabel("Developed by FloatingPoint-MC.");
        introduction.setHorizontalAlignment(SwingConstants.RIGHT);
        introduction.setVerticalAlignment(SwingConstants.BOTTOM);
        this.getContentPane().add(introduction);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public static void main(String[] args) {
        if (instance != null) {
            throw new InitiateException("Method called twice!");
        }
        new Bootstrap(args).clientStart();
        instance.prepareGame(args);
    }

    private void clientStart() {
        new Thread(() -> {
            try {
                label.setText("Checking status...");
                String url = WebUtil.getPlatform() + "data.json";
                JSONObject jsonObject = new JSONObject(WebUtil.getJSON(url));
                String remoteVersion = jsonObject.getString("CurrentVersion");
                if (!remoteVersion.equalsIgnoreCase(Bootstrap.instance.getVersion()) || Bootstrap.instance.checkSha1NonRight(jsonObject.getString("Sha-1"))) {
                    Bootstrap.instance.deleteJarFile();
                    Bootstrap.label.setText("Downloading client: ");
                    Bootstrap.instance.downloadJarFile(remoteVersion);
                } else {
                    Bootstrap.instance.canLaunch = true;
                }
            } catch (Exception e) {
                System.exit(-1);
            }
        }).start();
    }

    private void downloadJarFile(String version) {
        label.setText("Fetching game...");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(WebUtil.getDownloadUrl() + version + "/MIN-" + version + ".jar").openConnection();
            try (InputStream inputStream = connection.getInputStream()) {
                try (FileOutputStream out = new FileOutputStream(new File(this.dir, "Game.jar"))) {
                    int length = inputStream.available();
                    label.setText("Downloading Client: ");
                    byte[] bytes = new byte[1024 * 512];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        out.write(bytes, 0, len);
                    }
                    this.length += len;
                    Bootstrap.progressBar.setValue((int) (100.0D * this.length / length));
                    Bootstrap.instance.canLaunch = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    private void prepareGame(String[] args) {
        while (!this.canLaunch) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                System.exit(-1);
            }
        }
        this.launchGame(args);
    }

    private void launchGame(String[] args) {
        label.setText("Launching client...");
        File jar = new File(this.dir, "Game.jar");
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()})) {
            Class<?> launcherClass = urlClassLoader.loadClass("cn.floatingpoint.min.launcher.Launcher");
            Method launchMethod = launcherClass.getMethod("launch", String[].class);
            this.setVisible(false);
            launchMethod.invoke(launcherClass, (Object) args);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            System.exit(-1);
        }
        System.exit(0);
    }

    private String getVersion() {
        File jar = new File(this.dir, "Game.jar");
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()})) {
            Class<?> versionClass = urlClassLoader.loadClass("VersionInfo");
            return (String) versionClass.getMethod("getVersion").invoke(versionClass);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String getSha1ByFile(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] bytes = new byte[1024];
            int var4;
            while ((var4 = fileInputStream.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, var4);
            }
            bytes = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                stringBuilder.append(Integer.toString((b & 255) + 256, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            return "Unknown";
        }
    }

    private boolean checkSha1NonRight(String sha1) {
        return !this.getSha1ByFile(new File(this.dir, "Game.jar")).equals(sha1);
    }

    private void deleteJarFile() {
        if ((new File(this.dir, "Game.jar")).getAbsoluteFile().exists()) {
            do {
                label.setText("Removing old client...");
            } while (!(new File(this.dir, "Game.jar")).getAbsoluteFile().delete());
        }
    }
}
