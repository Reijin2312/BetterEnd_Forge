package org.betterx.betterend.util;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;


public class LangUtil {
    public final static String CONFIG_ELEMENT = "configuration";

    private String element;

    public LangUtil(String element) {
        this.element = element;
    }

    public void setElement(String key) {
        this.element = key;
    }

    public String getString(String key) {
        return getString(element, key);
    }

    public MutableComponent getText(String key) {
        return getText(element, key);
    }

    public static String translate(String key) {
        return I18n.get(key);
    }

    public static String getString(String element, String key) {
        return translate(String.format("%s.%s", element, key));
    }

    public static MutableComponent getText(String element, String key) {
        return Component.translatable(getString(element, key));
    }
}
