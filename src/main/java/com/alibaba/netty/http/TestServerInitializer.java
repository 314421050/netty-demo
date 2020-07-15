package com.alibaba.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author shenbin
 * @date 2020/7/15 11:23
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        ChannelPipeline pipeline = ch.pipeline();

         //加入一个netty提供的httpServerCodec code-》code-》decoder
         //httpServerCodec说明
         //1、httpServerCodec是netty提供的处理http的编-解码器
         pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
         //2、增加一个自定义的handler
         pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
