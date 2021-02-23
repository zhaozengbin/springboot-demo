package com.zzb.netty.server.echo.megpack;

import cn.hutool.log.Log;
import com.zzb.socket.netty.core.User;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * 类名称：MessagePackServerHandler
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:36 下午
 * 修改备注：TODO
 */
public class MessagePackServerHandler extends ChannelHandlerAdapter {

    private static final Log LOG = Log.get(MessagePackServerHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("--- [发生异常] 释放资源: {}", cause.getMessage());
        // todo
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("Server connect success");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        List<User> userInfo = (List<User>) msg;
        LOG.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [接收 ]: {}\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", userInfo);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
