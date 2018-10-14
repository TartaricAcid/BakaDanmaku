package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class GiftEvent extends BakaDanmakuEvent {
    private Gift gift;

    /**
     * 发送礼物事件
     *
     * @param gift 礼物对象
     */
    public GiftEvent(Gift gift) {
        super();
        this.gift = gift;
    }

    public GiftEvent(Gift gift, String platform) {
        super(platform);
        this.gift = gift;
    }

    public Gift getGift() {
        return gift;
    }

    public static class Gift {

        /**
         * 礼物名称
         */
        private String giftName;

        /**
         * 礼物数量
         */
        private int num;

        /**
         * 赠送人
         */
        private String user;

        /**
         * 赠送者头像的 URL
         */
        private String face;

        public Gift(String giftName, int num, String user, String face) {
            this.giftName = giftName;
            this.num = num;
            this.user = user;
            this.face = face;
        }

        /**
         * 构建一个 Gift 对象
         *
         * @param giftName 礼物名称
         * @param num      礼物数量
         * @param user     赠送人
         * @param face     赠送者头像的 URL
         */
        public static Gift builder(String giftName, int num, String user, String face) {
            return new Gift(giftName, num, user, face);
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
}
