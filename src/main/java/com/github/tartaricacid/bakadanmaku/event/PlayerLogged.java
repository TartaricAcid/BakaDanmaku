package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber
public class PlayerLogged {
    @SubscribeEvent
    public static void onEnterWorld(PlayerEvent.PlayerLoggedInEvent event) {
        OpenCloseDanmaku.openDanmaku();
    }

    @SubscribeEvent
    public static void onEnterWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        OpenCloseDanmaku.closeDanmaku();
    }
}