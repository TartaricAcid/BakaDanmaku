package github.tartaricacid.bakadanmaku.config;

import github.tartaricacid.bakadanmaku.BakaDanmaku;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BakaDanmaku.MOD_ID, name = "BakaDanmaku", category = "baka_danmaku_mod")
public class BakaDanmakuConfig {
    @Config.Name("房间与弹幕配置")
    public static Room room = new Room();

    public static class Room {
        @Config.Comment("直播间房间号，我想你们应该知道在哪获取")
        @Config.LangKey("config.bakadanmaku.liveroom")
        @Config.RangeInt(min = 0)
        public int liveRoom = 0;

        @Config.Comment("发送的弹幕信息格式，注意格式符")
        @Config.LangKey("config.bakadanmaku.style")
        public String danmakuStyle = "§f§r[§2§lbilibili§f§r] §6§l%1$s：§f§l%2$s";

        @Config.Comment("发送的礼物信息格式，注意格式符")
        @Config.LangKey("config.bakadanmaku.style")
        public String giftStyle = "§f§r[§2§lbilibili§f§r] §8§l%1$s：%2$sx%3$d";
    }

    /**
     * 用于 GUI 界面配置调节的保存
     */
    @Mod.EventBusSubscriber(modid = BakaDanmaku.MOD_ID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(BakaDanmaku.MOD_ID)) {
                ConfigManager.sync(BakaDanmaku.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
