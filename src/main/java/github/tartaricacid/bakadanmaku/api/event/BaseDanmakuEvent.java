package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 所有自定义事件的抽象类
 * 构造方法只传入了一个平台的字符串
 */
public abstract class BaseDanmakuEvent extends Event {
    private String platform;

    public BaseDanmakuEvent(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }
}
