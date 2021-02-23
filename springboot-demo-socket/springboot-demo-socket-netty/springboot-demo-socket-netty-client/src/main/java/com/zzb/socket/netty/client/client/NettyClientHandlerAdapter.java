package com.zzb.socket.netty.client.client;

import com.zzb.socket.netty.client.rpc.util.ChannelUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：NettyClientHandlerAdapter
 * 类描述：自定义的NettyClientHandler
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:45 上午
 * 修改备注：TODO
 */
public class NettyClientHandlerAdapter extends ChannelHandlerAdapter {


    /**
     * NettyClientHandlerAdapter 日志输出
     */
    private static final Logger LOG = LoggerFactory.getLogger(NettyClientHandlerAdapter.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.error("{} -> [通道异常] {}", this.getClass().getName(), ctx.channel().id());
        ChannelUtil.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        LOG.info("{} -> [连接建立成功] {}", this.getClass().getName(), channelHandlerContext.channel().id());
        // 注册通道
        ChannelUtil.registerChannel(channelHandlerContext.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof Exception)) {
            LOG.info("{} -> [客户端收到的消息] {}", this.getClass().getName(), msg);
        }
        String longText = ctx.channel().id().asLongText();
        String resultKey = ChannelUtil.getResultKey(longText);
        ChannelUtil.calculateResult(resultKey, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LOG.info("{} -> [客户端消息接收完毕] {}", this.getClass().getName(), ctx.channel().id());
        super.channelReadComplete(ctx);
        boolean active = ctx.channel().isActive();
        LOG.info("{} -> [此时通道状态] {}", this.getClass().getName(), active);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOG.info("{} -> [客户端心跳监测发送] 通道编号：{}", this.getClass().getName(), ctx.channel().id());
        if (evt instanceof IdleStateEvent) {
            ctx.writeAndFlush("ping-pong-ping-pong");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        LOG.info("{} -> [关闭通道] {}", this.getClass().getName(), ctx.channel().id());
        super.close(ctx, promise);
//        ChannelUtil.remove(ctx.channel());
    }
}