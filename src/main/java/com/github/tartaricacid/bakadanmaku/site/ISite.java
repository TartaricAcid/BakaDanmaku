package com.github.tartaricacid.bakadanmaku.site;

import com.github.tartaricacid.bakadanmaku.config.IConfig;
import com.github.tartaricacid.bakadanmaku.websocket.WebSocketClient;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface ISite {
    /**
     * 弹幕机域名
     *
     * @return 域名字符串
     */
    String getUri();

    /**
     * 弹幕机连接后的首次通信
     *
     * @param client WebSocket 客户端
     */
    void initMessage(WebSocketClient client);

    /**
     * 发送心跳包的间隔
     *
     * @return 心跳包间隔时间，毫秒
     */
    long getHeartBeatInterval();

    /**
     * 心跳包
     *
     * @return 心跳包的 ByteBuf
     */
    ByteBuf getHeartBeat();

    /**
     * * 处理收到的信息
     *
     * @param webSocketFrame webSocketFrame websocket 数据
     * @throws Exception 读取数据时发生的异常
     */
    void handMessage(WebSocketFrame webSocketFrame) throws Exception;

    /**
     * 获取此站点的配置文件
     *
     * @return 配置文件
     */
    IConfig getConfig();
}
