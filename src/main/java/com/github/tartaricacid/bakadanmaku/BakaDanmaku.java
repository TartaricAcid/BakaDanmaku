package com.github.tartaricacid.bakadanmaku;

import com.github.tartaricacid.bakadanmaku.command.MainCommand;
import com.github.tartaricacid.bakadanmaku.websocket.WebSocketClient;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledFuture;

@Mod(modid = BakaDanmaku.MOD_ID, name = BakaDanmaku.NAME, version = BakaDanmaku.VERSION, clientSideOnly = true)
public class BakaDanmaku {
    public static final String MOD_ID = "bakadanmaku";
    public static final String NAME = "Baka Danmaku";
    public static final String VERSION = "2.0.0";
    public static final Logger LOGGER = LogManager.getLogger();
    public static ScheduledFuture<?> HEART_BEAT_TASK = null;
    public static WebSocketClient WEBSOCKET_CLIENT = null;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new MainCommand());
    }
}
