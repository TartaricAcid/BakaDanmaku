package com.github.tartaricacid.bakadanmaku.event.post;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface PlayerLoggedOutEvent {
    Event<PlayerLoggedOutEvent> EVENT = EventFactory.createArrayBacked(PlayerLoggedOutEvent.class,
            listeners -> () -> {
                for (PlayerLoggedOutEvent listener : listeners) {
                    ActionResult result = listener.onLoggedOutEvent();
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult onLoggedOutEvent();
}
