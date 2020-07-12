package com.alibaba.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello";
        File file = new File("f:\\file01.txt");
        //创建一个输出流-》channel
        FileOutputStream fileOutputStream = new FileOutputStream("f:\\file01.txt");
        //通过fileOutputStream 获取对应的fileChannel
        //这个fileChannel真实类型是fileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        //创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入byteBuffer
        byteBuffer.put(str.getBytes());
        //进行filp
        byteBuffer.flip();
        //将byteBuffer数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
