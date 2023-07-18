package cn.floatingpoint.min.system.hyt.packet.impl;

import cn.floatingpoint.min.system.hyt.packet.CustomPacket;
import cn.floatingpoint.min.system.hyt.party.ButtonDecoder;
import cn.floatingpoint.min.system.hyt.party.Sender;
import cn.floatingpoint.min.system.ui.hyt.party.GuiInit;
import cn.floatingpoint.min.system.ui.hyt.party.GuiInput;
import cn.floatingpoint.min.system.ui.hyt.party.GuiPartyManage;
import io.netty.buffer.ByteBuf;

public class VexViewPacket implements CustomPacket {
    @Override
    public String getChannel() {
        return "VexView";
    }

    @Override
    public void process(ByteBuf byteBuf) {
        ButtonDecoder buttonDecoder = new ButtonDecoder(byteBuf);
        if (buttonDecoder.containsButtons("创建队伍", "申请入队")) {
            mc.displayGuiScreen(new GuiInit(buttonDecoder.getButton("创建队伍"), buttonDecoder.getButton("申请入队")));
        } else if (buttonDecoder.containsButtons("申请列表", "踢出队员", "离开队伍", "解散队伍")) {
            if (buttonDecoder.containsButton("邀请玩家")) {
                mc.displayGuiScreen(new GuiPartyManage(buttonDecoder.getButton("离开队伍"), buttonDecoder.getButton("解散队伍"), buttonDecoder.getButton("邀请玩家")));
            } else {
                mc.displayGuiScreen(new GuiPartyManage(buttonDecoder.getButton("离开队伍"), buttonDecoder.getButton("解散队伍"), null));
            }
        } else if (buttonDecoder.containsButton("手动输入")) {
            Sender.clickButton(buttonDecoder.getButton("手动输入").getId());
        } else if (buttonDecoder.containsButton("提交")) { // 提交
            mc.displayGuiScreen(new GuiInput(buttonDecoder.getElement(buttonDecoder.getButtonIndex("提交") - 1), buttonDecoder.getButton("提交")));
        }
    }
}
