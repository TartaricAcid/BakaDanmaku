package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class GiftEvent extends BaseDanmakuEvent {
    private String giftName; // 礼物名称
    private int num; // 礼物数量
    private String user; // 赠送人

    /**
     * 发送礼物事件
     *
     * @param platform 平台名
     * @param giftName 礼物名称
     * @param num      礼物数量
     * @param user     赠送人
     */
    public GiftEvent(String platform, String giftName, int num, String user) {
        super(platform);
        this.giftName = giftName;
        this.num = num;
        this.user = user;
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
}
