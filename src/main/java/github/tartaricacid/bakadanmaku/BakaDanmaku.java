package github.tartaricacid.bakadanmaku;

import github.tartaricacid.bakadanmaku.api.command.CommandBakaDM;
import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import github.tartaricacid.bakadanmaku.handler.ChatMsgHandler;
import github.tartaricacid.bakadanmaku.handler.ScreenMsgHandler;
import github.tartaricacid.bakadanmaku.thread.BilibiliDanmakuThread;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BakaDanmaku.MOD_ID, name = BakaDanmaku.MOD_NAME, acceptedMinecraftVersions = "[1.12]", version = BakaDanmaku.VERSION, clientSideOnly = true)
public class BakaDanmaku {
    public static final String MOD_ID = "baka_danmaku";
    public static final String MOD_NAME = "Baka Danmaku";
    public static final String VERSION = "1.0.0";

    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static Thread t;
    public static BaseDanmakuThread th;

    @Mod.Instance(MOD_ID)
    public static BakaDanmaku INSTANCE;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // 装载各大平台的弹幕线程
        DanmakuThreadFactory.setDanmakuThread("bilibili", new BilibiliDanmakuThread());

        // 注册事件处理器
        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        // 当配置开启时，注册聊天事件处理器
        if (BakaDanmakuConfig.bilibiliRoom.enableChatMsgHandler) {
            MinecraftForge.EVENT_BUS.register(ChatMsgHandler.class);
        }

        // 当配置开启时，注册屏幕信息事件处理器
        if (BakaDanmakuConfig.bilibiliRoom.enableScreenMsgHandler) {
            MinecraftForge.EVENT_BUS.register(ScreenMsgHandler.class);
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // 注册本模组指令
        event.registerServerCommand(new CommandBakaDM());
    }

    public static class EventHandler {
        // 玩家进入服务器时，依据配置提供的平台，new 一个弹幕线程
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            try {
                // 读取配置，从弹幕工厂中获取得到对应弹幕线程
                th = DanmakuThreadFactory.getDanmakuThread(BakaDanmakuConfig.general.platform);

                // 将这个线程设置为当前运行线程
                DanmakuThreadFactory.setRunningThread(th);

                // 为当前这个线程设置玩家
                th.player = event.player;

                // new 线程，并进行启动
                t = new Thread(th, BakaDanmakuConfig.general.platform + "DanmakuThread");
                t.start();
            } catch (Exception e) {
                // TODO: 异常情况处理
            }
        }

        // 当玩家离开游戏时，关闭线程
        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            // 将线程启动标识符设置为 false
            th.keepRunning = false;

            // 玩家清空
            th.player = null;
        }
    }
}
