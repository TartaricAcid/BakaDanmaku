package com.github.tartaricacid.bakadanmaku.event;

import com.github.tartaricacid.bakadanmaku.event.post.SendDanmakuEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

public class SendDanmaku {
    @SubscribeEvent
    public void onSendDanmaku(SendDanmakuEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;
        if (player != null) {
            player.addChatMessage(new ChatComponentText(event.getMessage()));
        }
    }
}
