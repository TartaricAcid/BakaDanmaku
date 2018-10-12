package github.tartaricacid.bakadanmaku.event.event;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = BakaDanmaku.MOD_ID)
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
