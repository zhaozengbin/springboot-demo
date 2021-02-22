package com.zzb.netty.server.time.demo3;

import cn.hutool.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：TimeServer
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:54 下午
 * 修改备注：TODO
 */
public class TimeServer {

    private static final Log LOGGER = Log.get(TimeServer.class);

    /**
     * 方法：bind
     * 描述：绑定端口
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param port : 端口号
     * @return : void
     * @date: 2021年01月15日 6:56 下午
     */
    public void bind(int port) throws Exception {

        LOGGER.info("--- [绑定端口] {}", port);
        // 声明Boss线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 声明Worker线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            LOGGER.info("--- [启动NIO] ");
            // Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 将两个NIO线程组传递到ServerBootStrap中
            bootstrap.group(bossGroup, workerGroup)
                    // NioServerSocketChannel 相当于NIO中的ServerSocketChannel类
                    .channel(NioServerSocketChannel.class)
                    // 配置NioServerSocketChannel的TCP参数 backlog设置为1024
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 绑定I/O事件处理类
                    .childHandler(new ChildChannelHandler());
            // 绑定端口，同步等待成功
            // channelFuture 相当于JDK的java.util.concurrent.Future用于异步操作通知回调
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
