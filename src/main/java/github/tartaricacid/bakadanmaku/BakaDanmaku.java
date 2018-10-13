package github.tartaricacid.bakadanmaku;

import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import github.tartaricacid.bakadanmaku.handler.ChatMsgHandler;
import github.tartaricacid.bakadanmaku.handler.ScreenMsgHandler;
import github.tartaricacid.bakadanmaku.thread.BilibiliDanmakuThread;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
        DanmakuThreadFactory.setDanmakuThread("bilibili", new BilibiliDanmakuThread());

        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        if (BakaDanmakuConfig.room.enableChatMsgHandler) {
            MinecraftForge.EVENT_BUS.register(ChatMsgHandler.class);
        }

        if (BakaDanmakuConfig.room.enableScreenMsgHandler) {
            MinecraftForge.EVENT_BUS.register(ScreenMsgHandler.class);
        }
    }

    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            try {
                th = DanmakuThreadFactory.getDanmakuThread(BakaDanmakuConfig.general.platform);
                th.player = event.player;
                t = new Thread(th, BakaDanmakuConfig.general.platform + "DanmakuThread");
                t.start();
            } catch (Exception e) {
                //TODO: Handle exception here.
            }
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            th.keepRunning = false;
            th.player = null;
        }
    }
}
