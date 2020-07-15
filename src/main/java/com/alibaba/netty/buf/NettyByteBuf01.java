package com.alibaba.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author shenbin
 * @date 2020/7/15 13:59
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {
        /**
         * 创建一个ByteBuf
         * 说明
         * 1、创建对象，该对象包含一个数组arr，是一个byte[10]
         * 2、在netty 的buffer中，不需要使用flip进行反转
         *      底层维护了readerindex和writerindex
         * 3、通过readerindex和writerindex和capacity，将buffer分成三个区域
         * 0--readerindex已读取的区域
         * readerindex-writerindex，可读取的区域
         * writerindex-capacity，可写的区域
         */
        ByteBuf buffer = Unpooled.buffer(10);
        for(int i = 0; i < 10; i++){
            buffer.writeByte(i);
        }
        System.out.println("capacity="+buffer.capacity());
        for(int i = 0; i < buffer.capacity(); i++){
            System.out.println(buffer.readByte());
        }
        System.out.println("执行完毕");
    }
}
