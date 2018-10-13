package github.tartaricacid.bakadanmaku.api;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
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
