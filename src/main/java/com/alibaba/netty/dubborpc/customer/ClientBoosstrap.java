package com.alibaba.netty.dubborpc.customer;

import com.alibaba.netty.dubborpc.netty.NettyClient;
import com.alibaba.netty.dubborpc.publicinterface.HelloService;

public class ClientBoosstrap {
    //这里定义协议头
    public static final String providerName = "HelloService#hello#";
    public static void main(String[] args) throws InterruptedException {
        //创建一个消费者
        NettyClient nettyClient = new NettyClient();

        //创建代理对象
        HelloService bean = (HelloService) nettyClient.getBean(HelloService.class, providerName);
        for (;;) {
            Thread.sleep(2*1000);
            //通过代理对象调用服务提供者的方法
            String dubbo = bean.hello("你好dubbo");
            System.out.println("调用结果：" + dubbo);
        }
    }
}
