package guichaguri.betterfps;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class file that should include Minecraft-dependent methods
 *
 * @author Guilherme Chaguri
 */
public class BetterFpsHelper {

    public static final Logger LOG = LogManager.getLogger("BetterFps");

    private static BetterFpsConfig INSTANCE = null;
    private static File CONFIG_FILE = null;

    public static BetterFpsConfig getConfig() {
        if (INSTANCE == null) loadConfig();
        return INSTANCE;
    }

    public static void loadConfig() {
        CONFIG_FILE = new File(Minecraft.getMinecraft().getDataDir(), "MIN/betterfps.json");
        FileReader reader = null;
        try {
            if (CONFIG_FILE.exists()) {
                reader = new FileReader(CONFIG_FILE);
                INSTANCE = new Gson().fromJson(reader, BetterFpsConfig.class);
            }
        } catch (Exception ex) {
            LOG.error("Could not load the config file", ex);
        } finally {
            IOUtils.closeQuietly(reader);
        }

        if (INSTANCE == null) INSTANCE = new BetterFpsConfig();

        saveConfig();
    }

    public static void saveConfig() {
        FileWriter writer = null;
        try {
            if (!CONFIG_FILE.exists()) CONFIG_FILE.getParentFile().mkdirs();
            writer = new FileWriter(CONFIG_FILE);
            new Gson().toJson(INSTANCE, writer);
        } catch (Exception ex) {
            LOG.error("Could not save the config file", ex);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

}
