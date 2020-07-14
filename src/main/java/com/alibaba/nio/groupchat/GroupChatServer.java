package com.alibaba.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author shenbin
 * @date 2020/7/14 10:30
 */
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;
    //初始化工作
    public GroupChatServer(){
        try {
            //得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            //将该listenchannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen(){
        try {
            while (true){
                int count = selector.select();

                if(count > 0){
                    //遍历得到集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        //监听到
                        if(key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress()+"上线");
                        }
                        if(key.isReadable()){//通道发送read事件，通道是可读的状态
                            readData(key);
                        }
                        //删除,防止重复
                        iterator.remove();
                    }
                }else {
                    System.out.println("等待");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }

    //读取客户端西欧毛线哦
    public void readData(SelectionKey key){
        //取到相关的channel
        SocketChannel channel = null;
        try {
            //得到channel
            channel = (SocketChannel)key.channel();
            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            //根据count的值做处理
            if(count > 0){
                //把缓存区的数据转成字符串
                String msg = new String(byteBuffer.array());
                System.out.println("from client"+msg);
                //想其他客户端转发消息，除去自己
                sendInfoToOtherClients(msg, channel);
            }
            
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress()+"离线了");
                key.cancel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    //想其他客户端转发消息，除去自己
    private void sendInfoToOtherClients(String msg, SocketChannel self) {
        System.out.println("服务器转发消息中");
        try {
            //遍历所有注册到selector的socketchannel，并排除自己
            for(SelectionKey key : selector.keys()){
                //通过key取出对应的socketchannel
                Channel targetChannel = key.channel();
                if(targetChannel instanceof SocketChannel && targetChannel != self){
                    SocketChannel dest = (SocketChannel)targetChannel;
                    //存到buffer
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    dest.write(buffer);

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
