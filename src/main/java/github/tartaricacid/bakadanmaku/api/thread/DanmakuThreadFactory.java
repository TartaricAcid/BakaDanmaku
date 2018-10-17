package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;

public class DanmakuThreadFactory {
    // 存储弹幕线程的 HashMap
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

        if (dmThread != null) {
            // 将指示参数设定为 true
            dmThread.keepRunning = true;

            // new 线程，并启动
            Thread threadToRun = new Thread(dmThread, platform + "DanmakuThread");
            threadToRun.start();

            // 在存储实际运行的 map 中存入这个线程
            realDanmakuThreads.put(platform, threadToRun);
        } else {
            // 发送错误信息
            BakaDanmaku.logger.error("平台 [" + platform + "] 不存在！请检查配置文件或已安装 Mod！");
            BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "弹幕姬错误：");
            BaseDanmakuThread.sendChatMessage(TextFormatting.RED + "平台 [" + platform + "] 不存在！请检查配置文件或已安装 Mod！");
        }
    }

    /**
     * 停止指定平台的 DanmakuThread
     *
     * @param platform 平台名
     */
    public static void stopThread(String platform) {
        BaseDanmakuThread th = getDanmakuThread(platform);

        if (th != null) {
            new Thread(() -> {
                // 关闭线程标识符
                th.keepRunning = false;

                // 阻塞，等待线程关闭，注意不要在主线程操作此方法
                while (realDanmakuThreads.get(platform).isAlive()) ;

                // 在运行线程表中移除该线程
                realDanmakuThreads.remove(platform);

                // 清空线程
                th.clear();
            }, "Stop" + platform + "DanmakuThread").start();
        }
    }

    /**
     * 停止现在正在运行的所有 DanmakuThread
     * stopThread 会卡住游戏，所以得在单独线程进行关闭操作
     */
    public static void stopAllThreads() {
        realDanmakuThreads.forEach((platform, thread) -> stopThread(platform));
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
     * 判断指定 platform 的 DanmakuThread 是否存在
     *
     * @param platform 平台名
     * @return DanmakuThread 存在与否
     */
    public static boolean isDanmakuThreadAvailable(String platform) {
        return danmakuThreads.containsKey(platform);
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
