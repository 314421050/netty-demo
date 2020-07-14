package com.alibaba.nio.simple;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 1.我们自定义一个Handler需要继承netty规定好的某个HandlerAdapter（规范）
 * 2.这时我们自定义一个handler，才能称为一个handler
 * @author shenbin
 * @date 2020/7/14 18:33
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 1.ChannelHandlerContext ctx上下文对象，含有管道pipeline，通道channel，地址
     * 2.Object msg 就是客户端发送的数据，默认object
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
