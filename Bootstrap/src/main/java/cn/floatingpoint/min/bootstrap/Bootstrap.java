package cn.floatingpoint.min.bootstrap;

import cn.floatingpoint.min.bootstrap.exceptions.InitiateException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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

    public Bootstrap(String[] args) {
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
    }

    private void clientStart() {
        try {
            label.setText("Checking status...");
        } catch (Exception var12) {
            var12.printStackTrace();
        }
    }
}
