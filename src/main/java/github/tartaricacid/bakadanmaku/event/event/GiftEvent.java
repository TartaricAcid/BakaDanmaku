package github.tartaricacid.bakadanmaku.event.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class GiftEvent extends Event {
    private String giftName; // 礼物名称
    private int num; // 礼物数量
    private String user; // 送礼玩家名称
    private String face; // 送礼玩家头像的 URL

    /**
     * 发送礼物事件
     *
     * @param giftName 礼物名称
     * @param num      礼物数量
     * @param user     用户名
     * @param face     用户头像 url
     */
    public GiftEvent(String giftName, int num, String user, String face) {
        this.giftName = giftName;
        this.num = num;
        this.user = user;
        this.face = face;
    }

    public String getGiftName() {
        return giftName;
    }

    public int getNum() {
        return num;
    }

    public String getUser() {
        return user;
    }

    public String getFace() {
        return face;
    }
}
