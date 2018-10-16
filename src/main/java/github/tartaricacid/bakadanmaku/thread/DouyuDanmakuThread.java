package github.tartaricacid.bakadanmaku.thread;

import com.google.common.io.ByteStreams;
import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.api.event.DanmakuEvent;
import github.tartaricacid.bakadanmaku.api.event.GiftEvent;
import github.tartaricacid.bakadanmaku.api.event.PopularityEvent;
import github.tartaricacid.bakadanmaku.api.event.WelcomeEvent;
import github.tartaricacid.bakadanmaku.api.thread.BaseDanmakuThread;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DouyuDanmakuThread extends BaseDanmakuThread {
    private static final String LIVE_URL = "openbarrage.douyutv.com"; // 斗鱼弹幕地址
    private static final int PORT = 8601; // Webscoket 端口

    private static Pattern readHotValue = Pattern.compile("\"online\":(\\d+),"); // 读取弹幕发送者

    private static Pattern readDanmakuUser = Pattern.compile("nn@=(.*?)/"); // 读取弹幕发送者
    private static Pattern readDanmakuInfo = Pattern.compile("txt@=(.*?)/"); // 读取弹幕文本

    private static Pattern readGiftUser = Pattern.compile("nn@=(.*?)/"); // 读取礼物发送者
    private static Pattern readGiftName = Pattern.compile("gfid@=(.*?)/"); // 读取礼物名称
    private static Pattern readGiftNum = Pattern.compile("gfcnt@=(.*?)/"); // 读取礼物发送者

    private static Pattern readWelcomeUser = Pattern.compile("nn@=(.*?)/"); // 读取欢迎的用户名

    private DataOutputStream dataOutputStream; // 等会读取数据流的

    @Override
    public boolean preRunCheck() {
        boolean check = super.preRunCheck();
        // 处理直播房间未设置的问题
        if (BakaDanmakuConfig.douyuRoom.liveRoom == 0) {
            sendChatMessage("§8§l直播房间 ID 未设置，弹幕机已停止工作！ ");
            check = false;
        }

        return check;
    }

    @Override
    public void doRun() {
        // 获取真实房间 ID
        String roomID = String.valueOf(BakaDanmakuConfig.douyuRoom.liveRoom);

        // 提示，相关房间信息已经获取
        sendChatMessage("§8§l直播房间 ID 已经获取，ID 为 " + roomID);

        try {
            // 连接
            Socket socket = new Socket(LIVE_URL, PORT);

            // 获取数据输出流
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            // 获取输出流
            InputStream inputStream = socket.getInputStream();

            // 发送加入信息
            sendJoinMsg(roomID);

            // 提示，已经连接
            sendChatMessage("§8§l弹幕机已经连接");

            // 直播热度值获取，Post PopularityEvent
            MinecraftForge.EVENT_BUS.post(new PopularityEvent(BakaDanmakuConfig.douyuRoom.platformDisplayName, getHotValue()));

            // 创建定时器
            Timer timer = new Timer();
            // 利用 timer 模块定时发送心跳包，同时定期更新直播间的热度值，周期为 45 秒
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // 心跳包
                    sendHeartBeat();

                    // 直播热度值更新，Post PopularityEvent
                    MinecraftForge.EVENT_BUS.post(new PopularityEvent(BakaDanmakuConfig.douyuRoom.platformDisplayName, getHotValue()));
                }
            }, 45000, 45000);

            // 等待验证
            while (keepRunning) {
                // 开始读取数据流，先开辟缓存区，因为头部为 8 字长，还有 4 字长消息长度
                byte[] buf = new byte[8 + 4];
                inputStream.read(buf);
                ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

                int length = LittleToBig(byteBuffer.getInt()); // 长度数据

                // 如果长度小于等于 8
                if (length <= 8) continue;

                // 剔除头部，进行读取
                byte[] bodyByte = new byte[length - 8];
                inputStream.read(bodyByte);

                // 如果长度大于 8，说明是有数据的
                String bodyStringFirst = new String(bodyByte, "UTF-8");

                // 看下是不是登陆信息
                if (bodyStringFirst.indexOf("type@=loginres/") == 0) {
                    sendChatMessage("§8§l斗鱼弹幕姬登陆成功");
                    break;
                }
            }

            // 发送分组信息
            sendDataPack((short) 689, String.format("type@=joingroup/rid@=%s/gid@=-9999", roomID));

            // 轮询接收弹幕
            while (keepRunning) {
                try {
                    // 开始读取数据流，先开辟缓存区，因为头部为 8 字长，还有 4 字长消息长度
                    byte[] buf = new byte[8 + 4];
                    inputStream.read(buf);
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

                    int length = LittleToBig(byteBuffer.getInt()); // 长度数据

                    // 如果长度小于等于 8，或者超出 String 上限，这种情况很常见
                    if (length <= 16 || length > 65534) continue;

                    // 剔除头部，进行读取
                    byte[] bodyByte = new byte[length - 8];
                    inputStream.read(bodyByte);

                    // 如果长度大于 8，说明是有数据的
                    String bodyString = new String(bodyByte, "UTF-8");

                    // BakaDanmaku.logger.info(bodyString);

                    // 开始判断，普通弹幕
                    if (bodyString.indexOf("type@=chatmsg") == 0) {
                        // 正则匹配
                        Matcher mDanmaku = readDanmakuInfo.matcher(bodyString);
                        Matcher mUser = readDanmakuUser.matcher(bodyString);

                        // 两者均匹配到才进行事件触发
                        if (mDanmaku.find() && mUser.find()) {
                            String danmuMsg = mDanmaku.group(1); // 弹幕信息
                            String user = mUser.group(1); // 发送者

                            // Post DanmakuEvent
                            MinecraftForge.EVENT_BUS.post(new DanmakuEvent(BakaDanmakuConfig.douyuRoom.platformDisplayName, user, danmuMsg));

                            continue;
                        }
                    }

                    // 开始判断，送礼弹幕
                    if (bodyString.indexOf("type@=dgb") == 0) {
                        // 正则匹配
                        Matcher mUser = readGiftUser.matcher(bodyString);
                        Matcher mGiftName = readGiftName.matcher(bodyString);
                        Matcher mNum = readGiftNum.matcher(bodyString);

                        // 两者均匹配到才进行事件触发
                        if (mGiftName.find() && mUser.find() && mNum.find()) {
                            String giftName = mGiftName.group(1); // 礼物信息
                            String user = mUser.group(1); // 发送者
                            int num = Integer.valueOf(mNum.group(1)); // 礼物数量

                            // Post GiftEvent
                            MinecraftForge.EVENT_BUS.post(new GiftEvent(BakaDanmakuConfig.douyuRoom.platformDisplayName, giftName, num, user));

                            continue;
                        }
                    }

                    // 开始判断，玩家进入
                    if (bodyString.indexOf("type@=uenter") == 0) {
                        // 正则匹配
                        Matcher mUser = readWelcomeUser.matcher(bodyString);

                        // 两者均匹配到才进行事件触发
                        if (mUser.find()) {
                            String user = mUser.group(1); // 玩家名称

                            // Post WelcomeEvent
                            MinecraftForge.EVENT_BUS.post(new WelcomeEvent(BakaDanmakuConfig.douyuRoom.platformDisplayName, user));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sendDataPack((short) 689, "type@=logout/"); // 发送断开包
            timer.cancel(); // 关闭心跳包线程的定时器
            socket.close(); // 关闭 socket
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 清除部分
     */
    @Override
    public void clear() {
    }

    /**
     * 首次连接之前测试网络连通性
     *
     * @return 能否找到斗鱼弹幕服务器
     */
    private boolean isReachable() {
        try {
            return InetAddress.getByName(LIVE_URL).isReachable(BakaDanmakuConfig.network.timeout);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 首次连接发送的认证数据
     *
     * @param roomId 直播房间 ID
     */
    private void sendJoinMsg(String roomId) {
        // 发送验证包
        sendDataPack((short) 689, String.format("type@=loginreq/roomid@=%s/", roomId));
    }

    /**
     * 客户端发送的心跳包，45 秒发送一次
     */
    private void sendHeartBeat() {
        sendDataPack((short) 689, "type@=mrkl/");
    }

    /**
     * 读取直播热度值
     *
     * @return 直播热度值
     */
    private int getHotValue() {
        // 初始化
        int hotValue = 0;

        try {
            // B 站提供的获取直播 id 的 api
            URL url = new URL("http://open.douyucdn.cn/api/RoomApi/room/" + BakaDanmakuConfig.douyuRoom.liveRoom);

            // 获取网络数据流
            InputStream con = url.openStream();

            // 按照 UTF-8 编码解析
            String data = new String(ByteStreams.toByteArray(con), StandardCharsets.UTF_8);

            // 关闭数据流
            con.close();

            // 简单的 JSON 不需要用 Gson 解析，正则省事
            Matcher matcher = readHotValue.matcher(data);
            if (matcher.find()) {
                hotValue = Integer.valueOf(matcher.group(1));
            } else {
                // 日志记录
                BakaDanmaku.logger.fatal("Cannot get live hot value.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hotValue;
    }

    /**
     * 固定的发包方法
     *
     * @param action 消息类型，消息类型有 689（C->S） 和 690（S->C）
     * @param body   发送的数据本体部分
     */
    private void sendDataPack(Short action, String body) {
        try {
            // 数据部分，要求以 \0 结尾，同时以 UTF-8 编码解析成 Byte
            body = body + '\0';
            byte[] bodyBytes = body.getBytes("UTF-8");

            // 封包总长度，因为头部固定为 8 字长，故加上 8
            int length = bodyBytes.length + 8;

            // 为缓冲区分配长度，再来个 4 字长的长度数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(length + 4);

            // 存两遍（鬼知道斗鱼在干啥） 4 字长的封包总大小数据，所以为 int
            // 别忘了斗鱼是该死的小端模式
            byteBuffer.putInt(BigToLittle(length));
            byteBuffer.putInt(BigToLittle(length));

            // 存入 2 字长的消息类型，消息类型有 689 和 690
            byteBuffer.putShort(BigToLittle(action));

            // 存入加密字段和保留字段，目前都为 0，总共 2 字长
            byteBuffer.putShort((short) 0);

            // 存入数据
            byteBuffer.put(bodyBytes);

            // 写入输出数据流中
            dataOutputStream.write(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 超时重连的间隔
     */
    private void waitForRetryInterval() {
        try {
            Thread.sleep(BakaDanmakuConfig.network.retryInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 4 字节大端模式数据 -> 小端模式数据
     *
     * @param big 大端模式数据
     * @return 小端模式数据
     */
    private int BigToLittle(int big) {
        // 利用 ByteBuffer 把 int 转 byte[]
        byte[] bytes = ByteBuffer.allocate(4).putInt(big).array();

        // 倒置数组，然后处理成 int，返回
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    /**
     * 2 字节大端模式数据 -> 小端模式数据
     *
     * @param big 大端模式数据
     * @return 小端模式数据
     */
    private short BigToLittle(short big) {
        // 利用 ByteBuffer 把 short 转 byte[]
        byte[] bytes = ByteBuffer.allocate(2).putShort(big).array();

        // 倒置数组，然后处理成 int，返回
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    /**
     * 4 字节小端模式数据 -> 大端模式数据
     *
     * @param little 小端模式数据
     * @return 大端模式数据
     */
    private int LittleToBig(int little) {
        // 利用 ByteBuffer 把 int 转 byte[]
        byte[] bytes = ByteBuffer.allocate(4).putInt(little).array();

        // 倒置数组，然后处理成 int，返回
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
}
