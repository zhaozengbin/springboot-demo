package com.zzb.netty.server.websocket;

import cn.hutool.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * 类名称：WebSocketServer
 * 类描述：webSocket服务器
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 7:00 下午
 * 修改备注：TODO
 */
public class WebSocketServer {

    private static final Log LOG = Log.get(WebSocketServer.class);

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketChildHandler());
            Channel ch = bootstrap.bind(port).sync().channel();
            LOG.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                    "\t├ [服务器启动]: {}\n" +
                    "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", port);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
