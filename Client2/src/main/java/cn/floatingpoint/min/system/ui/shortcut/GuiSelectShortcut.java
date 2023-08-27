package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-25 22:38:48
 */
public class GuiSelectShortcut extends GuiListExtended {
    private final ArrayList<GuiShortcutEntry> entries;
    private final GuiManageShortcut parent;

    public GuiSelectShortcut(GuiManageShortcut parent, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.parent = parent;
        entries = new ArrayList<>();
        for (Shortcut shortcut : Managers.clientManager.shortcuts) {
            entries.add(new GuiShortcutEntry(parent, shortcut));
        }
    }

    @Nonnull
    @Override
    public IGuiListEntry getListEntry(int index) {
        return entries.get(index);
    }

    @Override
    protected int getSize() {
        return this.entries.size();
    }
}
