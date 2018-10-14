package github.tartaricacid.bakadanmaku.api.event;

import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class DanmakuEvent extends BakaDanmakuEvent {
    private Danmaku danmaku;

    public DanmakuEvent(Danmaku danmaku) {
        super();
        this.danmaku = danmaku;
    }

    public DanmakuEvent(Danmaku danmaku, String platform) {
        super(platform);
        this.danmaku = danmaku;
    }

    public Danmaku getDanmaku() {
        return danmaku;
    }

    public static class Danmaku {
        /**
         * 发送弹幕的用户名称
         */
        private String user;

        /**
         * 发送弹幕的内容
         */
        private String msg;

        public Danmaku(String user, String msg) {
            this.user = user;
            this.msg = msg;
        }

        public static Danmaku builder(String user, String msg) {
            return new Danmaku(user, msg);
        }

        public String getUser() {
            return user;
        }

        public String getMsg() {
            return msg;
        }
    }
}
