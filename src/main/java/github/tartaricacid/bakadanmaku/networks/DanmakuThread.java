package github.tartaricacid.bakadanmaku.networks;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import github.tartaricacid.bakadanmaku.BakaDanmaku;
import github.tartaricacid.bakadanmaku.config.BakaDanmakuConfig;
import github.tartaricacid.bakadanmaku.event.DanmakuEvent;
import github.tartaricacid.bakadanmaku.event.GiftEvent;
import github.tartaricacid.bakadanmaku.event.PopularityEvent;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.Level;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DanmakuThread implements Runnable {
    private static final String LIVE_URL = "livecmt-1.bilibili.com";
    private static final int PROT = 788;
    private static final String INIT_URL = "https://api.live.bilibili.com/room/v1/Room/room_init";
    private static Pattern extractRoomId = Pattern.compile("\"room_id\":(\\d+),");

    // 特殊的修饰符 volatile，用来标定是否进行连接
    static volatile boolean keepRunning = true;

    private static Gson gson = new Gson();
    private DataOutputStream dataOutputStream;

    @Override
    public void run() {
        // 获取真实房间 ID
        String roomID = getRoomId(BakaDanmakuConfig.room.liveRoom);

        try {
            // 连接
            Socket socket = new Socket(LIVE_URL, PROT);
            // 获取数据输出流
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            // 获取输出流
            InputStream inputStream = socket.getInputStream();
            // 发送加入信息
            sendJoinMsg(roomID);

            // 创建定时器
            Timer timer = new Timer();
            // 利用 timer 模块定时发送心跳包，周期为 30 秒
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    sendHeartBeat();
                }
            }, 30000, 30000);

            while (keepRunning) {
                try {
                    // 开始读取数据流，先开辟缓存区
                    byte[] buf = new byte[16];
                    inputStream.read(buf);
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buf);

                    int length = byteBuffer.getInt(); // 头部的长度数据
                    int headLength = byteBuffer.getShort(); // 头部长度
                    int protocol = byteBuffer.getShort(); // 协议
                    int action = byteBuffer.getInt(); // 操作码
                    int sequence = byteBuffer.getInt(); // 协议

                    // 如果长度小于等于 16
                    if (length <= 16) continue;

                    // 剔除头部，进行读取
                    byte[] bodyByte = new byte[length - 16];
                    inputStream.read(bodyByte);

                    /* 如果长度大于 16，说明是有数据的
                     3：人气值
                     5：弹幕
                    */
                    if (action == 3) {
                        // byte 数组数据转 Int
                        int num = ByteBuffer.wrap(bodyByte).getInt();

                        // 实例化 PopularityEvent
                        new PopularityEvent(num);
                    }

                    if (action == 5) {
                        // UTF-8 解码
                        String bodyString = new String(bodyByte, "UTF-8");

                        // 数据解析成 json
                        Object o = gson.fromJson(bodyString, Object.class);

                        // 数据解析
                        LinkedTreeMap jsonMap = (LinkedTreeMap) o;
                        String msgType = (String) jsonMap.get("cmd");

                        /*
                        DANMU_MSG	收到弹幕
                        SEND_GIFT	有人送礼
                        WELCOME	欢迎加入房间
                        WELCOME_GUARD	欢迎房管加入房间
                        SYS_MSG	系统消息
                        */
                        switch (msgType) {
                            case "DANMU_MSG": {
                                ArrayList infoList = (ArrayList) jsonMap.get("info");

                                // 具体的发送者和信息
                                String danmuMsg = (String) infoList.get(1);
                                String user = (String) ((ArrayList) infoList.get(2)).get(1);

                                // 实例化 DanmakuEvent
                                new DanmakuEvent(user, danmuMsg);
                            }

                            case "SEND_GIFT": {
                                // 莫名会为空，加个判定再进行解析
                                if (jsonMap.get("data") == null) continue;
                                LinkedTreeMap dataMap = (LinkedTreeMap) jsonMap.get("data");

                                // 具体的送礼信息
                                String giftName = (String) dataMap.get("giftName");
                                int num = ((Double) dataMap.get("num")).intValue();
                                String user = (String) dataMap.get("uname");
                                String face = (String) dataMap.get("face");

                                // 实例化 GiftEvent
                                new GiftEvent(giftName, num, user, face);
                            }

                            case "WELCOME": {
                            }

                            case "WELCOME_GUARD": {
                            }

                            case "SYS_MSG": {
                            }

                            default: {
                            }
                        }
                    }
                } catch (JsonSyntaxException joe) {
                    // 送礼数据解析可能会出现异常，捕捉一下
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            socket.close(); // 关闭 socket
            timer.cancel(); // 关闭心跳包线程的定时器
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


    }

    /**
     * 首次连接发送的认证数据
     *
     * @param roomId 真实的直播房间 ID
     */
    private void sendJoinMsg(String roomId) {
        long clientId = RandomUtils.nextLong(100000000000000L, 300000000000000L);
        sendDataPack(7, String.format("{\"roomid\":%s,\"uid\":%d,\"protover\": 1,\"platform\": \"web\",\"clientver\": \"1.4.0\"}",
                roomId,
                clientId));
    }

    /**
     * 客户端发送的心跳包，30 秒发送一次
     */
    private void sendHeartBeat() {
        sendDataPack(2, "");
    }

    /**
     * 固定的发包方法
     *
     * @param action 操作码，可选 2,3,5,7,8
     * @param body   发送的数据本体部分
     */
    private void sendDataPack(int action, String body) {
        try {
            // 数据部分，以 UTF-8 编码解析成 Byte
            byte[] bodyBytes = body.getBytes("UTF-8");

            // 封包总长度，因为头部固定为 16 字长，故加上 16
            int length = bodyBytes.length + 16;

            // 为缓冲区分配长度
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);

            // 存入 4 字长的封包总大小数据，所以为 int
            byteBuffer.putInt(length);

            // 存入 2 字长的头部长度数据，头部长度固定为 16
            byteBuffer.putShort((short) 16);

            // 存入 2 字长的协议版本数据，默认为 1
            byteBuffer.putShort((short) 1);

            // 存入 4 字长的操作码，操作码有 2,3,5,7,8
            byteBuffer.putInt(action);

            // 存入 4 字长的 sequence，意味不明，取常数 1
            byteBuffer.putInt(1);

            // 存入数据
            byteBuffer.put(bodyBytes);

            // 写入输出数据流中
            dataOutputStream.write(byteBuffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取真实的直播房间 ID
     *
     * @param roomId 直播房间 ID
     * @return 真实的直播房间 ID
     */
    private String getRoomId(int roomId) {
        // 初始化
        String realRoomId = null;

        try {
            // B 站提供的获取直播 id 的 api
            URL url = new URL(INIT_URL + "?id=" + roomId);

            // 获取网络数据流
            InputStream con = url.openStream();

            // 按照 UTF-8 编码解析
            String data = new String(ByteStreams.toByteArray(con), "UTF-8");

            // 关闭数据流
            con.close();

            // 简单的 JSON 不需要用 Gson 解析，正则省事
            Matcher matcher = extractRoomId.matcher(data);
            if (matcher.find()) {
                realRoomId = matcher.group(1);
            } else {
                FMLLog.log(BakaDanmaku.MOD_ID, Level.FATAL, "can not get room id");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return realRoomId;
    }
}
