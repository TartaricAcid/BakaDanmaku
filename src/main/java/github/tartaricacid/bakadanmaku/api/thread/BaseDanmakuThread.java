package github.tartaricacid.bakadanmaku.api.thread;

import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public abstract class BaseDanmakuThread implements Runnable {
    protected int retryCounter = BakaDanmakuConfig.network.retry;

    // 特殊的修饰符 volatile，用来标定是否进行连接
    public volatile boolean keepRunning = true;
    public volatile EntityPlayer player = null;

    @Override
    public void run() {
        if (!preRunCheck()) return;
        doRun();
    }

    public boolean preRunCheck() {
        if (Minecraft.getMinecraft().player != null)
            player = Minecraft.getMinecraft().player;
        return true;
    }

    public abstract void doRun();

    public abstract void clear();

    protected void sendChatMessage(String text) {
        if (player != null)
            player.sendMessage(new TextComponentString(text));
    }
}
