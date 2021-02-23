package com.zzb.socket.netty.client.client;

import cn.hutool.log.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 类名称：NettyClient
 * 类描述：netty客户端第二个版本
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:44 上午
 * 修改备注：TODO
 */
public class NettyClient {

    private static final Log LOG = Log.get(NettyClient.class);

    private static int retry = 0;
    /**
     * 初始化Bootstrap实例， 此实例是netty客户端应用开发的入口
     */
    private Bootstrap bootstrap;
    /**
     * 工人线程组
     */
    private EventLoopGroup worker;
    /**
     * 远程端口
     */
    private int port;
    /**
     * 远程服务器url
     */
    private String url;
    /**
     * 默认重连机制为10次
     */
    private int MAX_RETRY_TIMES = 10;

    public NettyClient(int port, String url) {
        this.port = port;
        this.url = url;
        bootstrap = new Bootstrap();
        worker = new NioEventLoopGroup();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
    }

    public void start() {
        LOG.info("{} -> [启动连接] {}:{}", this.getClass().getName(), url, port);
        bootstrap.handler(new NettyClientHandler());
        ChannelFuture f = bootstrap.connect(url, port);
        try {
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            retry++;
            if (retry > MAX_RETRY_TIMES) {
                throw new RuntimeException("调用Wrong");
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                LOG.info("第{}次尝试....失败", retry);
                start();
            }
        }
    }
}
