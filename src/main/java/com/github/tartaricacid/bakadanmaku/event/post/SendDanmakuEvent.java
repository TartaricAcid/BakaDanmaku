package com.github.tartaricacid.bakadanmaku.event.post;


import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
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
        try {
            Object chatListener = minecraft.getClass().getMethod("m_240442_").invoke(minecraft);
            // ChatListener chatListener = minecraft.getChatListener();
            chatListener.getClass().getMethod("m_240494_", Component.class, boolean.class).invoke(chatListener, Component.literal(event.getMessage()), false);
            // chatListener.handleSystemChat(Component.literal(event.getMessage()), false);
        } catch (Exception e) {
            Registry<ChatType> chatTypes = RegistryAccess.BUILTIN.get().registryOrThrow(Registry.CHAT_TYPE_REGISTRY);
            ChatType chatType = chatTypes.get(ChatType.CHAT);
            if (chatType != null) {
                minecraft.gui.handleSystemChat(chatType, Component.literal(event.getMessage()));
            }
        }
    }

    public String getMessage() {
        return message;
    }
}
