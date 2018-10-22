package github.tartaricacid.bakadanmaku.handler;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.api.event.DanmakuEvent;
import github.tartaricacid.bakadanmaku.api.event.GiftEvent;
import github.tartaricacid.bakadanmaku.api.event.PopularityEvent;
import github.tartaricacid.bakadanmaku.api.event.WelcomeEvent;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = BakaDanmaku.MOD_ID)
public class ChatMsgHandler {
    private static int tmpPopularityCount = 0; // 临时静态变量，缓存人气值数据，在两个事件间传递

    /**
     * 发送普通弹幕
     *
     * @param e 发送弹幕事件
     */
    @SubscribeEvent
    public static void receiveDanmaku(DanmakuEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Pattern readDanmakuUser = Pattern.compile(BakaDanmakuConfig.blockFunction.blockPlayer); // 屏蔽弹幕发送者
        Pattern readDanmakuMsg = Pattern.compile(BakaDanmakuConfig.blockFunction.blockDanmaku); // 屏蔽弹幕关键词

        // 先判定是否开启弹幕显示
        if (player != null && BakaDanmakuConfig.chatMsg.showDanmaku) {
            // 获取弹幕信息
            String msg = e.getMsg();

            // 进行格式符消除，关键词屏蔽
            if (BakaDanmakuConfig.blockFunction.blockFormatCode) {
                // 替换消除格式符，更有效的祛除格式符
                msg = e.getMsg().replaceAll("(?:§(?=§*(\\1?+[0-9a-fk-or])))+\\1", "");

                // 如果不为空，消除关键词
                if (!BakaDanmakuConfig.blockFunction.blockKeyword.isEmpty())
                    msg = msg.replaceAll(BakaDanmakuConfig.blockFunction.blockKeyword, "*");
            }

            // 进行指定玩家屏蔽，指定弹幕屏蔽
            Matcher mUser = readDanmakuUser.matcher(e.getUser());
            Matcher mMsg = readDanmakuMsg.matcher(msg);
            // 没找到，或者对应列表为空
            if ((!mUser.find() || BakaDanmakuConfig.blockFunction.blockPlayer.isEmpty())
                    && (!mMsg.find() || BakaDanmakuConfig.blockFunction.blockDanmaku.isEmpty())) {
                player.sendMessage(new TextComponentString(String.format(BakaDanmakuConfig.chatMsg.danmakuStyle,
                        e.getPlatform(), e.getUser(), msg)));
            }
        }
    }

    /**
     * 发送礼物
     *
     * @param e 发送礼物事件
     */
    @SubscribeEvent
    public static void receiveGift(GiftEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Pattern readGiftName = Pattern.compile(BakaDanmakuConfig.blockFunction.blockGift); // 屏蔽礼物

        // 先判定是否开启礼物显示
        if (player != null && BakaDanmakuConfig.chatMsg.showGift) {
            // 进行礼物屏蔽
            Matcher mGift = readGiftName.matcher(e.getGiftName());
            // 没找到，或者对应列表为空
            if (!mGift.find() || BakaDanmakuConfig.blockFunction.blockGift.isEmpty()) {
                player.sendMessage(new TextComponentString(String.format(BakaDanmakuConfig.chatMsg.giftStyle,
                        e.getPlatform(), e.getUser(), e.getGiftName(), e.getNum())));
            }
        }
    }

    /**
     * 欢迎玩家进入
     *
     * @param e 玩家进入事件
     */
    @SubscribeEvent
    public static void welcome(WelcomeEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Pattern readWelcome = Pattern.compile(BakaDanmakuConfig.blockFunction.blockWelcome); // 屏蔽欢迎玩家

        // 先判定是否显示欢迎信息
        if (player != null && BakaDanmakuConfig.chatMsg.showWelcome) {
            // 进行玩家屏蔽
            Matcher mWelcome = readWelcome.matcher(e.getUser());
            // 没找到，或者对应列表为空
            if (!mWelcome.find() || BakaDanmakuConfig.blockFunction.blockWelcome.isEmpty()) {
                player.sendMessage(new TextComponentString(String.format(BakaDanmakuConfig.chatMsg.welcomeStyle,
                        e.getPlatform(), e.getUser())));
            }
        }
    }

    /**
     * 获取人气值
     *
     * @param e 得到人气值事件
     */
    @SubscribeEvent
    public static void getPopularityCount(PopularityEvent e) {
        tmpPopularityCount = e.getPopularity();
    }

    /**
     * 游戏界面显示人气值
     * TODO: 取消人气值与单独 Handler 的关联
     *
     * @param e 渲染游戏界面事件
     */
    @SubscribeEvent
    public static void showPopularityCount(RenderGameOverlayEvent.Post e) {
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI; // 获取 Minecraft 实例中的 GUI
        FontRenderer renderer = Minecraft.getMinecraft().fontRenderer; // 获取 Minecraft 原版字体渲染器

        // 当渲染快捷栏时候进行显示，意味着 F1 会隐藏
        if (e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && gui != null && BakaDanmakuConfig.general.showPopularity) {
            int x = (Minecraft.getMinecraft().displayWidth * BakaDanmakuConfig.general.posX) / 100; // 获取的配置宽度百分比
            int y = (Minecraft.getMinecraft().displayHeight * BakaDanmakuConfig.general.posY) / 100; // 获取的配置高度百分比

            gui.drawString(renderer, String.format(BakaDanmakuConfig.general.popularityStyle, String.valueOf(tmpPopularityCount)),
                    x, y, BakaDanmakuConfig.general.color);
        }
    }
}
