package github.tartaricacid.bakadanmaku.thread;

import com.google.common.io.ByteStreams;
import github.tartaricacid.bakadanmaku.api.event.DanmakuEvent;
import github.tartaricacid.bakadanmaku.api.event.GiftEvent;
import github.tartaricacid.bakadanmaku.api.event.PopularityEvent;
import github.tartaricacid.bakadanmaku.api.event.WelcomeEvent;
import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraftforge.common.MinecraftForge;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChushouDanmakuThread extends BaseDanmakuThread {
    private static final String URL_API = "https://chat.chushou.tv/chat/get.htm";

    private static Pattern readUser = Pattern.compile("\"nickname\":\"(.*?)\""); // 读取弹幕发送者
    private static Pattern readInfo = Pattern.compile("\"content\":\"(.*?)\""); // 读取具体信息
    private static Pattern readTime = Pattern.compile("\"createdTime\":(\\d+)"); // 读取信息发送时间
    private static Pattern readOnlineCount = Pattern.compile("\"onlineCount\":(\\d+)"); // 读取人气值

    private static long time; // 存储时间的参数，因为部分弹幕是重复显示的

    /**
     * 获取弹幕等信息的主方法
     *
     * @param room 触手直播房间号
     */
    private static void getInfo(int room) {
        try {
            // 触手提供的获取直播弹幕的 api
            URL url = new URL(URL_API + "?roomId=" + room);

            // 获取网络数据流
            InputStream con = url.openStream();

            // 按照 UTF-8 编码解析
            String data = new String(ByteStreams.toByteArray(con), StandardCharsets.UTF_8);

            // 关闭数据流
            con.close();

            Matcher mUser = readUser.matcher(data); // 用户
            Matcher mInfo = readInfo.matcher(data); // 信息
            Matcher mTime = readTime.matcher(data); // 信息发送事件
            Matcher mOnlineCount = readOnlineCount.matcher(data); // 人气值信息

            // 首先分析人气值信息，这是因为弹幕信息可能会出现多次，但是人气值只会出现一次
            if (mOnlineCount.find()) {
                // 人气值，所以 Post PopularityEvent
                MinecraftForge.EVENT_BUS.post(new PopularityEvent(BakaDanmakuConfig.livePlatform.chushouRoom.platformDisplayName,
                        Integer.valueOf(mOnlineCount.group(1))));
            }


            /* 当三者全找到时：
             * 普通礼物提醒信息、连击礼物提醒信息、系统抽奖之类的信息
             * 同时还夹杂送礼和普通弹幕、欢迎观众进直播间信息、观众关注主播提示信息
             * 毫无规律可循，无法判断类型，只能分析字符串
             */
            while (mTime.find() && mUser.find() && mInfo.find()) {
                // 先判定时间，放置重复信息显示
                if (Long.valueOf(mTime.group(1)) > time) {
                    // 交换信息时间
                    time = Long.valueOf(mTime.group(1));

                    // 普通弹幕的剖析，非普通弹幕，用户名会为一个 \u200E 的控制字符，需要剔除
                    // 此外送礼会额外提醒一条弹幕，过于冗余，剔除
                    if (!mUser.group(1).equals("\u200E") && !mInfo.group(1).contains("送给主播一个")) {
                        // 普通弹幕，所以 Post DanmakuEvent
                        MinecraftForge.EVENT_BUS.post(new DanmakuEvent(BakaDanmakuConfig.livePlatform.chushouRoom.platformDisplayName,
                                mUser.group(1).replace("\u200E", ""), mInfo.group(1)));
                        continue;
                    }

                    // 送礼信息，此时用户名处不为控制字符 ‎‎‎\u200E
                    if (!mUser.group(1).equals("\u200E") && mInfo.group(1).contains("送给主播一个")) {
                        // 送礼信息，Post GiftEvent
                        MinecraftForge.EVENT_BUS.post(new GiftEvent(BakaDanmakuConfig.livePlatform.chushouRoom.platformDisplayName,
                                mInfo.group(1).replace("送给主播一个", "").trim(), 1,
                                mUser.group(1).replace("\u200E", "")));
                        continue;
                    }

                    // 观众进入提醒信息，此时用户名处为控制字符 ‎‎‎\u200E
                    if (mUser.group(1).equals("\u200E") &&
                            (mInfo.group(1).contains("[图片]欢迎新宝宝") ||
                                    mInfo.group(1).contains("骑着大虎鲸冲进直播间") ||
                                    mInfo.group(1).contains("吹着魔法海螺亮相啦~"))) {
                        // 进行欢迎，所以 Post WelcomeEvent
                        MinecraftForge.EVENT_BUS.post(new WelcomeEvent(BakaDanmakuConfig.livePlatform.chushouRoom.platformDisplayName,
                                mInfo.group(1).replace("[图片]欢迎新宝宝", "")
                                        .replace("骑着大虎鲸冲进直播间！", "")
                                        .replace("吹着魔法海螺亮相啦~", "")
                                        .trim()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean preRunCheck() {
        boolean check = super.preRunCheck();
        // 处理直播房间未设置的问题
        if (BakaDanmakuConfig.livePlatform.chushouRoom.liveRoom == 0) {
            sendChatMessage("§8§l直播房间 ID 未设置，弹幕机已停止工作！ ");
            check = false;
        }

        return check;
    }

    @Override
    public void doRun() {
        // 记录当前启动时间戳
        time = System.currentTimeMillis();

        // 创建定时器
        Timer timer = new Timer();

        // 利用 timer 模块定时发送心跳包，获取弹幕等信息，周期为 3 秒
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // 关闭心跳包线程的定时器
                if (!keepRunning) timer.cancel();

                // 加一个判定，防止最后一个定时执行还是进行了
                if (keepRunning) getInfo(BakaDanmakuConfig.livePlatform.chushouRoom.liveRoom);
            }
        }, 3000, 50);
    }

    @Override
    public void clear() {
    }
}
