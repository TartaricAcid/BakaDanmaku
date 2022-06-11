package com.github.tartaricacid.bakadanmaku.event.post;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface PlayerLoggedInEvent {
    Event<PlayerLoggedInEvent> EVENT = EventFactory.createArrayBacked(PlayerLoggedInEvent.class,
            listeners -> () -> {
                for (PlayerLoggedInEvent listener : listeners) {
                    ActionResult result = listener.onLoggedInEvent();
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult onLoggedInEvent();
}
