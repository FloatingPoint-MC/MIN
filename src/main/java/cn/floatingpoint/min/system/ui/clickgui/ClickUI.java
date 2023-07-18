package cn.floatingpoint.min.system.ui.clickgui;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.system.module.Module;
import cn.floatingpoint.min.system.module.value.Value;
import cn.floatingpoint.min.system.module.value.impl.*;
import cn.floatingpoint.min.utils.math.FunctionUtil;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClickUI extends GuiScreen {
    private int animationLeft;
    private int animationRight;

    private Module moduleToBindKey;
    private static Category selectedCategory = null;
    private boolean openSetting;
    private static final ArrayList<Module> modulesOpenedValues = new ArrayList<>();
    private static final HashMap<Module, Integer> moduleAnimations = new HashMap<>();
    private static final HashMap<Module, Integer> modulePositions = new HashMap<>();
    private static final HashMap<Category, Integer> categoryAnimations = new HashMap<>();
    private static final HashMap<Category, Integer> categoryPositions = new HashMap<>();
    private static final HashMap<Value<?>, Integer> valueColorAnimations = new HashMap<>();
    private static final HashMap<Value<?>, Integer> valueAnimations = new HashMap<>();
    private static final HashMap<Value<?>, Integer> valuePositions = new HashMap<>();
    private Value<?> selectedValue;

    @Override
    public void initGui() {
        animationLeft = 0;
        animationRight = width;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
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
                                moduleY += 30;
                            }
                        }
                        modulePositions.put(module, moduleY);
                    } else {
                        modulePositions.put(module, 0);
                    }
                    y += moduleAnimations.get(module);
                    y += 34;
                }
                GL11.glPopMatrix();
                y = height / 2 - 140 - categoryAnimations.get(selectedCategory);
                for (Map.Entry<String, ? extends Module> moduleEntry : Managers.moduleManager.getModulesByCategory(selectedCategory).entrySet()) {
                    Module module = moduleEntry.getValue();
                    RenderUtil.doGlScissor(animationRight + 140, Math.max(y, height / 2 - 140), 280, Math.min(y + 30 + moduleAnimations.get(module) - Math.max(y, height / 2 - 140), height / 2 + 140 - y));
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
                                boolean hovered = isHovered(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, mouseX, mouseY);
                                if (hovered || value == selectedValue) {
                                    valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 255, 3.0f));
                                } else {
                                    valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 0, 3.0f));
                                }
                                int colorCode = (int) ((valueColorAnimations.get(value) / 255.0f) * 39 + 216);
                                int cardColor = new Color(colorCode, colorCode, colorCode).getRGB();
                                int textColorInNumberValue = new Color(216, 216, 216, valueColorAnimations.get(value)).getRGB();
                                drawRect(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, categoryColor);
                                double dist = (((DecimalValue) value).getMaximum() - ((DecimalValue) value).getMinimum());
                                double valuePercent = (((DecimalValue) value).getValue() - ((DecimalValue) value).getMinimum()) / dist;
                                valuePositions.put(value, (int) (valuePercent * 120));
                                drawRect(animationRight + 279 + valueAnimations.get(value), y + moduleY + 17, animationRight + 280 + valueAnimations.get(value), y + moduleY + 27, cardColor);
                                Managers.fontManager.sourceHansSansCN_Regular_14.drawCenteredString(String.valueOf(value.getValue()), animationRight + 280 + valueAnimations.get(value), y + moduleY + 28, textColorInNumberValue);
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
                                boolean hovered = isHovered(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, mouseX, mouseY);
                                if (hovered || value == selectedValue) {
                                    valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 255, 3.0f));
                                } else {
                                    valueColorAnimations.put(value, FunctionUtil.smooth(valueColorAnimations.get(value), 0, 3.0f));
                                }
                                int colorCode = (int) ((valueColorAnimations.get(value) / 255.0f) * 39 + 216);
                                int cardColor = new Color(colorCode, colorCode, colorCode).getRGB();
                                int textColorInNumberValue = new Color(216, 216, 216, valueColorAnimations.get(value)).getRGB();
                                drawRect(animationRight + 280, y + moduleY + 18, animationRight + 400, y + moduleY + 26, categoryColor);
                                int dist = (((IntegerValue) value).getMaximum() - ((IntegerValue) value).getMinimum());
                                float valuePercent = (((IntegerValue) value).getValue() - ((IntegerValue) value).getMinimum()) / (float) dist;
                                valuePositions.put(value, (int) (valuePercent * 120));
                                drawRect(animationRight + 279 + valueAnimations.get(value), y + moduleY + 17, animationRight + 280 + valueAnimations.get(value), y + moduleY + 27, cardColor);
                                Managers.fontManager.sourceHansSansCN_Regular_14.drawCenteredString(String.valueOf(value.getValue()), animationRight + 280 + valueAnimations.get(value), y + moduleY + 28, textColorInNumberValue);
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
                                Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(((ModeValue) value).getValue(), animationRight + 370, y + moduleY + 20, cardColor);
                                moduleY += valueAnimations.get(value);
                            } else if (value instanceof OptionValue) {
                                RenderUtil.drawRoundedRect(animationRight + 394, y + moduleY + 23, animationRight + 400, y + moduleY + 29, 3, ((OptionValue) value).getValue() ? enableColor : disableColor);
                            } else if (value instanceof TextValue) {
                                RenderUtil.drawRoundedRect(animationRight + 300, y + moduleY + 18, animationRight + 400, y + moduleY + 30, 2, categoryColor);
                                mc.fontRenderer.drawStringWithShadow(((TextValue) value).getValue(), animationRight + 304, y + moduleY + 20, selectedValue == value ? brightTextColor : textValueTextColor);
                            }
                            moduleY += 30;
                        }
                    }
                    y += 34;
                    y += moduleAnimations.get(module);
                }
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                int dWheel = Mouse.getDWheel();
                if (isHovered(animationRight + 140, height / 2 - 140, animationRight + 420, height / 2 + 140, mouseX, mouseY)) {
                    if (dWheel < 0) {
                        int current = categoryPositions.get(selectedCategory);
                        if (current + y <= -20) {
                            current += 20;
                        }
                        categoryPositions.put(selectedCategory, current);
                    } else if (dWheel > 0) {
                        int current = categoryPositions.get(selectedCategory);
                        if (current >= 20) {
                            current -= 20;
                        }
                        categoryPositions.put(selectedCategory, current);
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
        }
        RenderUtil.drawImage(new ResourceLocation("min/logo.png"), animationLeft - 110, height / 2 - 140, 100, 100);
        RenderUtil.drawImage(new ResourceLocation("min/uis/setting.png"), animationLeft - 118, height / 2 + 142, 16, 16);
        if (moduleToBindKey != null) {
            drawRect(0, 0, width, height, categoryColor);
            Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredString(Managers.i18NManager.getTranslation("clickgui.bind"), width / 2, height / 2 - 8, brightTextColor);
        }
        GlStateManager.disableBlend();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
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
                        if (isHovered(animationRight + 140, y - categoryAnimations.get(selectedCategory), animationRight + 420, y + 30 - categoryAnimations.get(selectedCategory), mouseX, mouseY)) {
                            if (mouseButton == 0) {
                                if (module.canBeEnabled()) {
                                    module.toggle();
                                }
                            } else if (mouseButton == 1) {
                                if (!module.getValues().isEmpty()) {
                                    if (modulesOpenedValues.contains(module)) {
                                        modulesOpenedValues.remove(module);
                                    } else {
                                        modulesOpenedValues.add(module);
                                    }
                                }
                            } else if (mouseButton == 2) {
                                moduleToBindKey = module;
                            }
                        }
                        if (mouseButton == 0) {
                            if (modulesOpenedValues.contains(module)) {
                                int moduleY = 20;
                                for (Map.Entry<String, Value<?>> valueEntry : module.getValues().entrySet()) {
                                    Value<?> value = valueEntry.getValue();
                                    if (!valueColorAnimations.containsKey(value)) {
                                        valueColorAnimations.put(value, 0);
                                    }
                                    if (value.isDisplayable()) {
                                        if (value instanceof DecimalValue) {
                                            if (isHovered(animationRight + 280, y + moduleY + 8, animationRight + 400, y + moduleY + 16, mouseX, mouseY)) {
                                                selectedValue = value;
                                            }
                                        } else if (value instanceof IntegerValue) {
                                            if (isHovered(animationRight + 280, y + moduleY + 8, animationRight + 400, y + moduleY + 16, mouseX, mouseY)) {
                                                selectedValue = value;
                                            }
                                        } else if (value instanceof ModeValue) {
                                            if (isHovered(animationRight + 340, y + moduleY + 19, animationRight + 400, y + moduleY + 29, mouseX, mouseY)) {
                                                ((ModeValue) value).nextMode();
                                            }
                                        } else if (value instanceof OptionValue) {
                                            if (isHovered(animationRight + 394, y + moduleY + 23, animationRight + 400, y + moduleY + 29, mouseX, mouseY)) {
                                                ((OptionValue) value).setValue(!((OptionValue) value).getValue());
                                            }
                                        } else if (value instanceof TextValue) {
                                            if (isHovered(animationRight + 300, y + moduleY + 18, animationRight + 400, y + moduleY + 30, mouseX, mouseY)) {
                                                selectedValue = value;
                                            }
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
