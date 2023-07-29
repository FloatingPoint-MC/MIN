package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 16:06:25
 */
public class PotionDisplay extends RenderModule implements DraggableGameView {
    private final ModeValue mode = new ModeValue(new String[]{"MC", "FPSMaster"}, "MC");
    private final OptionValue shadow = new OptionValue(false, () -> mode.isCurrentMode("FPSMaster"));
    private final OptionValue background = new OptionValue(false, () -> mode.isCurrentMode("FPSMaster"));
    public static GuiIngame guiIngame;
    private int width, height;

    public PotionDisplay() {
        addValues(
                new Pair<>("Mode", mode),
                new Pair<>("Shadow", shadow),
                new Pair<>("Background", background)
        );
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {

    }

    @Override
    public boolean draw(int x, int y) {
        if (!this.isEnabled()) return false;
        Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();
        if (!collection.isEmpty()) {
            if (mode.isCurrentMode("FPSMaster")) {
                width = 166;
                int oldY = y;
                int textColor = new Color(216, 216, 216, 216).getRGB();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableLighting();
                int l = 33;
                if (collection.size() > 5) {
                    l = 132 / (collection.size() - 1);
                }
                for (PotionEffect potioneffect : this.mc.player.getActivePotionEffects()) {
                    if (background.getValue()) {
                        Gui.drawRect(x, y + 1, x + 166, y + l - 1, new Color(0, 0, 0, 80).getRGB());
                    }
                    Potion potion = potioneffect.getPotion();
                    GlStateManager.enableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                    if (potion.hasStatusIcon()) {
                        int index = potion.getStatusIconIndex();
                        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x + 5, y + 7, index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                    }
                    Managers.fontManager.sourceHansSansCN_Regular_18.drawString(InventoryEffectRenderer.getPotionDisplayString(potioneffect, potion), x + 27, y + 8, textColor, shadow.getValue());
                    String s = Potion.getPotionDurationString(potioneffect, 1.0F);
                    Managers.fontManager.sourceHansSansCN_Regular_18.drawString(s, x + 27, y + 18, textColor, shadow.getValue());
                    y += l;
                }
                height = y - oldY;
            } else {
                int i = 0;
                int j = 1;
                for (PotionEffect potionEffect : collection) {
                    if (potionEffect.getPotion().hasStatusIcon() && potionEffect.doesShowParticles()) {
                        if (potionEffect.getPotion().isBeneficial()) {
                            ++i;
                        } else {
                            ++j;
                        }
                    }
                }
                width = i * 25;
                height = j * 24;
                this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                GlStateManager.pushMatrix();
                GlStateManager.translate(width, 0.0f, 0.0f);
                GlStateManager.enableBlend();
                i = 0;
                j = 0;
                Iterator<PotionEffect> iterator = Ordering.natural().reverse().sortedCopy(collection).iterator();
                while (true) {
                    PotionEffect potioneffect;
                    Potion potion;
                    boolean flag;
                    if (!iterator.hasNext()) {
                        GlStateManager.popMatrix();
                        return false;
                    }
                    potioneffect = iterator.next();
                    potion = potioneffect.getPotion();
                    flag = potion.hasStatusIcon();
                    if (flag && potioneffect.doesShowParticles()) {
                        int k = x;
                        int l = y;
                        int i1 = potion.getStatusIconIndex();
                        if (potion.isBeneficial()) {
                            ++i;
                            k = k - 25 * i;
                        } else {
                            ++j;
                            k = k - 25 * j;
                            l += 26;
                        }
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        float f = 1.0F;

                        if (potioneffect.getIsAmbient()) {
                            guiIngame.drawTexturedModalRect(k, l, 165, 166, 24, 24);
                        } else {
                            guiIngame.drawTexturedModalRect(k, l, 141, 166, 24, 24);

                            if (potioneffect.getDuration() <= 200) {
                                int j1 = 10 - potioneffect.getDuration() / 20;
                                f = MathHelper.clamp((float) potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float) potioneffect.getDuration() * (float) Math.PI / 5.0F) * MathHelper.clamp((float) j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                            }
                        }

                        GlStateManager.color(1.0F, 1.0F, 1.0F, f);
                        guiIngame.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                    }
                }
            }
        } else {
            width = 0;
            height = 0;
        }
        return true;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getIdentity() {
        return "PotionDisplay";
    }
}
