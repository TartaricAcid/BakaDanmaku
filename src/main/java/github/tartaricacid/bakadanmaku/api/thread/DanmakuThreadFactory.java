package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;

public class DanmakuThreadFactory {
    private static final HashMap<String, BaseDanmakuThread> danmakuThreads = new HashMap<>();
    private static BaseDanmakuThread runningThread;

    public static void setDanmakuThread(String name, BaseDanmakuThread thread) {
        danmakuThreads.put(name, thread);
    }

    public static BaseDanmakuThread getDanmakuThread(String platform) {
        return danmakuThreads.get(platform);
    }

    public static void setRunningThread(BaseDanmakuThread thread) {
        runningThread = thread;
    }

    public static BaseDanmakuThread getRunningDanmakuThread() {
        return runningThread;
    }

    public static void restartCurrentThread() {
        BaseDanmakuThread runningThread = getRunningDanmakuThread();
        BaseDanmakuThread dmThread = DanmakuThreadFactory.getDanmakuThread(BakaDanmakuConfig.general.platform);
        runningThread.keepRunning = false; // 关闭线程

        while (BakaDanmaku.t.isAlive()) {
            // 阻塞一下，防止上一个线程还没关闭，下一个线程开好了
        }

        dmThread.clear(); // 使用之前先清空
        dmThread.keepRunning = true; // 开启线程
        BakaDanmaku.t = new Thread(dmThread, BakaDanmakuConfig.general.platform + "DanmakuThread"); // 重新 new 线程
        BakaDanmaku.t.start(); // 启动
    }
}
