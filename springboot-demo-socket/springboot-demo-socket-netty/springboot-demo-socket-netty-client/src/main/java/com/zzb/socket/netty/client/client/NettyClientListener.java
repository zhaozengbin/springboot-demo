package com.zzb.socket.netty.client.client;

import cn.hutool.log.Log;
import com.zzb.socket.netty.client.action.MainAction;
import com.zzb.socket.netty.client.config.NettyConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 类名称：NettyClientListener
 * 类描述： netty客户端监听器 主要用于延迟测试RPC和启动NettyClient
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:45 上午
 * 修改备注：TODO
 */
@Order(0)
@Component
public class NettyClientListener implements CommandLineRunner {

    private static final Log LOG = Log.get(NettyClientListener.class);

    /**
     * netty客户端配置
     */
    @Resource
    private NettyConfig nettyConfig;
    /**
     * 主要用于测试RPC场景的类。集成到自己的业务中就不需要此依赖
     */
    @Resource
    private MainAction mainAction;

    @Override
    public void run(String... args) throws Exception {
        LOG.info("{} -> [准备进行与服务端通信]", this.getClass().getName());
        // region 模拟RPC场景
        Thread t1 = new Thread(() -> {
            try {
                mainAction.call();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 使用一个线程模拟Client启动完毕后RPC的场景
        t1.start();
        // endregion
        // 获取服务器监听的端口
        int port = nettyConfig.getPort();
        // 获取服务器IP地址
        String url = nettyConfig.getUrl();
        // 启动NettyClient
        new NettyClient(port, url).start();
    }
}
