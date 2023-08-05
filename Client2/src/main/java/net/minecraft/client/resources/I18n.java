package net.minecraft.client.resources;

import java.util.Map;

public class I18n
{
    private static Locale i18nLocale;

    static void setLocale()
    {
        i18nLocale = LanguageManager.CURRENT_LOCALE;
    }

    /**
     * Translates the given string and then formats it. Equivilant to String.format(translate(key), parameters).
     */
    public static String format(String translateKey, Object... parameters)
    {
        return i18nLocale.formatMessage(translateKey, parameters);
    }

    public static boolean hasKey(String key)
    {
        return i18nLocale.hasKey(key);
    }

    public static Map<String, String> getLocaleProperties()
    {
        return i18nLocale.properties;
    }
}
