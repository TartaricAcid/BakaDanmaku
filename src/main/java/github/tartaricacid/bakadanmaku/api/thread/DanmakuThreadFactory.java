package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DanmakuThreadFactory {
    // 存储弹幕线程的 HashMap
    private static final HashMap<String, BaseDanmakuThread> danmakuThreads = new HashMap<>(); // 未启动的弹幕线程
    private static final HashMap<String, Thread> realDanmakuThreads = new HashMap<>(); // 正在运行的弹幕线程

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
        // 先获取当前正在运行的弹幕线程
        BaseDanmakuThread dmThread = getDanmakuThread(platform);

        // 如果正在运行的弹幕线程为空
        if (dmThread != null) {
            // 将弹幕开启的指示参数设定为 true
            dmThread.keepRunning = true;

            // 而后重新 new 线程，并启动线程
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
     * @param restart  是否再次启动该线程
     */
    public static void stopThread(String platform, boolean restart) {
        // 先获取当前正在运行的弹幕线程
        BaseDanmakuThread th = getDanmakuThread(platform);

        // 如果正在运行的弹幕线程为空
        if (th != null) {
            // 创建新线程，因为关闭弹幕线程是有阻塞的
            new Thread(() -> {
                // 先关闭线程标识符，借此关闭所有弹幕线程
                th.keepRunning = false;

                // 阻塞，等待弹幕线程关闭
                while (realDanmakuThreads.get(platform).isAlive()) ;

                // 在运行线程表中移除该线程
                realDanmakuThreads.remove(platform);

                // 清空线程
                th.clear();

                // 如果 restart，则再次启动线程
                if (restart) runThread(platform);
            }, "Stop" + platform + "DanmakuThread").start();
        }
    }

    /**
     * 停止指定平台的 DanmakuThread
     *
     * @param platform 平台名
     */
    public static void stopThread(String platform) {
        stopThread(platform, false);
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
        // 获得所有的 platforms
        String[] _platforms = BakaDanmakuConfig.livePlatform.platform.split(",");
        for (int i = 0; i < _platforms.length; i++) {
            _platforms[i] = _platforms[i].trim(); // 剔除行首行尾空格
        }

        // 获得所有的平台
        ArrayList<String> platforms = new ArrayList<>(Arrays.asList(_platforms));
        // 获得正在运行的弹幕线程
        ArrayList<String> running = getRunningDanmakuThread();

        // 创建一个 restart 数据，存入刚刚正在运行的弹幕线程列表
        ArrayList<String> restart = new ArrayList<>(running);
        // 获得两者的交集
        restart.retainAll(platforms);

        // 创建一个 toStop 数据，存入刚刚正在运行的弹幕线程列表
        ArrayList<String> toStop = new ArrayList<>(running);
        // 获得两者的差集
        toStop.removeAll(platforms);

        // 创建一个 toStart 数据，存入所有的平台列表
        ArrayList<String> toStart = new ArrayList<>(platforms);
        // 获得两者的差集
        toStart.removeAll((getRunningDanmakuThread()));

        // restart 部分，依次进行停止、并重启
        restart.forEach((platform) -> stopThread(platform, true));
        // toStop 部分，依次进行停止
        toStop.forEach(DanmakuThreadFactory::stopThread);
        // toStart 部分，依次进行开启
        toStart.forEach(DanmakuThreadFactory::runThread);
    }
}
