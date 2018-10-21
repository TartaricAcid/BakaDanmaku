package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class DanmakuEvent extends BaseDanmakuEvent {
    private String user; // 发送弹幕的用户名称
    private String msg; // 发送弹幕的内容

    /**
     * 弹幕事件
     *
     * @param platform 平台名
     * @param user     发送弹幕的用户名称
     * @param msg      发送弹幕的内容
     */
    public DanmakuEvent(String platform, String user, String msg) {
        super(platform);
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
