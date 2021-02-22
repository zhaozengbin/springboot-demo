package com.zzb.netty.server.time.demo3;

import cn.hutool.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 类名称：TimeServerHandler
 * 类描述：针对网络事件进行读写操作的类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:57 下午
 * 修改备注：TODO
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private static final Log LOGGER = Log.get(TimeServerHandler.class);
    /**
     * 模拟粘包/拆包问题计数器
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    /**
     * 方法：channelRead
     * 描述：读事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ctx : ChannelHandlerContext
     * @param msg : 消息
     * @return : void
     * @date: 2021年01月15日 6:58 下午
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // region 解决模拟粘包/拆包问题相关代码
        String body = (String) msg;
        LOGGER.info("--- [接收到的数据] {} | [counter] {}", body, ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        // endregion

        // 异步发送应答消息给Client
        ctx.writeAndFlush(resp); // --> 将消息放到发送缓冲数组中
    }
}
