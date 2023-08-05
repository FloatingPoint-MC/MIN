package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class I18NManager implements Manager {
    private final HashMap<String, Map<String, Object>> translations = new HashMap<>();
    private final ArrayList<String> translationKeys = new ArrayList<>();
    private String selectedLanguage = "English";

    @Override
    public String getName() {
        return "I18N Manager";
    }

    @SuppressWarnings("all")
    @Override
    public void init() {
        File dir = Managers.fileManager.getConfigFile("translations");
        if (!dir.exists() || !dir.isDirectory()) {
            dir.delete();
            dir.mkdir();
        }
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".json");
            }
        });
        int loaded = 0;
        for (File file : files) {
            try {
                JSONObject jsonObject = new JSONObject(Managers.fileManager.readAsString("translations/" + file.getName()));
                if (!jsonObject.has("language.version") || jsonObject.getInt("language.version") != FileManager.VERSION) {
                    continue;
                }
                String key = jsonObject.has("language.identity") ? jsonObject.getString("language.identity") : file.getName().substring(0, file.getName().length() - 5);
                translations.put(key, jsonObject.toMap());
                translationKeys.add(key);
            } catch (Exception e) {
                System.err.println("Error while loading language: " + file.getName());
            }
            loaded++;
        }
        try {
            // 英语
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/assets/minecraft/min/translations/English.json");
            InputStreamReader in = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(in);
            StringBuilder sb = new StringBuilder();
            String s1;
            while ((s1 = reader.readLine()) != null) {
                sb.append(s1).append(System.lineSeparator());
            }
            Managers.fileManager.save("translations/English.json", sb.toString(), false);
            translations.put("English", new JSONObject(Managers.fileManager.readAsString("translations/English.json")).toMap());
            translationKeys.add("English");

            // 中文
            resourceAsStream = this.getClass().getResourceAsStream("/assets/minecraft/min/translations/zh-cn.json");
            in = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            reader = new BufferedReader(in);
            sb = new StringBuilder();
            while ((s1 = reader.readLine()) != null) {
                sb.append(s1).append(System.lineSeparator());
            }
            Managers.fileManager.save("translations/zh-cn.json", sb.toString(), false);
            translations.put("简体中文", new JSONObject(Managers.fileManager.readAsString("translations/zh-cn.json")).toMap());
            translationKeys.add("简体中文");
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public String getTranslation(String key) {
        return String.valueOf(translations.get(selectedLanguage).getOrDefault(key, key));
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        if (translations.containsKey(selectedLanguage)) {
            this.selectedLanguage = selectedLanguage;
        }
    }

    public void nextLanguage() {
        boolean found = false;
        for (String language : translationKeys) {
            if (found) {
                setSelectedLanguage(language);
                return;
            }
            if (language.equalsIgnoreCase(selectedLanguage)) {
                found = true;
            }
        }
        if (found) {
            setSelectedLanguage(translationKeys.get(0));
        }
    }
}
