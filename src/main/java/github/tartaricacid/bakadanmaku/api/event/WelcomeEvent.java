package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class WelcomeEvent extends BaseDanmakuEvent {
    private String user; // 老爷加入房间，我很想吐槽这个“老爷”，这不是旧社会用语么？

    /**
     * 老爷加入房间的事件
     *
     * @param user 用户名
     */
    public WelcomeEvent(String platform, String user) {
        super(platform);
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
