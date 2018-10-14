package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class DanmakuThreadFactory {
    private static final HashMap<String, BaseDanmakuThread> danmakuThreads = new HashMap<>();
    private static final HashMap<String, Thread> realDanmakuThreads = new HashMap<>();

    /**
     * 向 danmakuThreads 中添加新的 DanmakuThread 类型
     * 通常在 Mod 的 init 阶段使用
     *
     * @param name   DanmakuThread 的名称
     * @param thread DanmakuThread 的实例
     */
    public static void setDanmakuThread(String name, BaseDanmakuThread thread) {
        danmakuThreads.put(name, thread);
    }

    /**
     * 获得指定平台的 DanmakuThread
     *
     * @param platform 平台名
     * @return BaseDanmakuThread 实例
     */
    public static BaseDanmakuThread getDanmakuThread(String platform) {
        return danmakuThreads.getOrDefault(platform, null);
    }

    /**
     * 获得当前正在运行的线程
     *
     * @return 当前正在运行的线程
     */
    public static ArrayList<String> getRunningDanmakuThread() {
        return new ArrayList<>(realDanmakuThreads.keySet());
    }

    /**
     * 启动指定平台的 DanmakuThread
     *
     * @param platform 平台名
     */
    public static void runThread(String platform) {
        BaseDanmakuThread dmThread = getDanmakuThread(platform);
        dmThread.keepRunning = true;

        Thread threadToRun = new Thread(dmThread, platform + "DanmakuThread");
        threadToRun.start();

        realDanmakuThreads.put(platform, threadToRun);
    }

    /**
     * 停止指定平台的 DanmakuThread
     *
     * @param platform 平台名
     */
    public static void stopThread(String platform) {
        BaseDanmakuThread th = getDanmakuThread(platform);

        if (platform != null) {
            th.keepRunning = false; // 关闭线程
            while (realDanmakuThreads.get(platform).isAlive()) ;
            th.clear(); // 清空线程
        }
    }

    /**
     * 停止现在正在运行的所有 DanmakuThread
     */
    public static void stopAllThreads() {
        realDanmakuThreads.forEach((platform, thread) -> {
            stopThread(platform);
        });
    }

    /**
     * 判断指定 platform 的 DanmakuThread 是否正在运行
     *
     * @param platform 平台名
     * @return 指定 platform 的 DanmakuThread 是否正在运行
     */
    public static boolean isThreadRunning(String platform) {
        return realDanmakuThreads.containsKey(platform);
    }

    /**
     * 重启所有线程 同样可以用于初始化时线程的启动
     */
    public static void restartThreads() {
        getRunningDanmakuThread().forEach(DanmakuThreadFactory::stopThread);
        for (String p : BakaDanmakuConfig.general.platform.split(",")) {
            runThread(p.trim());
        }
    }
}
