package com.zzb.netty.server.time.demo2;

import cn.hutool.log.Log;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：ChildChannelHandler
 * 类描述：I/O事件处理类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:40 下午
 * 修改备注：TODO
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {


    private static final Log LOGGER = Log.get(ChildChannelHandler.class);

    /**
     * 方法：initChannel
     * 描述：
     * 创建NioSocketChannel成功之后，进行初始化时，
     * 将ChannelHandler设置到ChannelPipeline中，
     * 同样，用于处理网络I/O事件
     * <p>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ch :
     * @return : void
     * @date: 2021年01月15日 6:40 下午
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        LOGGER.info("--- [通道初始化]");
        ch.pipeline().addLast(new TimeServerHandler());
    }
}