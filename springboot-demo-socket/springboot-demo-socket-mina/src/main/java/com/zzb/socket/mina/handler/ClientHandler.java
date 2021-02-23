package com.zzb.socket.mina.handler;

import cn.hutool.log.Log;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * 类名称：ClientHandler
 * 类描述：客户端Handler
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/19 6:03 下午
 * 修改备注：TODO
 */
public class ClientHandler extends IoHandlerAdapter {
    private static final Log LOG = Log.get(ClientHandler.class);

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String address = session.getRemoteAddress().toString();
        LOG.warn("客户端收到[" + address + "]消息：" + message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
    }
}
