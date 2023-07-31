package cn.floatingpoint.min.system.hyt.packet.impl;

import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import cn.floatingpoint.min.system.hyt.party.ButtonDecoder;
import cn.floatingpoint.min.system.hyt.party.Sender;
import cn.floatingpoint.min.system.ui.hyt.party.GuiHandleRequests;
import cn.floatingpoint.min.system.ui.hyt.party.GuiInit;
import cn.floatingpoint.min.system.ui.hyt.party.GuiInput;
import cn.floatingpoint.min.system.ui.hyt.party.GuiPartyManage;
import cn.floatingpoint.min.utils.client.ChatUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class VexViewPacket implements CustomPacket {
    @Override
    public String getChannel() {
        return "VexView";
    }

    @Override
    public void process(ByteBuf byteBuf) {
        ButtonDecoder buttonDecoder = new ButtonDecoder(byteBuf);
        if (buttonDecoder.invited) {
            TextComponentString textComponents = new TextComponentString("\247b[MIN] 收到来自\247a" + buttonDecoder.inviter + "\247b的邀请消息。\247e\247n(点此查看)");
            textComponents.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("\247e点击查看!")));
            textComponents.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/min hyt party handle " + buttonDecoder.getButton("同意").getId() + " " + buttonDecoder.getButton("拒绝").getId()));
            ChatUtil.printToChat(textComponents);
        } else if (buttonDecoder.sign) {
            Sender.clickButton(buttonDecoder.getButton("sign").getId());
        } else if (buttonDecoder.containsButton("手动输入")) {
            Sender.clickButton(buttonDecoder.getButton("手动输入").getId());
        } else if (buttonDecoder.containsButton("提交")) { // 提交
            mc.displayGuiScreen(new GuiInput(buttonDecoder.getElement(buttonDecoder.getButtonIndex("提交") - 1), buttonDecoder.getButton("提交")));
        } else if (buttonDecoder.list) {
            mc.displayGuiScreen(new GuiHandleRequests(buttonDecoder.requests));
        } else if (buttonDecoder.containsButtons("创建队伍", "申请入队")) {
            mc.displayGuiScreen(new GuiInit(buttonDecoder.getButton("创建队伍"), buttonDecoder.getButton("申请入队")));
        } else if (buttonDecoder.containsButtons("申请列表", "申请列表", "踢出队员", "离开队伍", "解散队伍")) {
            if (buttonDecoder.containsButton("邀请玩家")) {
                mc.displayGuiScreen(new GuiPartyManage(buttonDecoder.getButton("离开队伍"), buttonDecoder.getButton("解散队伍"), buttonDecoder.getButton("邀请玩家"), buttonDecoder.getButton("申请列表")));
            } else {
                mc.displayGuiScreen(new GuiPartyManage(buttonDecoder.getButton("离开队伍"), buttonDecoder.getButton("解散队伍"), null, null));
            }
        } else if (buttonDecoder.containsButton("离开队伍")) {
            mc.displayGuiScreen(new GuiPartyManage(buttonDecoder.getButton("离开队伍"), null, null, null));
        }
    }
}
