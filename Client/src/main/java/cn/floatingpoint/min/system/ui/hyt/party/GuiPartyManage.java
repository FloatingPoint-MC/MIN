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

public class GuiPartyManage extends GuiScreen {
    private ClickableButton leave;
    private ClickableButton disband;
    private ClickableButton invite;
    private final VexViewButton leaveButton;
    private final VexViewButton disbandButton;
    private final VexViewButton inviteButton;

    public GuiPartyManage(VexViewButton leaveButton, VexViewButton disbandButton, VexViewButton inviteButton) {
        this.leaveButton = leaveButton;
        this.disbandButton = disbandButton;
        this.inviteButton = inviteButton;
        invite = null;
    }

    @Override
    public void initGui() {
        if (inviteButton != null) {
            leave = new ClickableButton(width / 2, height / 2 - 40, 100, 20, leaveButton.getName()) {
                @Override
                public void clicked() {
                    Sender.clickButton(leaveButton.getId());
                }
            };
            disband = new ClickableButton(width / 2, height / 2, 100, 20, disbandButton.getName()) {
                @Override
                public void clicked() {
                    Sender.clickButton(disbandButton.getId());
                }
            };
            invite = new ClickableButton(width / 2, height / 2 + 40, 100, 20, inviteButton.getName()) {
                @Override
                public void clicked() {
                    Sender.clickButton(inviteButton.getId());
                }
            };
        } else {
            leave = new ClickableButton(width / 2, height / 2 - 20, 100, 20, leaveButton.getName()) {
                @Override
                public void clicked() {
                    Sender.clickButton(leaveButton.getId());
                }
            };
            disband = new ClickableButton(width / 2, height / 2 + 20, 100, 20, disbandButton.getName()) {
                @Override
                public void clicked() {
                    Sender.clickButton(disbandButton.getId());
                }
            };
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.enableBlend();
        RenderUtil.drawImage(new ResourceLocation("min/hyt/background.png"), width / 2 - 100, height / 2 - 81, 200, 162);
        Managers.fontManager.sourceHansSansCN_Regular_20.drawCenteredString("花雨庭组队系统", width / 2, height / 2 - 72, new Color(216, 216, 216).getRGB());
        leave.drawScreen();
        disband.drawScreen();
        if (invite != null) {
            invite.drawScreen();
        }
        GlStateManager.disableBlend();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        leave.mouseClicked(mouseX, mouseY, mouseButton);
        disband.mouseClicked(mouseX, mouseY, mouseButton);
        if (invite != null) {
            invite.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
