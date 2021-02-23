package com.zzb.socket.websocket.server;

import cn.hutool.log.Log;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类名称：WebSocketServer
 * 类描述：websocket服务端,多例的，一次websocket连接对应一个实例 @ServerEndpoint 注解的值为URI, 映射客户端输入的URL来连接到WebSocket服务器端
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 1:37 下午
 * 修改备注：TODO
 */
@ServerEndpoint("/ws/{name}")
public class WebSocketServer {
    private static final Log LOG = Log.get(WebSocketServer.class);
    /**
     * 用来记录当前在线连接数。设计成线程安全的。
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /**
     * 用于保存uri对应的连接服务，{uri:WebSocketServer}，设计成线程安全的
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketServerMAP = new ConcurrentHashMap<>();

    private Session session;// 与某个客户端的连接会话，需要通过它来给客户端发送数据

    private String name; //客户端消息发送者

    private String uri; //连接的uri

    /**
     * 方法：onOpen
     * 描述：连接建立成功时触发，绑定参数
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param session : 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     * @param name    :
     * @param toName  :
     * @return : void
     * @date: 2021年01月15日 1:42 下午
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name, @PathParam("toName") String toName) throws IOException {
        this.session = session;
        this.name = name;
        this.uri = session.getRequestURI().toString();
        WebSocketServer webSocketServer = webSocketServerMAP.get(uri);
        if (webSocketServer != null) { //同样业务的连接已经在线，则把原来的挤下线。
            webSocketServer.session.getBasicRemote().sendText(uri + "重复连接被挤下线了");
            webSocketServer.session.close();//关闭连接，触发关闭连接方法onClose()
        }
        webSocketServerMAP.put(uri, this);//保存uri对应的连接服务
        addOnlineCount(); // 在线数加1

    }

    /**
     * 方法：onClose
     * 描述：连接关闭时触发，注意不能向客户端发送消息了
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2021年01月15日 1:42 下午
     */
    @OnClose
    public void onClose() throws IOException {
        webSocketServerMAP.remove(uri);//删除uri对应的连接服务
        reduceOnlineCount(); // 在线数减1
    }

    /**
     * 方法：onMessage
     * 描述：收到客户端消息后触发
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param message :
     * @return : void
     * @date: 2021年01月15日 1:43 下午
     */
    @OnMessage
    public void onMessage(String message) {
        LOG.info("收到消息：" + message);
    }

    /**
     * 方法：onError
     * 描述：通信发生错误时触发
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param session :
     * @param error   :
     * @return : void
     * @date: 2021年01月15日 1:43 下午
     */
    @OnError
    public void onError(Session session, Throwable error) {
        try {
            LOG.info("{}:通信发生错误，连接关闭", name);
            webSocketServerMAP.remove(uri);//删除uri对应的连接服务
        } catch (Exception e) {
        }
    }

    /**
     * 方法：sendMessage
     * 描述：发送消息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param message :
     * @return : void
     * @date: 2021年01月15日 1:51 下午
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 类名称：WebSocketServer
     * 类描述：群发自定义消息
     * 创建人：赵增斌
     * 修改人：赵增斌
     * 修改时间：2021/1/15 1:51 下午
     * 修改备注：TODO
     */
    public void sendInfo(String message) throws IOException {

        for (WebSocketServer item : webSocketServerMAP.values()) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 方法：getOnlineCount
     * 描述：获取在线连接数
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : int
     * @date: 2021年01月15日 1:43 下午
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 方法：addOnlineCount
     * 描述：原子性操作，在线连接数加一
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2021年01月15日 1:43 下午
     */
    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    /**
     * 方法：reduceOnlineCount
     * 描述：原子性操作，在线连接数减一
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2021年01月15日 1:44 下午
     */
    public static void reduceOnlineCount() {
        onlineCount.getAndDecrement();
    }
}
