package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.event.post.SendDanmakuEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SendDanmaku {
    public SendDanmaku() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onSendDanmaku(SendDanmakuEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;
        if (player != null) {
            player.addChatMessage(new TextComponentString(event.getMessage()));
        }
    }
}
