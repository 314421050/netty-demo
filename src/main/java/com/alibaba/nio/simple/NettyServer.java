package com.alibaba.nio.simple;


import com.alibaba.netty.dubborpc.netty.NettyServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author shenbin
 * @date 2020/7/14 17:48
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 创建BossGroup 和WorkerGroup
         * 说明
         * 1.创建两个线程组bossGroup和WorkerGroup
         * 2.bossGroup只是处理连接请求，真正的和客户端业务，会交给workerGroup完成
         * 3.两个都是无限循环
         * 4.bossGroup和workerGroup含有的子线程（NioEventLoop）的个数默认实际cpu核数*2
         */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象（匿名对象）
                    //给pipeline设置处理器
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyServerHandler());

                    }
                });//给我们的workerGroup的Eventloop对应的管道设置处理器
            System.out.println("服务器ready");
            //绑定一个端口并且同步，生成一个ChannelFuture
            ChannelFuture cf = bootstrap.bind(6668).sync();
            //对关闭通道监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        
    }
}
