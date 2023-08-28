package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-27 17:31:38
 */
public record GuiActionEntry(Shortcut.Action action, GuiEditShortcut parent) implements GuiListExtended.IGuiListEntry {

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        Minecraft.getMinecraft().fontRenderer.drawString(action.type().name(), x + 4, y + 1, 16777215);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        parent.selectedAction = action;
        parent.edit.enabled = true;
        parent.delete.enabled = true;
        return true;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

    }
}
