package github.tartaricacid.bakadanmaku.thread;

import com.google.common.io.ByteStreams;
import github.tartaricacid.bakadanmaku.api.event.DanmakuEvent;
import github.tartaricacid.bakadanmaku.api.event.GiftEvent;
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
    private static Pattern readTime = Pattern.compile("\"createdTime\":(\\d+)"); // 读取弹幕时间
    private static Pattern readType = Pattern.compile("\"type\":(\\d)"); // 读取弹幕发送者

    private static long time;

    public static void getInfo(int room) {
        try {
            // 触手提供的获取直播弹幕的 api
            URL url = new URL(URL_API + "?roomId=" + room);

            // 获取网络数据流
            InputStream con = url.openStream();

            // 按照 UTF-8 编码解析
            String data = new String(ByteStreams.toByteArray(con), StandardCharsets.UTF_8);

            // 关闭数据流
            con.close();

            Matcher mUser = readUser.matcher(data);
            Matcher mInfo = readInfo.matcher(data);
            Matcher mTime = readTime.matcher(data);

            Matcher mType = readType.matcher(data);
            while (mTime.find() && mType.find() && mUser.find() && mInfo.find()) {
                if (Long.valueOf(mTime.group(1)) > time) {
                    time = Long.valueOf(mTime.group(1));
                    switch (Integer.valueOf(mType.group(1))) {
                        case 1:
                            if (mUser.group(1).length() > 1) {
                                // Post DanmakuEvent
                                MinecraftForge.EVENT_BUS.post(new DanmakuEvent(BakaDanmakuConfig.livePlatform.chushouRoom.platformDisplayName, mUser.group(1).replace("\u200E", ""), mInfo.group(1)));
                                break;
                            }
                            if (mUser.group(1).length() == 1) {
                                // Post WelcomeEvent
                                MinecraftForge.EVENT_BUS.post(new WelcomeEvent(BakaDanmakuConfig.livePlatform.chushouRoom.platformDisplayName, mInfo.group(1).replace("[图片]欢迎新宝宝 ", "")));
                                break;
                            }

                        case 2:
                            // 类似于：
                            // [图片]婷儿家柒月 送给 沐婷儿🌸 1个520，这串数字是我最想对你说的话！[图片]
                            // TODO：也是送礼信息，但是解析稍微复杂
                            break;
                        case 3:
                            // Post GiftEvent
                            MinecraftForge.EVENT_BUS.post(new GiftEvent(BakaDanmakuConfig.livePlatform.chushouRoom.platformDisplayName, mInfo.group(1).replace("送给主播一个", ""), 1, mUser.group(1).replace("\u200E", "")));
                            break;
                        case 4:
                            // 没用的系统消息
                            break;
                        default:
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doRun() {
        time = System.currentTimeMillis(); // 记录弹幕启动时间

        // 创建定时器
        Timer timer = new Timer();
        // 利用 timer 模块定时发送心跳包，同时定期更新直播间的热度值，周期为 45 秒
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getInfo(BakaDanmakuConfig.livePlatform.chushouRoom.liveRoom);
            }
        }, 3000, 50);

        if (!keepRunning) {
            timer.cancel(); // 关闭心跳包线程的定时器
        }
    }

    @Override
    public void clear() {
    }
}
