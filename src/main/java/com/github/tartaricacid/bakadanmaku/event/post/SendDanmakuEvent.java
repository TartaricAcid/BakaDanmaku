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
                MinecraftClient client = MinecraftClient.getInstance();
                try {
                    Object messageHandler = client.getClass().getMethod("method_44714").invoke(client);
                    messageHandler.getClass().getMethod("method_44736", Text.class, boolean.class).invoke(messageHandler, Text.literal(str), false);
                } catch (Exception e) {
                    Registry<MessageType> registry = client.world.getRegistryManager().get(Registry.MESSAGE_TYPE_KEY);
                    MessageType messageType = registry.get(MessageType.CHAT);
                    client.inGameHud.onGameMessage(messageType, Text.literal(str));
                }
            }
            return ActionResult.SUCCESS;
        });
    }

    ActionResult register(String msg);
}
