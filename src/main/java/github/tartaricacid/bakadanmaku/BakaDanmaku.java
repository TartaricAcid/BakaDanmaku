package github.tartaricacid.bakadanmaku;

import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import github.tartaricacid.bakadanmaku.handler.ChatMsgHandler;
import github.tartaricacid.bakadanmaku.handler.ScreenMsgHandler;
import github.tartaricacid.bakadanmaku.network.DanmakuThread;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BakaDanmaku.MOD_ID, name = BakaDanmaku.MOD_NAME, acceptedMinecraftVersions = "[1.12]", version = BakaDanmaku.VERSION, clientSideOnly = true)
public class BakaDanmaku {
    public static final String MOD_ID = "baka_danmaku";
    public static final String MOD_NAME = "Baka Danmaku";
    public static final String VERSION = "1.0.0";

    public static final Logger logger = LogManager.getLogger(MOD_ID);

    public static Thread t;

    @Mod.Instance(MOD_ID)
    public static BakaDanmaku INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (BakaDanmakuConfig.room.enableChatMsgHandler) {
            MinecraftForge.EVENT_BUS.register(ChatMsgHandler.class);
        }

        if (BakaDanmakuConfig.room.enableScreenMsgHandler) {
            MinecraftForge.EVENT_BUS.register(ScreenMsgHandler.class);
        }
    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
    }

    @Mod.EventHandler
    public void onLoad(FMLInitializationEvent event) {
        t = new Thread(new DanmakuThread(), "BakaDanmakuThread");
        t.start();
    }
}
