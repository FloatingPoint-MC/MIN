package cn.floatingpoint.min.management.impl;

import java.io.*;
import java.util.*;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.Module;
import cn.floatingpoint.min.system.module.value.Value;
import cn.floatingpoint.min.system.module.value.impl.*;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.math.Vec2i;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("all")
public class FileManager implements Manager {
    public static final int VERSION = 4;
    public File dir = null;
    public boolean shouldSave = false;

    public File getConfigFile(String name) {
        File file = new File(this.dir, name);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public String getName() {
        return "File Manager";
    }

    @Override
    public void init() {
        final File mcDataDir = Minecraft.getMinecraft().getDataDir();
        this.dir = new File(mcDataDir, "MIN");
        if (this.dir.exists()) {
            if (!this.dir.isDirectory()) {
                if (this.dir.delete()) {
                    if (!this.dir.mkdir()) {
                        try {
                            throw new IOException();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        throw new IOException();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (!this.dir.mkdir()) {
                try {
                    throw new IOException();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void save(final String file, final String content, final boolean append) {
        try {
            final File f = new File(this.dir, file);
            if (!f.exists()) {
                if (!f.getParentFile().exists()) {
                    if (!f.getParentFile().mkdirs()) {
                        throw new IOException();
                    }
                }
                if (!f.createNewFile()) {
                    throw new IOException();
                }
            }
            try (FileWriter writer = new FileWriter(f, append)) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readAsList(final String file) {
        try {
            final File f = new File(this.dir, file);
            if (!f.exists()) {
                if (f.getParentFile() != null && !f.getParentFile().exists()) {
                    if (!f.getParentFile().mkdirs()) {
                        return new ArrayList<>();
                    }
                }
                if (!f.createNewFile()) {
                    return new ArrayList<>();
                }
            }
            ArrayList<String> arrayList = new ArrayList<>();
            try (FileInputStream fis = new FileInputStream(f)) {
                try (InputStreamReader isr = new InputStreamReader(fis)) {
                    try (BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            arrayList.add(line);
                        }
                    }
                }
            }
            return arrayList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String readAsString(final String file) {
        try {
            final File f = new File(this.dir, file);
            if (!f.exists()) {
                if (f.getParentFile() != null && !f.getParentFile().exists()) {
                    if (!f.getParentFile().mkdirs()) {
                        return "";
                    }
                }
                if (!f.createNewFile()) {
                    return "";
                }
            }
            StringBuilder out = new StringBuilder();
            try (FileInputStream fis = new FileInputStream(f)) {
                try (InputStreamReader isr = new InputStreamReader(fis)) {
                    try (BufferedReader br = new BufferedReader(isr)) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            out.append(line).append(System.lineSeparator());
                        }
                    }
                }
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void saveConfig() {
        if (!shouldSave) {
            return;
        }
        JSONObject moduleValueMap = new JSONObject();
        for (Map.Entry<String, Module> entry : Managers.moduleManager.modules.entrySet()) {
            Module module = entry.getValue();
            JSONObject valueMap = new JSONObject();
            for (Map.Entry<String, Value<?>> entryValue : module.getValues().entrySet()) {
                Value<?> v = entryValue.getValue();
                if (v instanceof IntegerValue) {
                    valueMap.put(entryValue.getKey(), ((IntegerValue) v).getValue());
                } else if (v instanceof DecimalValue) {
                    valueMap.put(entryValue.getKey(), ((DecimalValue) v).getValue());
                } else if (v instanceof OptionValue) {
                    valueMap.put(entryValue.getKey(), ((OptionValue) v).getValue());
                } else if (v instanceof ModeValue) {
                    valueMap.put(entryValue.getKey(), ((ModeValue) v).getValue());
                } else if (v instanceof TextValue) {
                    valueMap.put(entryValue.getKey(), ((TextValue) v).getValue());
                } else if (v instanceof PaletteValue) {
                    valueMap.put(entryValue.getKey(), ((PaletteValue) v).getValue());
                }
            }

            if (!valueMap.isEmpty()) {
                moduleValueMap.put(entry.getKey(), valueMap);
            }
        }
        save("module/value.json", moduleValueMap.toString(), false);
        JSONObject moduleStatusMap = new JSONObject();
        for (Map.Entry<String, Module> entry : Managers.moduleManager.modules.entrySet()) {
            Module module = entry.getValue();
            JSONObject statusMap = new JSONObject();
            statusMap.put("Enabled", module.isEnabled());
            statusMap.put("KeyBind", Keyboard.getKeyName(module.getKey()).replace("NONE", "None"));
            moduleStatusMap.put(entry.getKey(), statusMap);
        }
        save("module/status.json", moduleStatusMap.toString(), false);
        JSONObject draggableMap = new JSONObject();
        for (Map.Entry<DraggableGameView, Vec2i> entry : Managers.draggableGameViewManager.draggableMap.entrySet()) {
            Vec2i position = entry.getValue();
            JSONObject positionMap = new JSONObject();
            positionMap.put("x", position.x);
            positionMap.put("y", position.y);
            draggableMap.put(entry.getKey().getIdentity(), positionMap);
        }
        save("draggable.json", draggableMap.toString(), false);
        save("config.json", new JSONObject()
                .put("Config-Version", FileManager.VERSION)
                .put("Language", Managers.i18NManager.getSelectedLanguage())
                .put("Title-Size", Managers.clientManager.titleSize)
                .put("Title-X", Managers.clientManager.titleX)
                .put("Title-Y", Managers.clientManager.titleY)
                .toString(), false);
        StringBuilder stringBuilder = new StringBuilder();
        for (String sarcasticMessage : Managers.clientManager.sarcasticMessages) {
            stringBuilder.append(sarcasticMessage).append(System.lineSeparator());
        }
        save("SarcasticMessages.txt", stringBuilder.toString(), false);
    }
}
