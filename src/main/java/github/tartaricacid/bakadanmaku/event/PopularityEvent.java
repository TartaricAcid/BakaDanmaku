package github.tartaricacid.bakadanmaku.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PopularityEvent {
    private int num;

    public PopularityEvent(int num) {
        this.num = num;
    }

    @SubscribeEvent
    public void showPopularityCount(RenderGameOverlayEvent e) {
        GuiScreen gui = Minecraft.getMinecraft().currentScreen;

        if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            gui.drawHoveringText(String.valueOf(num), 5, 5);
        }
    }

}
