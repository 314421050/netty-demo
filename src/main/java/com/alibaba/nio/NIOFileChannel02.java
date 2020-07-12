package com.alibaba.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {
        File file = new File("f:\\file01.txt");
        //创建一个输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        //通过fileInputStream获取fileCHannel-》实际类型FIlechannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //讲通道的数据读入buffer
        fileChannel.read(byteBuffer);
        //转换成string
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
