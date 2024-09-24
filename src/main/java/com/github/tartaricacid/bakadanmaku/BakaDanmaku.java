package com.github.tartaricacid.bakadanmaku;

import com.github.tartaricacid.bakadanmaku.websocket.WebSocketClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ScheduledFuture;

@Mod(value = BakaDanmaku.MOD_ID, dist = Dist.CLIENT)
public class BakaDanmaku {
    public static final String MOD_ID = "bakadanmaku";
    public static final Logger LOGGER = LogManager.getLogger();
    public static ScheduledFuture<?> HEART_BEAT_TASK = null;
    public static WebSocketClient WEBSOCKET_CLIENT = null;
}
