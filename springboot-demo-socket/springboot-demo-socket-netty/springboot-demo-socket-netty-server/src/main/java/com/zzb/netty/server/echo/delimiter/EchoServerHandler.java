package com.zzb.netty.server.echo.delimiter;

import cn.hutool.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 类名称：EchoServerHandler
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:33 下午
 * 修改备注：TODO
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    private static final Log LOG = Log.get(EchoServerHandler.class);
    /**
     * 计数器
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("--- [发生异常] 释放资源");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        LOG.info("--- [第{}次接收客户端消息] {}", ++counter, body);
        body += "$_$";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }
}
