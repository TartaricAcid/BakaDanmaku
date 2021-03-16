package com.github.tartaricacid.bakadanmaku;

import com.github.tartaricacid.bakadanmaku.event.PlayerLogged;
import com.github.tartaricacid.bakadanmaku.event.post.SendDanmakuEvent;
import com.github.tartaricacid.bakadanmaku.input.ConfigKey;
import com.github.tartaricacid.bakadanmaku.websocket.WebSocketClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledFuture;

public class BakaDanmaku implements ClientModInitializer {
    public static final String MOD_ID = "bakadanmaku";
    public static final Logger LOGGER = LogManager.getLogger();
    public static ScheduledFuture<?> HEART_BEAT_TASK = null;
    public static WebSocketClient WEBSOCKET_CLIENT = null;

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(ConfigKey.CONFIG_KEY);
        ConfigKey.onKeyboardInput();
        SendDanmakuEvent.onSendDanmaku();
        PlayerLogged.onEnterWorld();
        PlayerLogged.onLeaveWorld();
    }
}
