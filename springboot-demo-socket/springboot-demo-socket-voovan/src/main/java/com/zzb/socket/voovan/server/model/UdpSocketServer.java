package com.zzb.socket.voovan.server.model;

import com.zzb.socket.voovan.handler.ServerHandler;
import org.voovan.network.filter.StringFilter;
import org.voovan.network.messagesplitter.LineMessageSplitter;
import org.voovan.network.udp.UdpServerSocket;

public class UdpSocketServer {
    /**
     * 方法：start
     * 描述：TODO
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param host         : 监听地址
     * @param port         : 监听端口
     * @param readTimeOut  : 空闲事件触发时间, 单位: 秒
     * @param sendTimeout  :  发超时时间, 单位: 毫秒
     * @param idleInterval :  超时时间, 单位: 毫秒
     * @return : boolean
     * @date: 2021年01月26日 11:54 上午
     */
    public static boolean start(String host, int port, int readTimeOut, int sendTimeout, int idleInterval) {
        try {
            UdpServerSocket udpServerSocket = new UdpServerSocket(host, port, readTimeOut, sendTimeout, idleInterval);
            udpServerSocket.messageSplitter(new LineMessageSplitter());
            udpServerSocket.filterChain().add(new StringFilter());
            udpServerSocket.handler(new ServerHandler());
            udpServerSocket.start();
        } catch (Exception e) {
            return true;
        }
        return false;
    }
}
