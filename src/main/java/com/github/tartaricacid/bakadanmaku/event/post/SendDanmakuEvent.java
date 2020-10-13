package com.github.tartaricacid.bakadanmaku.event.post;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SendDanmakuEvent extends Event {
    private final String message;

    public SendDanmakuEvent(String message) {
        this.message = message;
    }

    @SubscribeEvent
    public static void onSendDanmaku(SendDanmakuEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerEntity player = minecraft.player;
        if (player != null) {
            player.sendMessage(new StringTextComponent(event.getMessage()));
        }
    }

    public String getMessage() {
        return message;
    }
}
