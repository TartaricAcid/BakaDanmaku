package github.tartaricacid.bakadanmaku.api.event;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PopularityEvent extends BaseDanmakuEvent {
    private int popularity; // 人气值

    /**
     * 获取人气值事件
     *
     * @param popularity 人气值
     */
    public PopularityEvent(String platform, int popularity) {
        super(platform);
        this.popularity = popularity;
    }

    public int getPopularity() {
        return popularity;
    }
}
