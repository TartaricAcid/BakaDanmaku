package com.github.tartaricacid.bakadanmaku;

import com.github.tartaricacid.bakadanmaku.command.MainCommand;
import com.github.tartaricacid.bakadanmaku.event.PlayerLogged;
import com.github.tartaricacid.bakadanmaku.event.SendDanmaku;
import com.github.tartaricacid.bakadanmaku.websocket.WebSocketClient;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledFuture;

@Mod(modid = BakaDanmaku.MOD_ID, name = BakaDanmaku.NAME, version = BakaDanmaku.VERSION)
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
        // 注册事件
        FMLCommonHandler.instance().bus().register(new PlayerLogged());
        MinecraftForge.EVENT_BUS.register(new SendDanmaku());
    }
}
