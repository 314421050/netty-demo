package com.alibaba.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author shenbin
 * @date 2020/7/14 16:52
 */
public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.01", 7001));
        String fileName = "protoc-3.6.1-win32.zip";
        //得到一个文件channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long startTime = System.currentTimeMillis();
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("总字节数"+transferCount+"耗时"+(System.currentTimeMillis()-startTime));
        fileChannel.close();
    }
}
