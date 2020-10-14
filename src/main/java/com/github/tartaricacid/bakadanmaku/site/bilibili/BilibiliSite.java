package com.github.tartaricacid.bakadanmaku.site.bilibili;

import com.github.tartaricacid.bakadanmaku.config.BilibiliConfig;
import com.github.tartaricacid.bakadanmaku.event.post.SendDanmakuEvent;
import com.github.tartaricacid.bakadanmaku.event.post.UpdatePopularInfoEvent;
import com.github.tartaricacid.bakadanmaku.site.ISite;
import com.github.tartaricacid.bakadanmaku.utils.BilibiliMsgSplit;
import com.github.tartaricacid.bakadanmaku.utils.Zlib;
import com.github.tartaricacid.bakadanmaku.websocket.WebSocketClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BilibiliSite implements ISite {
    private static final String URI = "wss://broadcastlv.chat.bilibili.com:443/sub";
    private static final long HEART_BEAT_INTERVAL = 30 * 1000;

    private static final int HEADER_LENGTH = 16;
    private static final int SEQUENCE_ID = 1;

    private static final int PACKET_LENGTH_OFFSET = 0;
    private static final int PROTOCOL_VERSION_OFFSET = 6;
    private static final int OPERATION_OFFSET = 8;
    private static final int BODY_OFFSET = 16;

    private static final int JSON_PROTOCOL_VERSION = 0;
    private static final int POPULAR_PROTOCOL_VERSION = 1;
    private static final int BUFFER_PROTOCOL_VERSION = 2;

    private static final int HEART_BEAT_OPERATION = 2;
    private static final int POPULAR_OPERATION = 3;
    private static final int MESSAGE_OPERATION = 5;
    private static final int ENTER_ROOM_OPERATION = 7;

    private final Gson gson;

    private final BilibiliConfig config;

    public BilibiliSite(BilibiliConfig config) {
        this.config = config;
        this.gson = new GsonBuilder().registerTypeAdapter(String.class, new MessageDeserializer(config)).create();
    }

    @Override
    public String getUri() {
        return URI;
    }

    @Override
    public void initMessage(WebSocketClient client) {
        int id = RoomId.getRealRoomId(config.getRoom().getId());
        if (id == -1) {
            MinecraftForge.EVENT_BUS.post(new SendDanmakuEvent("房间获取失败！请检查是否输入错误，或者网络有问题"));
        } else {
            MinecraftForge.EVENT_BUS.post(new SendDanmakuEvent("房间获取成功！正在连接弹幕！"));
        }
        byte[] message = String.format("{\"roomid\": %d}", id).getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(HEADER_LENGTH + message.length);
        buf.writeShort(HEADER_LENGTH);
        buf.writeShort(BUFFER_PROTOCOL_VERSION);
        buf.writeInt(ENTER_ROOM_OPERATION);
        buf.writeInt(SEQUENCE_ID);
        buf.writeBytes(message);
        client.sendMessage(buf);
    }

    @Override
    public long getHeartBeatInterval() {
        return HEART_BEAT_INTERVAL;
    }

    @Override
    public ByteBuf getHeartBeat() {
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(HEADER_LENGTH);
        buf.writeShort(HEADER_LENGTH);
        buf.writeShort(BUFFER_PROTOCOL_VERSION);
        buf.writeInt(HEART_BEAT_OPERATION);
        buf.writeInt(SEQUENCE_ID);
        return buf;
    }

    @Override
    public void handMessage(WebSocketFrame webSocketFrame) throws Exception {
        if (webSocketFrame instanceof BinaryWebSocketFrame) {
            ByteBuf data = webSocketFrame.content();
            int protocol = data.getShort(PROTOCOL_VERSION_OFFSET);
            switch (protocol) {
                case JSON_PROTOCOL_VERSION:
                    return;
                case POPULAR_PROTOCOL_VERSION:
                    //handPopularMessage(data);
                    return;
                case BUFFER_PROTOCOL_VERSION:
                    handBufferMessage(data);
                    return;
                default:
            }
        }
    }

//    private void handJsonMessage(ByteBuf data) {
//        int packetLength = data.getInt(PACKET_LENGTH_OFFSET);
//        CharSequence message = data.getCharSequence(BODY_OFFSET, packetLength - BODY_OFFSET, StandardCharsets.UTF_8);
//        BakaDanmaku.LOGGER.info(message);
//    }

    private void handPopularMessage(ByteBuf data) {
    }

    private void handBufferMessage(ByteBuf data) throws Exception {
        int packetLength = data.getInt(PACKET_LENGTH_OFFSET);
        int operation = data.getInt(OPERATION_OFFSET);

        if (operation == POPULAR_OPERATION) {
            MinecraftForge.EVENT_BUS.post(new UpdatePopularInfoEvent(data.getInt(BODY_OFFSET)));
            return;
        }

        if (operation == MESSAGE_OPERATION) {
            byte[] uncompressedData = new byte[packetLength - BODY_OFFSET];
            data.getBytes(BODY_OFFSET, uncompressedData);
            byte[] decompressData = Zlib.decompress(uncompressedData);
            byte[] msgBytes = Arrays.copyOfRange(decompressData, BODY_OFFSET, decompressData.length);
            String[] message = BilibiliMsgSplit.split(IOUtils.toString(msgBytes, StandardCharsets.UTF_8.toString()));
            for (String msg : message) {
                handStringMessage(msg);
            }
        }
    }

    private void handStringMessage(String message) {
        try {
            String str = gson.fromJson(message, String.class);
            if (str != null) {
                MinecraftForge.EVENT_BUS.post(new SendDanmakuEvent(str));
            }
        } catch (JsonSyntaxException ignore) {
        }
    }

    @Override
    public BilibiliConfig getConfig() {
        return config;
    }
}
