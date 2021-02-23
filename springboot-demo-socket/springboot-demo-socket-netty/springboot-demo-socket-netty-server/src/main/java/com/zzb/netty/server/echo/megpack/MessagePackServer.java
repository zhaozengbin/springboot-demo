package com.zzb.netty.server.echo.megpack;

import cn.hutool.log.Log;
import com.zzb.netty.server.MsgPackDecoder;
import com.zzb.netty.server.MsgPackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 类名称：MessagePackServer
 * 类描述：EchoServer服务端
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:36 下午
 * 修改备注：TODO
 */
public class MessagePackServer {

    private static final Log LOG = Log.get(MessagePackServer.class);


    public void bind(int port) throws Exception {
        LOG.info("--- [绑定端口] {}", port);
        // 声明Boss线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 声明Worker线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            LOG.info("--- [启动NIO] ");
            // Netty用于启动NIO服务端的辅助启动类，目的是降低服务端的开发复杂度
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 将两个NIO线程组传递到ServerBootStrap中
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
//                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            ch.pipeline().addLast(new MsgPackDecoder());
//                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast(new MsgPackEncoder());
                            ch.pipeline().addLast(new MessagePackServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
