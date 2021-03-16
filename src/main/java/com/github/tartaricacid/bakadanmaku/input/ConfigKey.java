package com.github.tartaricacid.bakadanmaku.input;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class ConfigKey {
    public static final KeyBinding CONFIG_KEY = new KeyBinding(
            "key.bakadanmaku.config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.category.bakadanmaku"
    );

    public static void onKeyboardInput() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (CONFIG_KEY.wasPressed()) {
                if (client.player != null) {
                    OpenCloseDanmaku.closeDanmaku();
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("弹幕配置正在重载中……"), false);
                    }
                    OpenCloseDanmaku.openDanmaku();
                }
            }
        });
    }
}
