package com.alibaba.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author shenbin
 * @date 2020/7/14 16:39
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            while (-1 != readCount){
                try {
                    //从SocketChannel中读入数据至指定的ByteBuffer中。
                    readCount = socketChannel.read(byteBuffer);
                } catch (Exception ex){
                    break;
                }
                byteBuffer.rewind();//倒带 position = 0 mark 作废
            }
        }
    }
}
