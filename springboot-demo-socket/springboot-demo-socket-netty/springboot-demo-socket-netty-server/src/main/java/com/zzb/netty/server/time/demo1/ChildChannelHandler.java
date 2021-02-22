package com.zzb.netty.server.time.demo1;

import cn.hutool.log.Log;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 类名称：ChildChannelHandler
 * 类描述： I/O事件处理类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:37 下午
 * 修改备注：TODO
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    private static final Log LOGGER = Log.get(ChildChannelHandler.class);

    /**
     * 创建NioSocketChannel成功之后，进行初始化时，
     * 将ChannelHandler设置到ChannelPipeline中，
     * 同样，用于处理网络I/O事件
     *
     * @param ch
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        LOGGER.info("--- [通道初始化]");
        ch.pipeline().addLast(new TimeServerHandler());
    }
}