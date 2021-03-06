package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
