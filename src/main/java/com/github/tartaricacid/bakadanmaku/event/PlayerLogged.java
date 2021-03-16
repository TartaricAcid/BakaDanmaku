package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.event.post.PlayerLoggedInEvent;
import com.github.tartaricacid.bakadanmaku.event.post.PlayerLoggedOutEvent;
import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraft.util.ActionResult;

public class PlayerLogged {
    public static void onEnterWorld() {
        PlayerLoggedInEvent.EVENT.register(() -> {
            OpenCloseDanmaku.openDanmaku();
            return ActionResult.SUCCESS;
        });
    }

    public static void onLeaveWorld() {
        PlayerLoggedOutEvent.EVENT.register(() -> {
            OpenCloseDanmaku.closeDanmaku();
            return ActionResult.SUCCESS;
        });
    }
}
