package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;

import java.util.HashMap;

public class DanmakuThreadFactory {
    // 存储弹幕线程的 HashMap
    private static final HashMap<String, BaseDanmakuThread> danmakuThreads = new HashMap<>();

    // 正在运行的弹幕线程
    private static BaseDanmakuThread runningThread;

    /**
     * 存入弹幕线程
     *
     * @param name   弹幕线程所属平台名称
     * @param thread 具体需要存入的弹幕线程
     */
    public static void setDanmakuThread(String name, BaseDanmakuThread thread) {
        danmakuThreads.put(name, thread);
    }

    /**
     * 获取 HashMap 中存储的弹幕线程
     *
     * @param platform 弹幕线程所属平台名称
     * @return 对应的弹幕线程
     */
    public static BaseDanmakuThread getDanmakuThread(String platform) {
        return danmakuThreads.get(platform);
    }

    /**
     * 设置正在运行的弹幕线程
     *
     * @param thread 需要设置的正在运行的弹幕线程
     */
    public static void setRunningThread(BaseDanmakuThread thread) {
        runningThread = thread;
    }

    /**
     * 获取正在运行的弹幕线程
     *
     * @return 正在运行的弹幕线程
     */
    public static BaseDanmakuThread getRunningDanmakuThread() {
        return runningThread;
    }

    /**
     * 重载当前线程
     */
    public static void restartCurrentThread() {
        // 先获取当前运行的弹幕线程
        BaseDanmakuThread runningThread = getRunningDanmakuThread();

        // 从配置文件读取平台，获取到对应平台的弹幕线程
        BaseDanmakuThread dmThread = DanmakuThreadFactory.getDanmakuThread(BakaDanmakuConfig.general.platform);

        // 关闭正在运行的弹幕线程
        runningThread.keepRunning = false;

        while (BakaDanmaku.t.isAlive()) {
            // 阻塞一下，防止上一个线程还没关闭，下一个线程开好了
        }

        // 使用之前先清空
        dmThread.clear();

        // 重设线程开始标识符
        dmThread.keepRunning = true;

        // 重新 new 线程
        BakaDanmaku.t = new Thread(dmThread, BakaDanmakuConfig.general.platform + "DanmakuThread");

        // 启动
        BakaDanmaku.t.start();
    }
}
