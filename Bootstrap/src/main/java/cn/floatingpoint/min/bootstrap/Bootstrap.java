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
    private static JLabel label;
    private static JProgressBar progressBar;
    private static Bootstrap instance;
    private File dir;
    private boolean canLaunch = false;
    private int length = 0;
    private String sha1;

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
        optionparser.accepts("username").withRequiredArg().defaultsTo("狂笑的蛇将写散文");
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
        introduction.setHorizontalAlignment(SwingConstants.CENTER);
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
                label.setText("Checking java: ");
                if (Integer.parseInt(System.getProperty("java.version").split("\\.")[0]) < 17) {
                    JOptionPane.showMessageDialog(this, "请使用Java17以上版本启动。Please use Java(version upper 17) to launch me!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                label.setText("Checking status: ");
                String url = WebUtil.getPlatform() + "data.json";
                progressBar.setValue(33);
                JSONObject jsonObject = WebUtil.getJSON(url);
                progressBar.setValue(66);
                String remoteVersion = jsonObject.getString("CurrentVersion");
                progressBar.setValue(99);
                sha1 = jsonObject.getString("Sha-1");
                if (!remoteVersion.equalsIgnoreCase(getVersion()) || checkSha1NonRight(sha1)) {
                    progressBar.setValue(100);
                    deleteJarFile();
                    downloadJarFile(remoteVersion);
                } else {
                    progressBar.setValue(100);
                    canLaunch = true;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error while grabbing data.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }).start();
    }

    private void downloadJarFile(String version) {
        label.setText("Fetching game: ");
        progressBar.setValue(0);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(WebUtil.getDownloadUrl() + version + "/Client" + version.split("\\.")[0] + "-" + version + ".jar").openConnection();
            progressBar.setValue(33);
            try (InputStream inputStream = connection.getInputStream()) {
                progressBar.setValue(67);
                File jarFile = new File(this.dir, "Game.jar");
                try (FileOutputStream out = new FileOutputStream(jarFile)) {
                    progressBar.setValue(100);
                    int length = connection.getContentLength();
                    label.setText("Downloading Client: ");
                    progressBar.setValue(0);
                    byte[] bytes = new byte[1024 * 512];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        this.length += len;
                        progressBar.setValue((int) Math.round(100.0D * this.length / length));
                        out.write(bytes, 0, len);
                    }
                    label.setText("Verifying Client: ");
                    progressBar.setValue(0);
                    if (checkSha1NonRight(sha1)) {
                        JOptionPane.showMessageDialog(this, "Invalid file downloaded. Please contact the author.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(0);
                    } else {
                        canLaunch = true;
                        progressBar.setValue(100);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while grabbing game file.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    @SuppressWarnings("all")
    private void prepareGame(String[] args) {
        while (!this.canLaunch) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(this, "Error.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        this.launchGame(args);
    }

    private void launchGame(String[] args) {
        label.setText("                  Launching client...                  ");
        progressBar.setVisible(false);
        File jar = new File(this.dir, "Game.jar");
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()})) {
            Class<?> launcherClass = urlClassLoader.loadClass("cn.floatingpoint.min.launcher.Launcher");
            Method launchMethod = launcherClass.getMethod("launch", String[].class);
            this.setVisible(false);
            launchMethod.invoke(launcherClass, (Object) args);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            JOptionPane.showMessageDialog(this, "Error while launching game.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                progressBar.setValue(0);
            } while (!(new File(this.dir, "Game.jar")).getAbsoluteFile().delete());
            progressBar.setValue(100);
        }
    }
}
