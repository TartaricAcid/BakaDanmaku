package com.github.tartaricacid.bakadanmaku.mixin;


import com.github.tartaricacid.bakadanmaku.event.post.PlayerLoggedInEvent;
import com.github.tartaricacid.bakadanmaku.event.post.PlayerLoggedOutEvent;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerEventMixin {
    @Inject(at = @At("TAIL"), method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void playerLoggedIn(CallbackInfo info) {
        PlayerLoggedInEvent.EVENT.invoker().onLoggedInEvent();
    }

    @Inject(at = @At("HEAD"), method = "net/minecraft/server/PlayerManager.remove(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    private void playerLoggedOut(CallbackInfo info) {
        PlayerLoggedOutEvent.EVENT.invoker().onLoggedOutEvent();
    }
}
