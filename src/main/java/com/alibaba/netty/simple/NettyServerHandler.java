package com.alibaba.netty.simple;

import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * 1.我们自定义一个Handler需要继承netty规定好的某个HandlerAdapter（规范）
 * 2.这时我们自定义一个handler，才能称为一个handler
 * @author shenbin
 * @date 2020/7/14 18:33
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 读取数据实际（这里我们可以读取客户端发送的消息）
     * 1.ChannelHandlerContext ctx上下文对象，含有管道pipeline，通道channel，地址
     * 2.Object msg 就是客户端发送的数据，默认object
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程"+Thread.currentThread().getName());
        System.out.println("server ctx"+ctx);
        System.out.println("看看channel和pipeline的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链接，出站入站
        //将msg转成一个ByteBuf
        //ByteBuf是Netty提供的，不是nio的ByteBuffer
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("客户端发送的消息是"+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址是"+channel.remoteAddress());


        /**
         * 比如这里我们有一个非常耗时的业务-》异步执行-》提交该channel对应的NIOEventLoop的taskqueue中
         * 解决方案1 用户程序自定义的普通任务
         */
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client2", CharsetUtil.UTF_8));
                System.out.println("channel code"+ctx.channel().hashCode());
            }catch (Exception e){
                System.out.println("发生异常"+e.getMessage());
            }
        });
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client3", CharsetUtil.UTF_8));
                System.out.println("channel code"+ctx.channel().hashCode());
            }catch (Exception e){
                System.out.println("发生异常"+e.getMessage());
            }
        });
        /**
         * 解决方案2 用户自定义定时任务-》该任务提交到scheduledTaskQueue中
         */
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client4", CharsetUtil.UTF_8));
                System.out.println("channel code"+ctx.channel().hashCode());
            }catch (Exception e){
                System.out.println("发生异常"+e.getMessage());
            }
        }, 5, TimeUnit.SECONDS);
        System.out.println("go on ...");

    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write+flush
        //将数据写入到缓存，并刷新
        //对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client",CharsetUtil.UTF_8));
    }

    //异常处理关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
