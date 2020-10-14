package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerLogged {
    public PlayerLogged() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEnterWorld(PlayerEvent.PlayerLoggedInEvent event) {
        OpenCloseDanmaku.openDanmaku();
    }

    @SubscribeEvent
    public void onEnterWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        OpenCloseDanmaku.closeDanmaku();
    }
}
