package com.github.tartaricacid.bakadanmaku.event.post;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.MessageType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

public interface SendDanmakuEvent {
    Event<SendDanmakuEvent> EVENT = EventFactory.createArrayBacked(SendDanmakuEvent.class,
            listeners -> msg -> {
                for (SendDanmakuEvent listener : listeners) {
                    ActionResult result = listener.register(msg);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    static void register() {
        SendDanmakuEvent.EVENT.register(str -> {
            if (MinecraftClient.getInstance().world != null) {
                Registry<MessageType> registry = MinecraftClient.getInstance().world.getRegistryManager().get(Registry.MESSAGE_TYPE_KEY);
                MessageType messageType = registry.get(MessageType.CHAT);
                MinecraftClient.getInstance().inGameHud.onGameMessage(messageType, Text.literal(str));
            }
            return ActionResult.SUCCESS;
        });
    }

    ActionResult register(String msg);
}
