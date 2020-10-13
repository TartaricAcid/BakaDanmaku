package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
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
