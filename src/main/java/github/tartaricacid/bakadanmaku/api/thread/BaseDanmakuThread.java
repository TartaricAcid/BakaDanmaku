package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public abstract class BaseDanmakuThread implements Runnable {
    protected int retryCounter = BakaDanmakuConfig.network.retry; // 配置文件，重试次数

    public volatile boolean keepRunning = true; // 特殊的修饰符 volatile，用来标定是否进行连接

    @Override
    public void run() {
        // 预检查不通过，不执行
        if (!preRunCheck()) return;

        // 执行主体
        doRun();
    }

    /**
     * 预检查，检查玩家是否为空，尝试获取玩家
     *
     * @return 检查是否通过
     */
    public boolean preRunCheck() {
        if (Minecraft.getMinecraft().player != null)
            BakaDanmaku.player = Minecraft.getMinecraft().player;
        return true;
    }

    /**
     * 弹幕线程运行主体
     */
    public abstract void doRun();

    /**
     * 重载时的清除方法，用来清除某些可能需要销毁的数据
     */
    public abstract void clear();

    /**
     * 发送信息，进行游戏内提醒
     *
     * @param text 需要发送的信息
     */
    public static void sendChatMessage(String text) {
        if (BakaDanmaku.player != null)
            BakaDanmaku.player.sendMessage(new TextComponentString(text));
    }
}
