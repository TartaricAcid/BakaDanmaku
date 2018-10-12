package github.tartaricacid.bakadanmaku.event.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class WelcomeGuardEvent extends Event {
    private String user; // 房管加入房间

    /**
     * 房管加入事件
     *
     * @param user 房管用户名
     */
    public WelcomeGuardEvent(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
