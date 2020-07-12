package com.alibaba.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //举例说明buffer的使用
        //创建一个buffer
        //c创建一个buffer，大小5，可以放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
//        intBuffer.put(10);
//        intBuffer.put(11);
//        intBuffer.put(12);
//        intBuffer.put(13);
//        intBuffer.put(14);
        for(int i = 0; i< intBuffer.capacity(); i++){
            intBuffer.put(i*2);
        }
        //切换读写
        intBuffer.flip();

        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
