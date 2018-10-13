package github.tartaricacid.bakadanmaku.api;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PopularityEvent extends Event {
    private int num; // 人气值

    /**
     * 获取人气值事件
     *
     * @param num 人气值
     */
    public PopularityEvent(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
