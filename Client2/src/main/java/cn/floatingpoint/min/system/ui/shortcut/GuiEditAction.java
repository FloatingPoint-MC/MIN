package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-27 17:51:00
 */
public class GuiEditAction extends GuiScreen {
    private Shortcut.Action action;
    private final GuiEditShortcut parent;
    private GuiTextField context;
    private final String title;

    public GuiEditAction(Shortcut.Action action, GuiEditShortcut parent) {
        this.action = action;
        this.parent = parent;
        if (action == null) {
            title = Managers.i18NManager.getTranslation("shortcut.action.create");
        } else {
            title = Managers.i18NManager.getTranslation("shortcut.action.change");
        }
    }

    @Override
    public void initGui() {
        context = new GuiTextField(0, mc.fontRenderer, this.width / 2 - 100, 80, 200, 20);
        if (action == null) {
            action = new Shortcut.Action(Shortcut.Action.Type.SEND_MESSAGE, "");
            context.setFocused(true);
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.add")));
        } else {
            if (action.type() == Shortcut.Action.Type.SEND_MESSAGE) {
                context.setText(action.context());
            } else {
                context.setEnabled(false);
            }
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.save")));
        }
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.back")));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 96 + mc.fontRenderer.getStringWidth(Managers.i18NManager.getTranslation("shortcut.action.type")), 41, 98, 20, Managers.i18NManager.getTranslation("shortcut.action." + action.type().name())));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        this.drawCenteredString(this.fontRenderer, title, this.width / 2, 20, -1);
        mc.fontRenderer.drawString(Managers.i18NManager.getTranslation("shortcut.action.type"), this.width / 2 - 100, 46, -1);
        mc.fontRenderer.drawString(Managers.i18NManager.getTranslation("shortcut.action.context"), this.width / 2 - 100, 66, -1);
        context.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                parent.shortcut.actions().add(action);
                mc.displayGuiScreen(parent);
            } else if (button.id == 2) {
                mc.displayGuiScreen(parent);
            } else if (button.id == 3) {
                action = new Shortcut.Action(
                        switch (action.type()) {
                            case SEND_MESSAGE -> Shortcut.Action.Type.QUIT_NETWORK;
                            case QUIT_NETWORK -> Shortcut.Action.Type.SEND_MESSAGE;
                        }, action.context());
                context.setEnabled(action.type() == Shortcut.Action.Type.SEND_MESSAGE);
                button.displayString = Managers.i18NManager.getTranslation("shortcut.action." + action.type());
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        context.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.displayGuiScreen(parent);
        } else {
            context.textboxKeyTyped(typedChar, keyCode);
            action = new Shortcut.Action(action.type(), context.getText());
        }
    }

    @Override
    public void updateScreen() {
        if (action.type() == Shortcut.Action.Type.SEND_MESSAGE) {
            context.updateCursorCounter();
        } else {
            context.setFocused(false);
        }
    }
}
