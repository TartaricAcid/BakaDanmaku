package github.tartaricacid.bakadanmaku.event;

import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class GiftEvent {
    private String giftName; // 礼物名称
    private int num; // 礼物数量
    private String user; // 送礼玩家名称
    private String face; // 送礼玩家头像的 URL

    public GiftEvent(String giftName, int num, String user, String face) {
        this.giftName = giftName;
        this.num = num;
        this.user = user;
        this.face = face;

        chatShow();
    }

    private void chatShow() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            player.sendMessage(new TextComponentString(String.format(BakaDanmakuConfig.room.giftStyle, user, giftName, num)));
        }
    }
}
