package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.gui.GuiListExtended;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-27 17:31:38
 */
public class GuiActionEntry implements GuiListExtended.IGuiListEntry {
    private final Shortcut.Action action;

    public GuiActionEntry(Shortcut.Action action) {
        this.action = action;
    }

    @Override
    public void updatePosition(int slotIndex, int x, int y, float partialTicks) {

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {

    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
        return false;
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {

    }

    public Shortcut.Action getAction() {
        return action;
    }
}
