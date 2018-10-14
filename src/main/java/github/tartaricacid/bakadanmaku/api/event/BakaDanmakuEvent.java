package github.tartaricacid.bakadanmaku.api.event;

import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class BakaDanmakuEvent extends Event {
    private String platform;

    public BakaDanmakuEvent(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public BakaDanmakuEvent() {
        this("UNKNOWN");
    }
}
