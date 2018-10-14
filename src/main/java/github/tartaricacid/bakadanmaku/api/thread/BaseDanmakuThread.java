package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public abstract class BaseDanmakuThread implements Runnable {
    protected int retryCounter = BakaDanmakuConfig.network.retry;

    // 特殊的修饰符 volatile，用来标定是否进行连接
    public volatile boolean keepRunning = true;

    @Override
    public void run() {
        if (!preRunCheck()) return;
        doRun();
    }

    public boolean preRunCheck() {
        if (Minecraft.getMinecraft().player != null)
            BakaDanmaku.player = Minecraft.getMinecraft().player;
        return true;
    }

    public abstract void doRun();

    public abstract void clear();

    public static void sendChatMessage(String text) {
        if (BakaDanmaku.player != null)
            BakaDanmaku.player.sendMessage(new TextComponentString(text));
    }
}
