package com.alibaba.netty.simple;

import com.alibaba.netty.dubborpc.netty.NettyClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author shenbin
 * @date 2020/7/15 10:37
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建客户端启动对象
            //注意客户端使用的不是serverBootstrap而是bootstrap
            Bootstrap bootstrap = new Bootstrap();
            //设置参数
            bootstrap.group(group)
                .channel(NioSocketChannel.class)//设置客户端通道的实现类（反射）
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler());//加入自己的处理器
                    }
                });
            System.out.println("客户端ok");
            //启动客户端去连接服务器
            //关于channelFuture，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
