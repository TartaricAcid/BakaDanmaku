package com.github.tartaricacid.bakadanmaku.input;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeybindingRegistry {
    @SubscribeEvent
    public static void onRegisterKeyMapping(RegisterKeyMappingsEvent event) {
        event.register(ConfigKey.CONFIG_KEY);
    }
}
