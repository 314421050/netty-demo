package com.alibaba.netty.http;

import com.sun.jndi.toolkit.url.Uri;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 1、SimpleChannelInboundHandler是ChannelInboundHandlerAdapter
 * 2、HttpObject 客户端和服务器相互通讯的数据封装成HttpObject
 * @author shenbin
 * @date 2020/7/15 11:30
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是不是HttpObject类型
        if(msg instanceof HttpObject){
            System.out.println("pipeline hashcode"+ctx.channel().hashCode());
            System.out.println("msg class"+msg.getClass());
            System.out.println("clien address"+ctx.channel().remoteAddress());
            HttpRequest httpRequest = (HttpRequest)msg;
            //获取uri，过滤指定资源
            Uri uri = new Uri(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico,不响应");
                return;
            }
            //回复信息给浏览器 http协议
            ByteBuf content = Unpooled.copiedBuffer("hello i am server", CharsetUtil.UTF_8);
            //构造要给http响应，httpresponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            //返回
            ctx.writeAndFlush(response);
        }

    }
}
