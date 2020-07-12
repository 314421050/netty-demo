package com.alibaba.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 通过多个buffer（数组）
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        //使用ServerSocketChannel和SocketChannel网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        while (true){
            int byteRead = 0;
            while (byteRead<messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead"+byteRead);
                Arrays.asList(byteBuffers).stream().map(buffer->"postion"+buffer.position()).forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).forEach(buffer->buffer.flip());
            //显示
            long byteWirte = 0;
            while (byteWirte < messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWirte += l;
            }
            Arrays.asList(byteBuffers).forEach(buffer->{
                buffer.clear();
            });
            System.out.println("byteRead"+byteRead);
        }
    }
}
