package cn.floatingpoint.min.system.ui.hyt.party;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.hyt.party.Sender;
import cn.floatingpoint.min.system.ui.components.ClickableButton;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

public class GuiInit extends GuiScreen {
    private ClickableButton create;
    private ClickableButton join;
    private final VexViewButton createButton;
    private final VexViewButton joinButton;

    public GuiInit(VexViewButton createButton, VexViewButton joinButton) {
        this.createButton = createButton;
        this.joinButton = joinButton;
    }

    @Override
    public void initGui() {
        create = new ClickableButton(width / 2, height / 2 - 20, 100, 20, createButton.getName()) {
            @Override
            public void clicked() {
                Sender.clickButton(createButton.getId());
            }
        };
        join = new ClickableButton(width / 2, height / 2 + 20, 100, 20, joinButton.getName()) {
            @Override
            public void clicked() {
                Sender.clickButton(joinButton.getId());
            }
        };
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawImage(new ResourceLocation("min/hyt/background.png"), width / 2 - 100, height / 2 - 81, 200, 162);
        Managers.fontManager.sourceHansSansCN_Regular_20.drawCenteredString("花雨庭组队系统", width / 2, height / 2 - 72, new Color(216, 216, 216).getRGB());
        create.drawScreen();
        join.drawScreen();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        create.mouseClicked(mouseX, mouseY, mouseButton);
        join.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
