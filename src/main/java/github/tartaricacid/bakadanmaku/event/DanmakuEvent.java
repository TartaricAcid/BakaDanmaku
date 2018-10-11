package github.tartaricacid.bakadanmaku.event;

import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class DanmakuEvent {
    private String user; // 发送弹幕的玩家名称
    private String msg; // 发送的弹幕

    public DanmakuEvent(String user, String msg) {
        this.user = user;
        this.msg = msg;

        chatShow();
    }

    private void chatShow() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            player.sendMessage(new TextComponentString(String.format(BakaDanmakuConfig.room.danmakuStyle, user, msg)));
        }
    }
}
