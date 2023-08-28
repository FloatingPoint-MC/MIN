package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-27 17:31:32
 */
public class GuiSelectAction extends GuiListExtended {
    private final ArrayList<GuiActionEntry> actions = new ArrayList<>();

    public GuiSelectAction(Shortcut shortcut, GuiEditShortcut parent, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        if (shortcut != null) {
            shortcut.actions().forEach(action -> actions.add(new GuiActionEntry(action, parent)));
        }
    }

    @Nonnull
    @Override
    public IGuiListEntry getListEntry(int index) {
        return actions.get(index);
    }

    @Override
    protected int getSize() {
        return actions.size();
    }

    public ArrayList<Shortcut.Action> getActions() {
        ArrayList<Shortcut.Action> actions = new ArrayList<>();
        this.actions.forEach(guiActionEntry -> actions.add(guiActionEntry.action()));
        return actions;
    }
}
