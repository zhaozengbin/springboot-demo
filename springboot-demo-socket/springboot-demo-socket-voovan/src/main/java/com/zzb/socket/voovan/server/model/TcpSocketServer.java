package com.zzb.socket.voovan.server.model;

import com.zzb.socket.voovan.handler.ServerHandler;
import org.voovan.network.filter.StringFilter;
import org.voovan.network.messagesplitter.LineMessageSplitter;
import org.voovan.network.tcp.TcpServerSocket;


public class TcpSocketServer {

    /**
     * 方法：start
     * 描述：TODO
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ip           : 监听地址
     * @param port         : 监听端口
     * @param readTimeOut  : 空闲事件触发时间, 单位: 秒
     * @param sendTimeout  :  发超时时间, 单位: 毫秒
     * @param idleInterval :  超时时间, 单位: 毫秒
     * @return : boolean
     * @date: 2021年01月26日 11:54 上午
     */
    public static boolean start(String host, int port, int readTimeOut, int sendTimeout, int idleInterval) {
        try {
            //构造客户端类实例
            TcpServerSocket socket = new TcpServerSocket(host, port, readTimeOut, sendTimeout, idleInterval);
            //设置业务处理句柄
            socket.handler(new ServerHandler());
            //设置消息过滤器
            socket.filterChain().add(new StringFilter());
            // 设置消息粘包分割器
            socket.messageSplitter(new LineMessageSplitter());
            //启动服务类
            socket.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
