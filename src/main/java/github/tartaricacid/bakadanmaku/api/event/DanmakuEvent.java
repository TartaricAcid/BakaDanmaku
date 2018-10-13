package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class DanmakuEvent extends Event {
    private String user; // 发送弹幕的玩家名称
    private String msg; // 发送的弹幕

    /**
     * 弹幕发送事件
     *
     * @param user 发送者
     * @param msg  弹幕
     */
    public DanmakuEvent(String user, String msg) {
        this.user = user;
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public String getMsg() {
        return msg;
    }
}
