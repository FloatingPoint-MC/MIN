package cn.floatingpoint.min.system.ui.hyt.party;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.hyt.party.Sender;
import cn.floatingpoint.min.system.ui.components.ClickableButton;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 20:55:37
 */
public class GuiHandleInvitation extends GuiScreen {
    private ClickableButton accept;
    private ClickableButton deny;
    private final VexViewButton acceptButton;
    private final VexViewButton denyButton;

    public GuiHandleInvitation(VexViewButton acceptButton, VexViewButton denyButton) {
        this.acceptButton = acceptButton;
        this.denyButton = denyButton;
    }

    @Override
    public void initGui() {
        accept = new ClickableButton(width / 2, height / 2 - 20, 100, 20, acceptButton.getName()) {
            @Override
            public void clicked() {
                Sender.clickButton(acceptButton.getId());
            }
        };
        deny = new ClickableButton(width / 2, height / 2 + 20, 100, 20, denyButton.getName()) {
            @Override
            public void clicked() {
                Sender.clickButton(denyButton.getId());
            }
        };
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableBlend();
        RenderUtil.drawImage(new ResourceLocation("min/hyt/background.png"), width / 2 - 100, height / 2 - 81, 200, 162);
        Managers.fontManager.sourceHansSansCN_Regular_20.drawCenteredString("花雨庭组队系统", width / 2, height / 2 - 72, new Color(216, 216, 216).getRGB());
        accept.drawScreen();
        deny.drawScreen();
        GlStateManager.disableBlend();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        accept.mouseClicked(mouseX, mouseY, mouseButton);
        deny.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
