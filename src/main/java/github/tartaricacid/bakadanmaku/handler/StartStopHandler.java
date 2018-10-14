package github.tartaricacid.bakadanmaku.handler;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class StartStopHandler {
    // 玩家进入服务器时，依据配置提供的平台，启动对于的弹幕线程
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        BakaDanmaku.player = event.player;
        DanmakuThreadFactory.restartThreads();
    }

    // 当玩家离开游戏时，停止所有线程
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        BakaDanmaku.player = null;
        DanmakuThreadFactory.stopAllThreads();
    }
}
