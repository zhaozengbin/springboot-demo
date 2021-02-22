package com.zzb.netty.server.time.demo1;

import cn.hutool.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * 类名称：TimeServerHandler
 * 类描述：针对网络事件进行读写操作的类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:39 下午
 * 修改备注：TODO
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private static final Log LOGGER = Log.get(TimeServerHandler.class);
    /**
     * 模拟粘包/拆包问题计数器
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 方法：channelRead
     * 描述：读事件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ctx :
     * @param msg :
     * @return : void
     * @date: 2021年01月15日 6:39 下午
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将消息转换成ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        // 获取缓冲区中的字节数
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        LOGGER.info("--- [接收到的数据] {}", body);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() :
                "BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        // region 模拟粘包/拆包问题相关代码
//        String body = new String(req, "utf-8").substring(0, req.length - System.getProperty("line.separator").length());
//        LOGGER.info("--- [接收到的数据] {} | [counter] {}", body, ++counter);
//        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
//        currentTime = currentTime + System.getProperty("line.separator");
//        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        // endregion


        // 异步发送应答消息给Client
        ctx.write(resp); // --> 将消息放到发送缓冲数组中
    }

    /**
     * 方法：channelReadComplete
     * 描述：读完之后
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ctx :
     * @return : void
     * @date: 2021年01月15日 6:40 下午
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("--- [服务器写消息] ");
        // 将消息发送队列中的消息写到SocketChannel中
        ctx.flush(); // --> 将消息写到 SocketChannel 中
    }

}
