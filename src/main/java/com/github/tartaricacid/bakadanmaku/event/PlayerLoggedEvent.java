package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.event.post.PlayerLoggedInEvent;
import com.github.tartaricacid.bakadanmaku.event.post.PlayerLoggedOutEvent;
import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraft.util.ActionResult;

public class PlayerLoggedEvent {
    public static void registerEnterWorld() {
        PlayerLoggedInEvent.EVENT.register(() -> {
            OpenCloseDanmaku.openDanmaku();
            return ActionResult.SUCCESS;
        });
    }

    public static void registerLeaveWorld() {
        PlayerLoggedOutEvent.EVENT.register(() -> {
            OpenCloseDanmaku.closeDanmaku();
            return ActionResult.SUCCESS;
        });
    }
}
