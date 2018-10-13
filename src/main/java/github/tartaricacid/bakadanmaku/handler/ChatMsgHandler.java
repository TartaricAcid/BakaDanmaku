package github.tartaricacid.bakadanmaku.handler;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import github.tartaricacid.bakadanmaku.api.DanmakuEvent;
import github.tartaricacid.bakadanmaku.api.GiftEvent;
import github.tartaricacid.bakadanmaku.api.PopularityEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = BakaDanmaku.MOD_ID)
public class ChatMsgHandler {
    private static int tmpPopularityCount = 0; // 临时静态变量，缓存人气值数据，在两个事件间传递

    /**
     * 发送普通弹幕
     *
     * @param e 发送弹幕事件
     */
    @SubscribeEvent
    public static void sendDanmaku(DanmakuEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            player.sendMessage(new TextComponentString(String.format(BakaDanmakuConfig.room.danmakuStyle, e.getUser(), e.getMsg())));
        }
    }

    /**
     * 发送礼物
     *
     * @param e 发送礼物事件
     */
    @SubscribeEvent
    public static void sendGift(GiftEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player != null) {
            player.sendMessage(new TextComponentString(String.format(BakaDanmakuConfig.room.giftStyle, e.getUser(), e.getGiftName(), e.getNum())));
        }
    }

    /**
     * 获取人气值
     *
     * @param e 得到人气值事件
     */
    @SubscribeEvent
    public static void getPopularityCount(PopularityEvent e) {
        tmpPopularityCount = e.getNum();
    }

    /**
     * 游戏界面显示人气值
     *
     * @param e 渲染游戏界面事件
     */
    @SubscribeEvent
    public static void showPopularityCount(RenderGameOverlayEvent.Post e) {
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;

        // 当渲染快捷栏时候进行显示，意味着 F1 会隐藏
        if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && gui != null && BakaDanmakuConfig.room.showPopularity) {
            gui.drawString(renderer, String.format(BakaDanmakuConfig.room.popularityStyle, String.valueOf(tmpPopularityCount)), 5, 5, 0xffffff);
        }
    }
}
