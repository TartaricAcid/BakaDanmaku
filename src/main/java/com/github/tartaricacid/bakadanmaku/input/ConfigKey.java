package com.github.tartaricacid.bakadanmaku.input;

import com.github.tartaricacid.bakadanmaku.utils.OpenCloseDanmaku;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static net.minecraft.util.Util.field_240973_b_;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ConfigKey {
    public static final KeyBinding CONFIG_KEY = new KeyBinding("key.bakadanmaku.config",
            KeyConflictContext.IN_GAME,
            KeyModifier.ALT,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.category.bakadanmaku");

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        if (CONFIG_KEY.isPressed()) {
            OpenCloseDanmaku.closeDanmaku();
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("弹幕配置正在重载中……"), field_240973_b_);
            }
            OpenCloseDanmaku.openDanmaku();
        }
    }
}
