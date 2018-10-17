package github.tartaricacid.bakadanmaku.config;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.api.thread.DanmakuThreadFactory;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BakaDanmaku.MOD_ID, name = "BakaDanmaku", category = "baka_danmaku_mod")
public class BakaDanmakuConfig {
    @Config.Name("通用设置")
    public static General general = new General();

    @Config.Name("聊天栏弹幕设置")
    public static ChatMsg chatMsg = new ChatMsg();

    @Config.Name("网络配置")
    public static Network network = new Network();

    @Config.Name("直播平台设置")
    public static LivePlatform livePlatform = new LivePlatform();

    public static class General {
        @Config.Comment("直播平台选择，可填 bilibili 和 douyu")
        @Config.Name("直播平台")
        public String platform = "bilibili";

        @Config.Comment("是否显示人气值信息")
        @Config.Name("是否显示人气值")
        public Boolean showPopularity = true;

        @Config.Comment("显示的人气值格式，注意格式符")
        @Config.Name("人气值格式")
        public String popularityStyle = "§6§l人气值: %1$s";
    }

    public static class ChatMsg {
        @Config.Comment("是否显示弹幕")
        @Config.Name("是否显示弹幕")
        public Boolean showDanmaku = true;

        @Config.Comment("发送的弹幕信息格式，注意格式符")
        @Config.Name("弹幕格式")
        public String danmakuStyle = "§7§l[§8§l%1$s§7§l] §6§l%2$s: §f§l%3$s";

        @Config.Comment("是否显示礼物信息")
        @Config.Name("是否显示礼物")
        public Boolean showGift = true;

        @Config.Comment("发送的礼物信息格式，注意格式符")
        @Config.Name("礼物格式")
        public String giftStyle = "§7§l[§8§l%1$s§7§l] §8§l%2$s: %3$sx%4$d";

        @Config.Comment("是否显示欢迎信息")
        @Config.Name("是否显示欢迎信息")
        public Boolean showWelcome = true;

        @Config.Comment("发送的欢迎信息格式，注意格式符")
        @Config.Name("欢迎信息格式")
        public String welcomeStyle = "§7§l[§8§l%1$s§7§l] §f§l欢迎 §6§l%2$s§f§l 加入直播间";
    }

    public static class LivePlatform {
        @Config.Name("哔哩哔哩直播间配置")
        public BilibiliRoom bilibiliRoom = new BilibiliRoom();

        @Config.Name("斗鱼直播间配置")
        public DouyuRoom douyuRoom = new DouyuRoom();

        @Config.Name("触手直播间配置")
        public ChushouRoom chushouRoom = new ChushouRoom();

        public class BilibiliRoom {
            @Config.Comment("直播间房间号，我想你们应该知道在哪获取")
            @Config.Name("直播房间号")
            @Config.RangeInt(min = 0)
            public int liveRoom = 0;

            @Config.Comment("代表信息来源的标识符，显示在开头")
            @Config.Name("自定义直播平台名称")
            public String platformDisplayName = "哔哩哔哩";
        }

        public class DouyuRoom {
            @Config.Comment("直播间房间号，我想你们应该知道在哪获取")
            @Config.Name("直播房间号")
            @Config.RangeInt(min = 0)
            public int liveRoom = 0;

            @Config.Comment("代表信息来源的标识符，显示在开头")
            @Config.Name("自定义直播平台名称")
            public String platformDisplayName = "斗鱼";
        }

        public class ChushouRoom {
            @Config.Comment("直播间房间号，我想你们应该知道在哪获取")
            @Config.Name("直播房间号")
            @Config.RangeInt(min = 0)
            public int liveRoom = 0;

            @Config.Comment("代表信息来源的标识符，显示在开头")
            @Config.Name("自定义直播平台名称")
            public String platformDisplayName = "触手";
        }
    }

    public static class Network {
        @Config.Comment("测试网络连通性时的超时时间")
        @Config.Name("超时时间")
        public int timeout = 2000;

        @Config.Comment("连接失败后重连的次数")
        @Config.Name("重连次数")
        public int retry = 3;

        @Config.Comment("连接失败后的重连间隔，单位毫秒；0 代表不重连，直接退出")
        @Config.Name("重连间隔")
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

                if (BakaDanmaku.player != null) {
                    // 提示信息
                    BaseDanmakuThread.sendChatMessage("§8§l配置已经保存，正在重启中……");

                    // 重载房间信息，单独开启一个线程，防止卡死游戏主线程
                    new Thread(DanmakuThreadFactory::restartThreads, "BakaDanmakuChangeConfig").start();
                }
            }
        }
    }
}
