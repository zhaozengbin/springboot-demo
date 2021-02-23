package com.zzb.socket.mina.handler;

import cn.hutool.log.Log;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.concurrent.ExecutionException;

/**
 * 类名称：ServerHandler
 * 类描述：socket服务器端处理类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/19 6:04 下午
 * 修改备注：TODO
 */
public class ServerHandler extends IoHandlerAdapter {
    private static final Log LOG = Log.get(ServerHandler.class);

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        LOG.error("出现异常 :" + session.getRemoteAddress().toString() + " : " + cause.toString());
        session.write("出现异常");
        session.closeNow();
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        LOG.info("连接创建 : " + session.getRemoteAddress().toString());
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        LOG.info("连接打开: " + session.getRemoteAddress().toString());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String address = session.getLocalAddress().toString();
        LOG.info("服务器[" + address + "]接受到数据 :" + String.valueOf(message));
        String text = String.valueOf(message);
        LOG.info("数据业务处理开始...... ");
        String result = analyzeData(text, session);
        LOG.info("数据业务处理结束...... ");
        session.write(result);
    }

    private String analyzeData(String text, IoSession session) throws InterruptedException, ExecutionException {
        String address = session.getLocalAddress().toString();
        String responseMessage = "test".equals(text) ? "1111" : "收到啦！";
        return responseMessage;
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        LOG.info("返回客户端消息 : " + message);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if (status == IdleStatus.READER_IDLE) {
            LOG.info("进入读空闲状态");
            session.closeNow();
        } else if (status == IdleStatus.BOTH_IDLE) {
            LOG.info("BOTH空闲");
            session.closeNow();
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        LOG.info("连接关闭 : " + session.getRemoteAddress().toString());
        int size = session.getService().getManagedSessions().values().size();
        LOG.info("连接关闭时session数量==》" + size);
    }

}
