package com.alibaba.netty.dubborpc.provider;

import com.alibaba.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {

    public String hello(String mes) {
        System.out.println("收到消息："+mes);
        if(mes != null){
            return "你好客户端，我已经收到你的消息【"+mes+"】";
        }
        return "你好客户端，我已经收到你的消息";
    }
}
