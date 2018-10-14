package github.tartaricacid.bakadanmaku.handler;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class StartStopHandler {
    // 玩家进入服务器时，依据配置提供的平台，new 一个弹幕线程
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            // 读取配置，从弹幕工厂中获取得到对应弹幕线程
            BakaDanmaku.th = DanmakuThreadFactory.getDanmakuThread(BakaDanmakuConfig.general.platform);

            // 将这个线程设置为当前运行线程
            DanmakuThreadFactory.setRunningThread(BakaDanmaku.th);

            // 为当前这个线程设置玩家
            BakaDanmaku.th.player = event.player;

            // new 线程，并进行启动
            BakaDanmaku.t = new Thread(BakaDanmaku.th, BakaDanmakuConfig.general.platform + "DanmakuThread");
            BakaDanmaku.t.start();
        } catch (Exception e) {
            // TODO: 异常情况处理
        }
    }

    // 当玩家离开游戏时，关闭线程
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        // 将线程启动标识符设置为 false
        BakaDanmaku.th.keepRunning = false;

        // 玩家清空
        BakaDanmaku.th.player = null;
    }
}
