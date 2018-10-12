package github.tartaricacid.bakadanmaku.event.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class SysMegEvent extends Event {
    private String msg; // 系统信息

    /**
     * 系统信息事件
     *
     * @param msg 系统信息
     */
    public SysMegEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
