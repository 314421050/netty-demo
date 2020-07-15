package com.alibaba.netty.websocket;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author shenbin
 * @date 2020/7/15 15:37
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //基于http协议，使用http的编码和解码器
                        pipeline.addLast(new HttpServerCodec());
                        //是以块的方式，添加ChunkedWriteHandler处理器
                        pipeline.addLast(new ChunkedWriteHandler());
                        /**
                         * 说明
                         * 1、http数据在传输过程中是分段，HttpObjectAggregator，就是将多个段聚合
                         * 2、这就是为什么，当浏览器发送大量数据时，就会发送多次http请求
                         */
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        /**
                         * 说明
                         * 1、对应websocket 他的数据是以帧（frame）形式传递
                         * 2、可以看到WebSocketFrame下面有六个子类
                         * 3、浏览器请求时ws://localhost:7000/hello 表示uri请求
                         * 4、WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议，保持长连接
                         * 5、是通过一个状态码101
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("hello2"));
                        //自定义的handler
                        pipeline.addLast(new MyTextWebSocketFrameHandler());
                    }
                });
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
