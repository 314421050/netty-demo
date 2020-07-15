package com.alibaba.netty.groupchat;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author shenbin
 * @date 2020/7/15 14:28
 */
public class GroupChatServer {
    private int port;
    public GroupChatServer(int port){
        this.port = port;
    }
    //编写run方法，处理客户端的请求
    public void run() throws InterruptedException {
        //创建两个线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();//8个NioEventLoop
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //获取到pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        //加入解码器
                        pipeline.addLast(new StringDecoder());
                        //加入编码器
                        pipeline.addLast(new StringEncoder());
                        //加入自己的业务处理handler
                        pipeline.addLast(new GroupChatServerHandler());
                    }
                });
            System.out.println("netty start");
            ChannelFuture channelFuture = b.bind(port).sync();
            //监听关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(7000).run();
    }
}
