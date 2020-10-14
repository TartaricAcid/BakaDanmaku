package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class PlayerLogged {
    @SubscribeEvent
    public void onEnterWorld(PlayerEvent.PlayerLoggedInEvent event) {
        OpenCloseDanmaku.openDanmaku();
    }

    @SubscribeEvent
    public void onEnterWorld(PlayerEvent.PlayerLoggedOutEvent event) {
        OpenCloseDanmaku.closeDanmaku();
    }
}
