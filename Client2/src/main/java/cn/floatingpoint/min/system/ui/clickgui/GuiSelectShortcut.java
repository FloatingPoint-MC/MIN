package cn.floatingpoint.min.system.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-25 22:38:48
 */
public class GuiSelectShortcut extends GuiListExtended {
    public GuiSelectShortcut(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return null;
    }

    @Override
    protected int getSize() {
        return 0;
    }
}
