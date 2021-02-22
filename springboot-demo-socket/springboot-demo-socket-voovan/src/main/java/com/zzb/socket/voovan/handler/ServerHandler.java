package com.zzb.socket.voovan.handler;

import org.voovan.network.ConnectType;
import org.voovan.network.HeartBeat;
import org.voovan.network.IoHandler;
import org.voovan.network.IoSession;
import org.voovan.tools.log.Logger;

/**
 * 类名称：ServerHandler
 * 类描述：服务端业务句柄类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/26 11:15 上午
 * 修改备注：TODO
 */
public class ServerHandler implements IoHandler {

    /**
     * 方法：onConnect
     * 描述：连接事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param session :
     * @return : java.lang.Object
     * @date: 2021年01月26日 11:12 上午
     */
    @Override
    public Object onConnect(IoSession session) {
        //在日志平台,输出事件名称
        Logger.simple("onConnect");
        if (session.socketContext().getConnectType() == ConnectType.UDP) {
            return "onConnect";
        }
        HeartBeat heartBeat = session.getHeartBeat();
        if (heartBeat == null) {
            heartBeat = HeartBeat.attachSession(session, "PINGq", "PONGq");
        }
        //返回消息,在 onConnect 事件发送消息
        return "onConnect";
    }

    /**
     * 方法：onDisconnect
     * 描述：连接断开事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param session :
     * @return : void
     * @date: 2021年01月26日 11:16 上午
     */
    @Override
    public void onDisconnect(IoSession session) {
        //在日志平台,输出事件名称
        Logger.simple("onDisconnect");
    }

    /**
     * 方法：onReceive
     * 描述：连接接收事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param session :
     * @param obj     :
     * @return : java.lang.Object
     * @date: 2021年01月26日 11:16 上午
     */
    @Override
    public Object onReceive(IoSession session, Object obj) {
        //在日志平台,输出事件名称和接受到的消息内容
        Logger.simple("onRecive: " + obj.toString());
        //输出会话中保存的属性
        Logger.simple("Attribute onRecive: " + session.getAttribute("key"));
        session.close();  //关闭 Socket 连接
        return obj;
    }

    /**
     * 方法：onException
     * 描述：异常事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param session :
     * @param e       :
     * @return : void
     * @date: 2021年01月26日 11:24 上午
     */
    @Override
    public void onException(IoSession session, Exception e) {
        //在日志平台,输出事件名称
        Logger.simple("onException");
        //输入异常栈信息
        e.printStackTrace();
    }

    /**
     * 方法：onIdle
     * 描述：空闲事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ioSession :
     * @return : void
     * @date: 2021年01月26日 11:26 上午
     */
    @Override
    public void onIdle(IoSession ioSession) {
        //在日志平台,输出事件名称
        Logger.simple("onIdle");
        //心跳依赖于 idle 时间,这个参数在构造 socket 的时候设置具体查看 AioSocket
        //服务端和客户端使用了两种不同的心跳绑定方式,这是其中一种
        if (ioSession.socketContext().getConnectType() == ConnectType.UDP) {
            return;
        }
        HeartBeat heartBeat = ioSession.getHeartBeat();

        //心跳一次, 返回 true:本次心跳成功, false: 本次心跳失败
        Logger.simple("HB==>" + HeartBeat.beat(ioSession));
        if (heartBeat.getFailedCount() > 5) {
            ioSession.close();
        }
    }

    /**
     * 方法：onSent
     * 描述：消息发送事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param session :
     * @param obj     :
     * @return : void
     * @date: 2021年01月26日 11:24 上午
     */
    @Override
    public void onSent(IoSession session, Object obj) {
        //byte缓冲区
        String sad = obj.toString();
        //输出事件名称和发送的消息内容
        Logger.simple("onSent: " + sad);
    }

    /**
     * 方法：onFlush
     * 描述：缓冲区发送事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ioSession :
     * @return : void
     * @date: 2021年01月26日 11:26 上午
     */
    @Override
    public void onFlush(IoSession ioSession) {
        //在日志平台,输出事件名称
        Logger.simple("onFlush");
    }

}
