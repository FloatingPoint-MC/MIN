package cn.floatingpoint.min.system.ui.clickgui;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.system.module.Module;
import cn.floatingpoint.min.system.module.value.Value;
import cn.floatingpoint.min.system.module.value.impl.*;
import cn.floatingpoint.min.system.ui.shortcut.GuiManageShortcut;
import cn.floatingpoint.min.utils.math.FunctionUtil;
import cn.floatingpoint.min.utils.math.Vec3f;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjglx.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClickUI extends GuiScreen {
    private int animationLeft;
    private int animationRight;

    private Module moduleToBindKey;
    private static Category selectedCategory = null;
    private static boolean openSetting;
    private static final ArrayList<Module> modulesOpenedValues = new ArrayList<>();
    private static final HashMap<Module, Integer> moduleAnimations = new HashMap<>();
    private static final HashMap<Module, Integer> modulePositions = new HashMap<>();
    private static final HashMap<Category, Integer> categoryAnimations = new HashMap<>();
    private static final HashMap<Category, Integer> categoryPositions = new HashMap<>();
    private static final HashMap<Value<?>, Integer> valueColorAnimations = new HashMap<>();
    private static final HashMap<Value<?>, Integer> valueAnimations = new HashMap<>();
    private static final HashMap<Value<?>, Integer> valuePositions = new HashMap<>();
    private static final HashMap<PaletteValue, Vec3f> palettes = new HashMap<>();
    private static int titleSizeAnimation;
    private static int titleSizeColorAnimation;
    private static boolean selectedTitleSize;
    private static int titleXAnimation;
    private static int titleXColorAnimation;
    private static boolean selectedTitleX;
    private static int titleYAnimation;
    private static int titleYColorAnimation;
    private static boolean selectedTitleY;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private Value<?> selectedValue;
    private int selectType = -1;

    @Override
    public void initGui() {
        animationLeft = 0;
        animationRight = width;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (mc.world == null) {
            drawDefaultBackground();
        }
        GlStateManager.enableBlend();
        int backgroundColor = new Color(40, 40, 40, 102).getRGB();
        int categoryColor = new Color(40, 40, 40, 56).getRGB();
        int textColor = new Color(216, 216, 216).getRGB();
        int brightTextColor = new Color(255, 255, 255).getRGB();
        int disableColor = new Color(255, 0, 0, 102).getRGB();
        int enableColor = new Color(0, 255, 0, 156).getRGB();
        int textValueTextColor = new Color(200, 200, 200).getRGB();
        animationLeft = FunctionUtil.decreasedSpeed(animationLeft, 0, width / 2 - 100, 20.0f);
        animationRight = FunctionUtil.decreasedSpeed(animationRight, width, width / 2 - 220, 40.0f);
        RenderUtil.drawRoundedRect(animationRight, height / 2 - 160, animationRight + 440, height / 2 + 160, 3, backgroundColor);
        RenderUtil.drawRoundedRect(animationLeft - 120, height / 2 - 160, animationLeft, height / 2 + 160, 3, backgroundColor);
        int y = height / 2 - 10;
        categoryAnimations.forEach((c, a) -> categoryAnimations.put(c, FunctionUtil.smooth(a, categoryPositions.get(c), 2.5f)));
        moduleAnimations.forEach((m, a) -> moduleAnimations.put(m, FunctionUtil.smooth(a, modulePositions.get(m), 3.5f)));
        valueAnimations.forEach((v, a) -> valueAnimations.put(v, a + valuePositions.get(v).compareTo(a)));
        for (Category category : Category.values()) {
            categoryAnimations.putIfAbsent(category, 0);
            categoryPositions.putIfAbsent(category, 0);
            RenderUtil.drawRoundedRect(animationLeft - 100, y, animationLeft - 20, y + 20, 2, category == selectedCategory ? backgroundColor : categoryColor);
            Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredString(Managers.i18NManager.getTranslation("module.category." + category.name()), animationLeft - 60, y + 8, textColor);
            y += 30;
        }
        if (!openSetting) {
            if (selectedCategory == null) {
                Managers.fontManager.sourceHansSansCN_Regular_30.drawStringWithShadow("MIN Client,", animationRight + 130, height / 2 - 150, textColor);
                Managers.fontManager.sourceHansSansCN_Regular_34.drawStringWithShadow("MAX Performance.", animationRight + 150, height / 2 - 136, textColor);
            } else {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glPushMatrix();
                RenderUtil.doGlScissor(animationRight + 140, height / 2 - 140, 280, 280);
                y = height / 2 - 140 - categoryAnimations.get(selectedCategory);
                for (Map.Entry<String, ? extends Module> moduleEntry : Managers.moduleManager.getModulesByCategory(selectedCategory).entrySet()) {
                    Module module = moduleEntry.getValue();
                    boolean enabled = module.isEnabled() && module.canBeEnabled();
                    if (!moduleAnimations.containsKey(module)) {
                        moduleAnimations.put(module, 0);
                    }
                    RenderUtil.drawRoundedRect(animationRight + 140, y, animationRight + 420, y + 30 + moduleAnimations.get(module), 3, enabled ? backgroundColor : categoryColor);
                    if (module.canBeEnabled()) {
                        RenderUtil.drawRoundedRect(animationRight + 404, y + 12, animationRight + 410, y + 18, 3, enabled ? enableColor : disableColor);
                    }
                    Managers.fontManager.sourceHansSansCN_Regular_20.drawStringWithShadow(Managers.i18NManager.getTranslation("module.implement." + moduleEntry.getKey()), animationRight + 150, y + 6, enabled ? brightTextColor : textColor);
                    Managers.fontManager.sourceHansSansCN_Regular_18.drawStringWithShadow(Managers.i18NManager.getTranslation("module.implement." + moduleEntry.getKey() + ".description"), animationRight + 150, y + 17, enabled ? brightTextColor : textColor);
                    if (modulesOpenedValues.contains(module)) {
                        int moduleY = 20;
                        for (Map.Entry<String, Value<?>> valueEntry : module.getValues().entrySet()) {
                            Value<?> value = valueEntry.getValue();
                            if (value.isDisplayable()) {
                                if (value instanceof PaletteValue) {
                                    moduleY += 40;
                                }
                                moduleY += 30;
                            }
                        }
                        modulePositions.put(module, moduleY);
                    } else {
                        modulePositions.put(module, 0);
                    }
                    y += 34;
                    y += moduleAnimations.get(module);
                }
                GL11.glPopMatrix();
                y = height / 2 - 140 - categoryAnimations.get(selectedCategory);
                for (Map.Entry<String, ? extends Module> moduleEntry : Managers.moduleManager.getModulesByCategory(selectedCategory).entrySet()) {
                    Module module = moduleEntry.getValue();
                    int minY = Math.max(y, height / 2 - 140);
                    if (minY >= height / 2 + 140) continue;
                    RenderUtil.doGlScissor(animationRight + 140, minY, 280, Math.max(Math.min(Math.min(y + 30 + moduleAnimations.get(module) - Math.max(y, height / 2 - 140), height / 2 + 140 - y), 280), 0));
                    int moduleY = 20;
                    for (Map.Entry<String, Value<?>> valueEntry : module.getValues().entrySet()) {
                        Value<?> value = valueEntry.getValue();
                        valueColorAnimations.putIfAbsent(value, 0);
                        valueAnimations.putIfAbsent(value, 0);
                        valuePositions.putIfAbsent(value, 0);
                        if (value.isDisplayable()) {
                            String valueName = Managers.i18NManager.getTranslation("module.implement." + moduleEntry.getKey() + "." + valueEntry.getKey());
                            String valueDescription = Managers.i18NManager.getTranslation("module.implement." + moduleEntry.getKey() + "." + valueEntry.getKey() + ".description");
                            Managers.fontManager.sourceHansSansCN_Regular_18.drawString(valueName, animationRight + 150, y + moduleY + 16, textColor);
                            Managers.fontManager.sourceHansSansCN_Regular_14.drawString(valueDescription, animationRight + 150, y + moduleY + 26, textColor);
                            if (value instanceof DecimalValue) {
                                doNumberValueAnimations(mouseX, mouseY, y, moduleY, value);
                                drawRect(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, categoryColor);
                                int colorCode = (int) ((valueColorAnimations.get(value) / 255.0f) * 39 + 216);
                                int cardColor = new Color(colorCode, colorCode, colorCode).getRGB();
                                int textColorInNumberValue = new Color(216, 216, 216, valueColorAnimations.get(value)).getRGB();
                                double dist = (((DecimalValue) value).getMaximum() - ((DecimalValue) value).getMinimum());
                                double valuePercent = (((DecimalValue) value).getValue() - ((DecimalValue) value).getMinimum()) / dist;
                                doNumberValueDraw(y, moduleY, value, cardColor, textColorInNumberValue, valuePercent);
                                if (value == selectedValue) {
                                    if (Mouse.isButtonDown(0) && moduleToBindKey == null) {
                                        double percentage = Math.max(0.0, Math.min(mouseX - animationRight - 280.0, 120.0)) / 120.0;
                                        double valueToSet = percentage * dist + ((DecimalValue) value).getMinimum();
                                        double valueDist = valueToSet % ((DecimalValue) value).getIncrement();
                                        valueToSet -= valueDist;
                                        if (valueDist >= ((DecimalValue) value).getIncrement() / 2.0) {
                                            valueToSet += ((DecimalValue) value).getIncrement();
                                        }
                                        ((DecimalValue) value).setValue(valueToSet);
                                    } else {
                                        selectedValue = null;
                                    }
                                }
                            } else if (value instanceof IntegerValue) {
                                doNumberValueAnimations(mouseX, mouseY, y, moduleY, value);
                                drawRect(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, categoryColor);
                                int colorCode = (int) ((valueColorAnimations.get(value) / 255.0f) * 39 + 216);
                                int cardColor = new Color(colorCode, colorCode, colorCode).getRGB();
                                int textColorInNumberValue = new Color(216, 216, 216, valueColorAnimations.get(value)).getRGB();
                                int dist = (((IntegerValue) value).getMaximum() - ((IntegerValue) value).getMinimum());
                                float valuePercent = (((IntegerValue) value).getValue() - ((IntegerValue) value).getMinimum()) / (float) dist;
                                doNumberValueDraw(y, moduleY, value, cardColor, textColorInNumberValue, valuePercent);
                                if (value == selectedValue) {
                                    if (Mouse.isButtonDown(0) && moduleToBindKey == null) {
                                        float percentage = Math.max(0.0f, Math.min(mouseX - animationRight - 280.0f, 120.0f)) / 120.0f;
                                        float valueToSet = percentage * dist + ((IntegerValue) value).getMinimum();
                                        float valueDist = valueToSet % ((IntegerValue) value).getIncrement();
                                        valueToSet -= valueDist;
                                        if (valueDist >= ((IntegerValue) value).getIncrement() / 2.0) {
                                            valueToSet += ((IntegerValue) value).getIncrement();
                                        }
                                        ((IntegerValue) value).setValue((int) valueToSet);
                                    } else {
                                        selectedValue = null;
                                    }
                                }
                            } else if (value instanceof ModeValue) {
                                if (isHovered(animationRight + 340, y + moduleY + 19, animationRight + 400, y + moduleY + 29, mouseX, mouseY)) {
                                    valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 255, 3.0f));
                                } else {
                                    valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 0, 3.0f));
                                }
                                int colorCode = (int) ((valueColorAnimations.get(value) / 255.0f) * 39 + 216);
                                int cardColor = new Color(colorCode, colorCode, colorCode).getRGB();
                                RenderUtil.drawRoundedRect(animationRight + 340, y + moduleY + 19, animationRight + 400, y + moduleY + 29, 2, categoryColor);
                                Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(Managers.i18NManager.getTranslation("module.implement." + moduleEntry.getKey() + "." + valueEntry.getKey() + ".sub." + ((ModeValue) value).getValue()), animationRight + 370, y + moduleY + 20, cardColor);
                            } else if (value instanceof OptionValue) {
                                RenderUtil.drawRoundedRect(animationRight + 394, y + moduleY + 23, animationRight + 400, y + moduleY + 29, 3, ((OptionValue) value).getValue() ? enableColor : disableColor);
                            } else if (value instanceof TextValue) {
                                RenderUtil.drawRoundedRect(animationRight + 300, y + moduleY + 18, animationRight + 400, y + moduleY + 30, 2, categoryColor);
                                mc.fontRenderer.drawStringWithShadow(((TextValue) value).getValue(), animationRight + 304, y + moduleY + 20, selectedValue == value ? brightTextColor : textValueTextColor);
                            } else if (value instanceof PaletteValue) {
                                int color = ((PaletteValue) value).getValue();
                                if (!palettes.containsKey(value)) {
                                    float[] values = Color.RGBtoHSB(color >> 16 & 255, color >> 8 & 255, color & 255, null);
                                    palettes.put((PaletteValue) value, new Vec3f(values[0], values[1], values[2]));
                                }
                                Vec3f vec3f = palettes.get(value);
                                float hue = vec3f.x;
                                float saturation = vec3f.y;
                                float brightness = vec3f.z;
                                float alpha = (color >> 24 & 255) / 255f;
                                // Brightness
                                RenderUtil.drawGradientSideways(animationRight + 344, y + moduleY + 20, animationRight + 384, y + moduleY + 60, -1, Color.HSBtoRGB(hue, 1, 1));
                                RenderUtil.drawGradientRect(animationRight + 344, y + moduleY + 20, animationRight + 384, y + moduleY + 60, new Color(0, 0, 0).getRGB(), new Color(255, 255, 255, 26).getRGB());

                                // Saturation
                                for (int i = 0; i < 5; i++) {
                                    RenderUtil.drawGradientRect(animationRight + 386, y + moduleY + 20 + 8 * i, animationRight + 392, y + moduleY + 20 + 8 * (i + 1), Color.HSBtoRGB(1 - 0.2f * (i + 1), 1, 1), Color.HSBtoRGB(1 - 0.2f * i, 1, 1));
                                }

                                drawRect(animationRight + 394, y + moduleY + 20, animationRight + 400, y + moduleY + 60, new Color(82, 82, 82, 102).getRGB());
                                RenderUtil.drawGradientRect(animationRight + 394, y + moduleY + 20, animationRight + 400, y + moduleY + 60, RenderUtil.reAlpha(color, 1), RenderUtil.reAlpha(color, 0));
                                int bY = (int) (40 - brightness * 40);
                                int sX = (int) (saturation * 40);
                                RenderUtil.drawFilledCircle(animationRight + 344 + sX, y + moduleY + 20 + bY, 1, new Color(255, 255, 255).getRGB(), 5);

                                int hueY = (int) ((1.0f - hue) * 40);
                                Gui.drawRect(animationRight + 386, y + moduleY + 20 + hueY, animationRight + 392, y + moduleY + 20 + hueY + 1, new Color(255, 255, 255, 255).getRGB());
                                int alphaY = (int) ((1 - alpha) * 40);
                                Gui.drawRect(animationRight + 394, y + moduleY + 20 + alphaY, animationRight + 400, y + moduleY + 20 + alphaY + 1, new Color(255, 255, 255, 255).getRGB());
                                if (selectedValue == value) {
                                    if (Mouse.isButtonDown(0)) {
                                        if (selectType == 0) {
                                            int currentX = mouseX - animationRight - 344;
                                            int currentY = mouseY - y - moduleY - 20;
                                            currentX = Math.min(40, currentX);
                                            currentX = Math.max(0, currentX);
                                            currentY = Math.min(40, currentY);
                                            currentY = Math.max(0, currentY);
                                            saturation = currentX / 40.0f;
                                            brightness = 1.0f - currentY / 40.0f;
                                        } else if (selectType == 1) {
                                            int currentY = mouseY - y - moduleY - 20;
                                            currentY = Math.min(40, currentY);
                                            currentY = Math.max(0, currentY);
                                            hue = 1.0f - currentY / 40.0f;
                                        } else if (selectType == 2) {
                                            int currentY = mouseY - y - moduleY - 20;
                                            currentY = Math.min(40, currentY);
                                            currentY = Math.max(0, currentY);
                                            alpha = 1.0f - currentY / 40.0f;
                                        }
                                    } else {
                                        selectedValue = null;
                                        selectType = -1;
                                    }
                                }
                                color = new Color(RenderUtil.reAlpha(Color.HSBtoRGB(hue, saturation, brightness), (int) (alpha * 255)), true).getRGB();
                                palettes.put((PaletteValue) value, new Vec3f(hue, saturation, brightness));
                                ((PaletteValue) value).setValue(color);
                                moduleY += 40;
                            }
                            moduleY += 30;
                        }
                    }
                    y += 34;
                    y += moduleAnimations.get(module);
                }
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                int dWheel = Mouse.getDWheel();
                if (dWheel != 0) {
                    if (isHovered(animationRight + 140, height / 2 - 140, animationRight + 420, height / 2 + 140, mouseX, mouseY)) {
                        if (dWheel < 0) {
                            int current = categoryPositions.get(selectedCategory);
                            if (y > height / 2 + 140) {
                                current += 20;
                            }
                            categoryPositions.put(selectedCategory, current);
                        } else {
                            int current = categoryPositions.get(selectedCategory);
                            if (current >= 20) {
                                current -= 20;
                            }
                            categoryPositions.put(selectedCategory, current);
                        }
                    }
                }
            }
        } else {
            Managers.fontManager.sourceHansSansCN_Regular_30.drawStringWithShadow(Managers.i18NManager.getTranslation("clickgui.setting"), animationRight + 130, height / 2 - 150, textColor);
            int cardColor = new Color(255, 255, 255).getRGB();
            String language = Managers.i18NManager.getTranslation("clickgui.language");
            Managers.fontManager.sourceHansSansCN_Regular_20.drawString(language, animationRight + 150, height / 2 - 110, textColor);
            RenderUtil.drawRoundedRect(animationRight + 340, height / 2 - 110, animationRight + 400, height / 2 - 100, 2, categoryColor);
            Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(Managers.i18NManager.getSelectedLanguage(), animationRight + 370, height / 2 - 109, cardColor);

            String title = Managers.i18NManager.getTranslation("clickgui.titleSize");
            Managers.fontManager.sourceHansSansCN_Regular_20.drawString(title, animationRight + 150, height / 2 - 80, textColor);
            float valuePercent = Managers.clientManager.titleSize;
            titleSizeAnimation = titleSizeAnimation + Integer.compare((int) (valuePercent * 120), titleSizeAnimation);
            int textColorInNumberValue = new Color(216, 216, 216, titleSizeColorAnimation).getRGB();
            if (isHovered(animationRight + 280, height / 2 - 82, animationRight + 400, height / 2 - 74, mouseX, mouseY) || selectedTitleSize) {
                titleSizeColorAnimation = FunctionUtil.smooth(titleSizeColorAnimation, 255, 3.0f);
            } else {
                titleSizeColorAnimation = FunctionUtil.smooth(titleSizeColorAnimation, 0, 3.0f);
            }
            if (selectedTitleSize) {
                if (Mouse.isButtonDown(0)) {
                    Managers.clientManager.titleSize = Math.max(0.0f, Math.min(mouseX - animationRight - 280.0f, 120.0f)) / 120.0f;
                } else {
                    selectedTitleSize = false;
                }
            }
            drawRect(animationRight + 280, height / 2 - 82, animationRight + 400, height / 2 - 74, categoryColor);
            drawRect(animationRight + 279 + titleSizeAnimation, height / 2 - 83, animationRight + 280 + titleSizeAnimation, height / 2 - 73, cardColor);
            Managers.fontManager.sourceHansSansCN_Regular_14.drawCenteredString(new DecimalFormat("#.##").format(valuePercent), animationRight + 280 + titleSizeAnimation, height / 2 - 71, textColorInNumberValue);

            title = Managers.i18NManager.getTranslation("clickgui.titleX");
            Managers.fontManager.sourceHansSansCN_Regular_20.drawString(title, animationRight + 150, height / 2 - 50, textColor);
            valuePercent = (400.0f + Managers.clientManager.titleX) / 800.0f;
            titleXAnimation = titleXAnimation + Integer.compare((int) (valuePercent * 120), titleXAnimation);
            textColorInNumberValue = new Color(216, 216, 216, titleXColorAnimation).getRGB();
            if (isHovered(animationRight + 280, height / 2 - 52, animationRight + 400, height / 2 - 44, mouseX, mouseY) || selectedTitleX) {
                titleXColorAnimation = FunctionUtil.smooth(titleXColorAnimation, 255, 3.0f);
            } else {
                titleXColorAnimation = FunctionUtil.smooth(titleXColorAnimation, 0, 3.0f);
            }
            if (selectedTitleX) {
                if (Mouse.isButtonDown(0)) {
                    Managers.clientManager.titleX = Math.max(0.0f, Math.min(mouseX - animationRight - 280.0f, 120.0f)) / 120.0f * 800.0f - 400.0f;
                } else {
                    selectedTitleX = false;
                }
            }
            drawRect(animationRight + 280, height / 2 - 52, animationRight + 400, height / 2 - 44, categoryColor);
            drawRect(animationRight + 279 + titleXAnimation, height / 2 - 53, animationRight + 280 + titleXAnimation, height / 2 - 43, cardColor);
            Managers.fontManager.sourceHansSansCN_Regular_14.drawCenteredString(decimalFormat.format(Managers.clientManager.titleX), animationRight + 280 + titleXAnimation, height / 2 - 41, textColorInNumberValue);

            title = Managers.i18NManager.getTranslation("clickgui.titleY");
            Managers.fontManager.sourceHansSansCN_Regular_20.drawString(title, animationRight + 150, height / 2 - 20, textColor);
            valuePercent = (400.0f + Managers.clientManager.titleY) / 800.0f;
            titleYAnimation = titleYAnimation + Integer.compare((int) (valuePercent * 120), titleYAnimation);
            textColorInNumberValue = new Color(216, 216, 216, titleYColorAnimation).getRGB();
            if (isHovered(animationRight + 280, height / 2 - 22, animationRight + 400, height / 2 - 14, mouseX, mouseY) || selectedTitleY) {
                titleYColorAnimation = FunctionUtil.smooth(titleYColorAnimation, 255, 3.0f);
            } else {
                titleYColorAnimation = FunctionUtil.smooth(titleYColorAnimation, 0, 3.0f);
            }
            if (selectedTitleY) {
                if (Mouse.isButtonDown(0)) {
                    Managers.clientManager.titleY = Math.max(0.0f, Math.min(mouseX - animationRight - 280.0f, 120.0f)) / 120.0f * 800.0f - 400.0f;
                } else {
                    selectedTitleY = false;
                }
            }
            drawRect(animationRight + 280, height / 2 - 22, animationRight + 400, height / 2 - 14, categoryColor);
            drawRect(animationRight + 279 + titleYAnimation, height / 2 - 23, animationRight + 280 + titleYAnimation, height / 2 - 13, cardColor);
            Managers.fontManager.sourceHansSansCN_Regular_14.drawCenteredString(decimalFormat.format(Managers.clientManager.titleY), animationRight + 280 + titleYAnimation, height / 2 - 11, textColorInNumberValue);

            Managers.fontManager.sourceHansSansCN_Regular_20.drawString(Managers.i18NManager.getTranslation("clickgui.adsorption"), animationRight + 150, height / 2 + 10, textColor);
            RenderUtil.drawRoundedRect(animationRight + 394, height / 2 + 13, animationRight + 400, height / 2 + 19, 3, Managers.clientManager.adsorption ? enableColor : disableColor);
            RenderUtil.drawRoundedRect(animationRight + 150, height / 2 + 39, animationRight + 250, height / 2 + 49, 3, categoryColor);
            Managers.fontManager.sourceHansSansCN_Regular_20.drawCenteredString(Managers.i18NManager.getTranslation("clickgui.shortcut"), animationRight + 200, height / 2 + 40, textColor);
        }
        RenderUtil.drawImage(new ResourceLocation("min/logo.png"), animationLeft - 110, height / 2 - 140, 100, 100);
        RenderUtil.drawImage(new ResourceLocation("min/uis/clickgui/setting.png"), animationLeft - 118, height / 2 + 142, 16, 16);
        if (moduleToBindKey != null) {
            drawRect(0, 0, width, height, categoryColor);
            Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredString(Managers.i18NManager.getTranslation("clickgui.bind"), width / 2, height / 2 - 8, brightTextColor);
        }
        GlStateManager.disableBlend();
    }

    private void doNumberValueAnimations(int mouseX, int mouseY, int y, int moduleY, Value<?> value) {
        boolean hovered = isHovered(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, mouseX, mouseY);
        if ((hovered && selectedValue == null) || value == selectedValue) {
            valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 255, 3.0f));
        } else {
            valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 0, 3.0f));
        }
    }

    private void doNumberValueDraw(int y, int moduleY, Value<?> value, int cardColor, int textColorInNumberValue, double valuePercent) {
        valuePositions.put(value, (int) (valuePercent * 120));
        drawRect(animationRight + 279 + valueAnimations.get(value), y + moduleY + 17, animationRight + 280 + valueAnimations.get(value), y + moduleY + 27, cardColor);
        Managers.fontManager.sourceHansSansCN_Regular_14.drawCenteredString(decimalFormat.format(value.getValue()), animationRight + 280 + valueAnimations.get(value), y + moduleY + 28, textColorInNumberValue);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (moduleToBindKey != null) return;
        int y = height / 2 - 10;
        for (Category category : Category.values()) {
            if (isHovered(animationLeft - 100, y, animationLeft - 20, y + 20, mouseX, mouseY)) {
                selectedCategory = category;
                openSetting = false;
                return;
            }
            y += 30;
        }
        if (!openSetting) {
            if (isHovered(animationRight + 120, height / 2 - 140, animationRight + 420, height / 2 + 140, mouseX, mouseY)) {
                selectedValue = null;
                if (selectedCategory != null) {
                    y = height / 2 - 140 - categoryAnimations.get(selectedCategory);
                    for (Map.Entry<String, ? extends Module> moduleEntry : Managers.moduleManager.getModulesByCategory(selectedCategory).entrySet()) {
                        Module module = moduleEntry.getValue();
                        if (isHovered(animationRight + 140, y, animationRight + 420, y + 30, mouseX, mouseY)) {
                            if (mouseButton == 0) {
                                if (module.canBeEnabled()) {
                                    module.toggle();
                                    return;
                                }
                            } else if (mouseButton == 1) {
                                if (!module.getValues().isEmpty()) {
                                    if (modulesOpenedValues.contains(module)) {
                                        modulesOpenedValues.remove(module);
                                    } else {
                                        modulesOpenedValues.add(module);
                                    }
                                    return;
                                }
                            } else if (mouseButton == 2) {
                                moduleToBindKey = module;
                                return;
                            }
                        }
                        if (mouseButton == 0) {
                            if (modulesOpenedValues.contains(module)) {
                                int moduleY = 20;
                                for (Map.Entry<String, Value<?>> valueEntry : module.getValues().entrySet()) {
                                    Value<?> value = valueEntry.getValue();
                                    if (value.isDisplayable()) {
                                        if (value instanceof DecimalValue) {
                                            if (isHovered(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, mouseX, mouseY)) {
                                                selectedValue = value;
                                                return;
                                            }
                                        } else if (value instanceof IntegerValue) {
                                            if (isHovered(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, mouseX, mouseY)) {
                                                selectedValue = value;
                                                return;
                                            }
                                        } else if (value instanceof ModeValue) {
                                            if (isHovered(animationRight + 340, y + moduleY + 19, animationRight + 400, y + moduleY + 29, mouseX, mouseY)) {
                                                ((ModeValue) value).nextMode();
                                                return;
                                            }
                                        } else if (value instanceof OptionValue) {
                                            if (isHovered(animationRight + 394, y + moduleY + 23, animationRight + 400, y + moduleY + 29, mouseX, mouseY)) {
                                                ((OptionValue) value).setValue(!((OptionValue) value).getValue());
                                                return;
                                            }
                                        } else if (value instanceof TextValue) {
                                            if (isHovered(animationRight + 300, y + moduleY + 18, animationRight + 400, y + moduleY + 30, mouseX, mouseY)) {
                                                selectedValue = value;
                                                return;
                                            }
                                        } else if (value instanceof PaletteValue) {
                                            if (isHovered(animationRight + 344, y + moduleY + 20, animationRight + 384, y + moduleY + 60, mouseX, mouseY)) {
                                                selectedValue = value;
                                                selectType = 0;
                                                return;
                                            } else if (isHovered(animationRight + 386, y + moduleY + 20, animationRight + 392, y + moduleY + 60, mouseX, mouseY)) {
                                                selectedValue = value;
                                                selectType = 1;
                                                return;
                                            } else if (isHovered(animationRight + 394, y + moduleY + 20, animationRight + 400, y + moduleY + 60, mouseX, mouseY)) {
                                                selectedValue = value;
                                                selectType = 2;
                                                return;
                                            }
                                            moduleY += 40;
                                        }
                                        moduleY += 30;
                                    }
                                }
                            }
                        }
                        y += 34;
                        y += moduleAnimations.get(module);
                    }
                }
            } else {
                selectedCategory = null;
                if (isHovered(animationLeft - 118, height / 2 + 142, animationLeft - 102, height / 2 + 158, mouseX, mouseY)) {
                    openSetting = true;
                }
            }
        } else {
            if (isHovered(animationRight + 120, height / 2 - 140, animationRight + 420, height / 2 + 140, mouseX, mouseY)) {
                if (isHovered(animationRight + 340, height / 2 - 110, animationRight + 400, height / 2 - 100, mouseX, mouseY)) {
                    Managers.i18NManager.nextLanguage();
                } else if (isHovered(animationRight + 280, height / 2 - 82, animationRight + 400, height / 2 - 74, mouseX, mouseY)) {
                    selectedTitleSize = true;
                } else if (isHovered(animationRight + 280, height / 2 - 52, animationRight + 400, height / 2 - 44, mouseX, mouseY)) {
                    selectedTitleX = true;
                } else if (isHovered(animationRight + 280, height / 2 - 22, animationRight + 400, height / 2 - 14, mouseX, mouseY)) {
                    selectedTitleY = true;
                } else if (isHovered(animationRight + 394, height / 2 + 13, animationRight + 400, height / 2 + 19, mouseX, mouseY)) {
                    Managers.clientManager.adsorption = !Managers.clientManager.adsorption;
                } else if (isHovered(animationRight + 150, height / 2 + 39, animationRight + 250, height / 2 + 49, mouseX, mouseY)) {
                    mc.displayGuiScreen(new GuiManageShortcut(this));
                }
            } else {
                selectedCategory = null;
                if (!isHovered(animationLeft - 118, height / 2 + 142, animationLeft - 102, height / 2 + 158, mouseX, mouseY)) {
                    openSetting = false;
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (selectedValue instanceof TextValue) {
            if (keyCode == 1) {
                selectedValue = null;
                return;
            }
            ((TextValue) selectedValue).updateText(typedChar, keyCode);
            return;
        }
        if (moduleToBindKey != null) {
            if (keyCode == 1) {
                keyCode = 0;
            }
            moduleToBindKey.setKey(keyCode);
            moduleToBindKey = null;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
