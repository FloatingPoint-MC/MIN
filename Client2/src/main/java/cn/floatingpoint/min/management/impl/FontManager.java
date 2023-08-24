package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.system.ui.font.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class FontManager implements Manager {
    public FontRenderer sourceHansSansCN_Regular_14;
    public FontRenderer sourceHansSansCN_Regular_18;
    public FontRenderer sourceHansSansCN_Regular_20;
    public FontRenderer sourceHansSansCN_Regular_22;
    public FontRenderer sourceHansSansCN_Regular_26;
    public FontRenderer sourceHansSansCN_Regular_30;
    public FontRenderer sourceHansSansCN_Regular_34;
    public FontRenderer comfortaa_25;

    @Override
    public String getName() {
        return "Font Manager";
    }

    @Override
    public void init() {
        sourceHansSansCN_Regular_14 = new FontRenderer(new ResourceLocation("min/fonts/shs.ttf"), 14);
        sourceHansSansCN_Regular_18 = new FontRenderer(new ResourceLocation("min/fonts/shs.ttf"), 18);
        sourceHansSansCN_Regular_20 = new FontRenderer(new ResourceLocation("min/fonts/shs.ttf"), 20);
        sourceHansSansCN_Regular_22 = new FontRenderer(new ResourceLocation("min/fonts/shs.ttf"), 22);
        sourceHansSansCN_Regular_26 = new FontRenderer(new ResourceLocation("min/fonts/shs.ttf"), 26);
        sourceHansSansCN_Regular_30 = new FontRenderer(new ResourceLocation("min/fonts/shs.ttf"), 30);
        sourceHansSansCN_Regular_34 = new FontRenderer(new ResourceLocation("min/fonts/shs.ttf"), 34);
        comfortaa_25 = new FontRenderer(new ResourceLocation("min/fonts/comfortaa.ttf"), 25);
    }
}
