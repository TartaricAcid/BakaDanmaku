package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class SysMsgEvent extends Event {
    private String msg; // 系统信息

    /**
     * 系统信息事件
     *
     * @param msg 系统信息
     */
    public SysMsgEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
