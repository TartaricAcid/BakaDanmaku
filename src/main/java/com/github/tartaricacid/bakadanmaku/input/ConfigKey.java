package com.github.tartaricacid.bakadanmaku.input;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT)
public class ConfigKey {
    public static final KeyMapping CONFIG_KEY = new KeyMapping("key.bakadanmaku.config",
            KeyConflictContext.IN_GAME,
            KeyModifier.ALT,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.category.bakadanmaku");

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        if (CONFIG_KEY.isDown()) {
            OpenCloseDanmaku.closeDanmaku();
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("弹幕配置正在重载中……"));
            }
            OpenCloseDanmaku.openDanmaku();
        }
    }
}
