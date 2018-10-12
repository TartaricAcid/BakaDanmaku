package github.tartaricacid.bakadanmaku.event;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BakaDanmaku.MOD_ID)
public class PopularityEvent {
    private static int num = 0;

    public PopularityEvent(int num) {
        PopularityEvent.num = num;
    }

    @SubscribeEvent
    public static void showPopularityCount(RenderGameOverlayEvent.Post e) {
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;

        if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && gui != null && BakaDanmakuConfig.room.showPopularity) {
            gui.drawString(renderer, String.format(BakaDanmakuConfig.room.popularityStyle, String.valueOf(num)), 5, 5, 0xffffff);
        }
    }
}
