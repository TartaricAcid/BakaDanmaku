package com.github.tartaricacid.bakadanmaku.websocket;

import com.github.tartaricacid.bakadanmaku.BakaDanmaku;
import com.github.tartaricacid.bakadanmaku.site.ISite;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslHandler;
import org.apache.http.conn.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketClient {
    public static final ScheduledExecutorService SERVICE = Executors.newSingleThreadScheduledExecutor();
    private final ISite site;
    private final URI uri;
    private Channel channel;

    public WebSocketClient(ISite site) {
        this.site = site;
        this.uri = URI.create(site.getUri());
    }

    public void open() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        SSLContext sslContext = new SSLContextBuilder().build();
        EventLoopGroup group = new NioEventLoopGroup();
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,
                null, false, HttpHeaders.EMPTY_HEADERS);
        WebSocketClientHandler handler = new WebSocketClientHandler(handshaker, site);

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipe = ch.pipeline();
                        SSLEngine sslEngine = sslContext.createSSLEngine(uri.getHost(), uri.getPort());
                        sslEngine.setUseClientMode(true);
                        SslHandler sslHandler = new SslHandler(sslEngine);
                        pipe.addLast(
                                sslHandler,
                                new HttpClientCodec(),
                                new HttpObjectAggregator(8192),
                                handler
                        );
                    }
                });

        channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
        handler.handshakeFuture().sync();

        site.initMessage(this);
        BakaDanmaku.HEART_BEAT_TASK = SERVICE.scheduleAtFixedRate(() -> sendMessage(site.getHeartBeat()),
                site.getHeartBeatInterval(), site.getHeartBeatInterval(), TimeUnit.MILLISECONDS);
    }

    public void close() throws InterruptedException {
        channel.writeAndFlush(new CloseWebSocketFrame());
        channel.closeFuture().sync();
        BakaDanmaku.HEART_BEAT_TASK.cancel(true);
    }

    public void sendMessage(final ByteBuf binaryData) {
        channel.writeAndFlush(new BinaryWebSocketFrame(binaryData));
    }
}
