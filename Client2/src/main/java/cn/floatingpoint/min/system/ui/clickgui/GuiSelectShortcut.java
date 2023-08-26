package cn.floatingpoint.min.system.ui.clickgui;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import java.util.ArrayList;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-25 22:38:48
 */
public class GuiSelectShortcut extends GuiListExtended {
    private final ArrayList<GuiShortcutEntry> entries;
    public GuiSelectShortcut(int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        entries = new ArrayList<>();
        for (Shortcut shortcut : Managers.clientManager.shortcuts) {
            entries.add(new GuiShortcutEntry(shortcut));
        }
    }

    @Override
    public IGuiListEntry getListEntry(int index) {
        return entries.get(index);
    }

    @Override
    protected int getSize() {
        return 0;
    }
}
