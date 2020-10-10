package com.github.tartaricacid.bakadanmaku.event.post;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SendDanmakuEvent extends Event {
    private final String message;

    public SendDanmakuEvent(String message) {
        this.message = message;
    }

    @SubscribeEvent
    public static void onSendDanmaku(SendDanmakuEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player != null) {
            player.sendMessage(new TextComponentString(event.getMessage()));
        }
    }

    public String getMessage() {
        return message;
    }
}
