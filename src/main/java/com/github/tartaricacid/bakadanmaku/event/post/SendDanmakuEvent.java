package com.github.tartaricacid.bakadanmaku.event.post;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
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
        if (minecraft.ingameGUI != null) {
            minecraft.ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString(event.getMessage()));
        }
    }

    public String getMessage() {
        return message;
    }
}
