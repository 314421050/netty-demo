package com.alibaba.netty.heartbeat;


import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author shenbin
 * @date 2020/7/15 15:13
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        /**
                         * 加入一个netty提供 IdleStateHandler
                         * 说明.
                         * 1、IdleStateHandler是netty提供的处理空闲状态的处理器
                         * 2、long readerIdleTime 表示多长时间没读，就会发送一个心跳包检测是否连接
                         * 3、long writerIdleTime 表示多长时间没写，就会发送一个心跳包检测是否连接
                         * 4、long allIdleTime 表示多长时间没有读写，就会发送一个心跳包检测是否连接
                         * 5、文档说明
                         * 6、当IdleStateEvent触发后，就会传递给管道的下一个handler去处理
                         * 通过调用下一个handler的userEventTiggered，在该方法中去处理IdleStateEvent（读空闲，写空闲，读写空闲）
                         *
                         */
                        pipeline.addLast(new IdleStateHandler(13, 5, 2, TimeUnit.SECONDS));
                        //加入一个对空闲检测进一步处理的handler 自定义
                        pipeline.addLast(new MyServerHandler());
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
