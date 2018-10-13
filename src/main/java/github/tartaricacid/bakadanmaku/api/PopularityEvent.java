package github.tartaricacid.bakadanmaku.api;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PopularityEvent extends Event {
    private int popularity; // 人气值

    /**
     * 获取人气值事件
     *
     * @param popularity 人气值
     */
    public PopularityEvent(int popularity) {
        this.popularity = popularity;
    }

    public int getPopularity() {
        return popularity;
    }
}
