package com.zzb.netty.server.echo.fixlength;

import cn.hutool.log.Log;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：EchoServerHandler
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:35 下午
 * 修改备注：TODO
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    private static final Log LOG = Log.get(EchoServerHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("--- [发生异常] 释放资源");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info("--- [接收到客户端的数据] {}", msg);
    }
}
