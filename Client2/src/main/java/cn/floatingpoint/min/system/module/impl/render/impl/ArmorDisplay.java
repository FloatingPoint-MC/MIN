package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-24 11:36:18
 */
public class ArmorDisplay extends RenderModule implements DraggableGameView {
    private final ModeValue mode = new ModeValue(new String[]{"V", "H"}, "V");
    private int width, height;
    private boolean drawable;

    public ArmorDisplay() {
        addValues(
                new Pair<>("Mode", mode)
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
    public void draw(int x, int y) {
        if (!this.isEnabled()) {
            drawable = false;
            return;
        }
        drawable = true;
        if (mode.isCurrentMode("V")) {
            int tempY = 0;
            ItemStack[] armorInventory = mc.player.inventory.armorInventory.toArray(new ItemStack[0]);
            if (!armorInventory[3].isEmpty()) {
                draw(armorInventory[3], x, y);
                tempY += 16;
            }
            if (!armorInventory[2].isEmpty()) {
                draw(armorInventory[2], x, y + tempY);
                tempY += 16;
            }
            if (!armorInventory[1].isEmpty()) {
                draw(armorInventory[1], x, y + tempY);
                tempY += 16;
            }
            if (!armorInventory[0].isEmpty()) {
                draw(armorInventory[0], x, y + tempY);
                tempY += 16;
            }
            if (!mc.player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
                draw(mc.player.getHeldItem(EnumHand.MAIN_HAND), x, y + tempY);
                tempY += 16;
            }
            width = 16;
            height = tempY;
        } else {
            int tempX = 0;
            ItemStack[] armorInventory = mc.player.inventory.armorInventory.toArray(new ItemStack[0]);
            if (!armorInventory[3].isEmpty()) {
                draw(armorInventory[3], x, y);
                tempX += 16;
            }
            if (!armorInventory[2].isEmpty()) {
                draw(armorInventory[2], x + tempX, y);
                tempX += 16;
            }
            if (!armorInventory[1].isEmpty()) {
                draw(armorInventory[1], x + tempX, y);
                tempX += 16;
            }
            if (!armorInventory[0].isEmpty()) {
                draw(armorInventory[0], x + tempX, y);
                tempX += 16;
            }
            if (!mc.player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
                draw(mc.player.getHeldItem(EnumHand.MAIN_HAND), x + tempX, y);
                tempX += 16;
            }
            width = tempX;
            height = 16;
        }
    }

    @Override
    public boolean isDrawable() {
        return drawable;
    }

    public int draw(ItemStack item, int x, int y) {
        int temp = 0;
        if (item == null)
            return 0;
        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemIntoGUI(item, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, item, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.clear(256);
        GlStateManager.popMatrix();
        return temp;
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
        return "ArmorDisplay";
    }
}
