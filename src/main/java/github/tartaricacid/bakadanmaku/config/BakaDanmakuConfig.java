package github.tartaricacid.bakadanmaku.config;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Config(modid = BakaDanmaku.MOD_ID, name = "BakaDanmaku", category = "baka_danmaku_mod")
public class BakaDanmakuConfig {
    @Config.Name("通用设置")
    public static General general = new General();

    @Config.Name("哔哩哔哩直播间配置")
    public static BilibiliRoom bilibiliRoom = new BilibiliRoom();

    @Config.Name("聊天栏弹幕设置")
    public static ChatMsg chatMsg = new ChatMsg();

    @Config.Name("网络配置")
    public static Network network = new Network();

    public static class General {
        @Config.Comment("直播平台选择")
        public String platform = "bilibili";

        @Config.Comment("是否显示人气值信息")
        public Boolean showPopularity = true;

        @Config.Comment("显示的人气值格式，注意格式符")
        public String popularityStyle = "§2§l人气值§f§r：§f§l%1$s";
    }

    public static class BilibiliRoom {
        @Config.Comment("直播间房间号，我想你们应该知道在哪获取")
        @Config.RangeInt(min = 0)
        public int liveRoom = 0;

        @Config.Comment("是否显示弹幕")
        public Boolean showDanmaku = true;

        @Config.Comment("是否显示礼物信息")
        public Boolean showGift = true;

        @Config.Comment("是否启用在聊天栏输出弹幕信息")
        @Config.RequiresMcRestart
        public Boolean enableChatMsgHandler = true;

        // TODO: delete the "unfinished" tag when it's okay.
        @Config.Comment("是否启用屏幕上滚动弹幕信息（未完成）")
        @Config.RequiresMcRestart
        public Boolean enableScreenMsgHandler = false;
    }

    public static class ChatMsg {
        @Config.Comment("是否显示弹幕")
        public Boolean showDanmaku = true;

        @Config.Comment("发送的弹幕信息格式，注意格式符")
        public String danmakuStyle = "§f§r[§2§l%1$s§f§r] §6§l%2$s：§f§l%3$s";

        @Config.Comment("是否显示礼物信息")
        public Boolean showGift = true;

        @Config.Comment("发送的礼物信息格式，注意格式符")
        public String giftStyle = "§f§r[§2§l%1$s§f§r] §8§l%2$s：%3$sx%4$d";
    }

    public static class Network {
        @Config.Comment("测试网络连通性时的超时时间")
        public int timeout = 2000;

        @Config.Comment("连接失败后重连的次数")
        public int retry = 3;

        @Config.Comment("连接失败后的重连间隔，单位毫秒；0代表不重连，直接退出")
        @Config.RangeInt(min = 0)
        public int retryInterval = 2000;
    }


    /**
     * 用于 GUI 界面配置调节的保存
     */
    @Mod.EventBusSubscriber(modid = BakaDanmaku.MOD_ID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(BakaDanmaku.MOD_ID)) {
                // 重载配置
                ConfigManager.sync(BakaDanmaku.MOD_ID, Config.Type.INSTANCE);

                // 提示已经关闭
                if (Minecraft.getMinecraft().player != null)
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString("§8§l配置已经保存，正在重启中……"));

                // 重载房间信息，单独开启一个线程，防止卡死游戏主线程
                new Thread(DanmakuThreadFactory::restartThreads, "BakaDanmakuChangeConfig").start();
            }
        }
    }
}
