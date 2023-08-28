package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-26 16:52:13
 */
public class GuiShortcutEntry implements GuiListExtended.IGuiListEntry {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final GuiManageShortcut parent;
    private final Shortcut shortcut;
    public GuiShortcutEntry(GuiManageShortcut parent, Shortcut shortcut) {
        this.parent = parent;
        this.shortcut = shortcut;
    }

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
        mc.fontRenderer.drawString(shortcut.name(), x + 32 + 3, y + 1, 16777215);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        parent.selectedShortcut = shortcut;
        parent.delete.enabled = true;
        parent.edit.enabled = true;
        return true;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

    }
}
