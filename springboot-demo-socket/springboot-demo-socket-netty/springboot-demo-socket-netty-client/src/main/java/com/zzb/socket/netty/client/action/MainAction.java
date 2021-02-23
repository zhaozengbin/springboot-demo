package com.zzb.socket.netty.client.action;

import cn.hutool.log.Log;
import com.zzb.socket.netty.client.rpc.service.DemoService;
import com.zzb.socket.netty.core.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 类名称：MainAction
 * 类描述：主要用来进行模拟测试的类.就不用写接口来进行测试了
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:41 上午
 * 修改备注：TODO
 */
@Component
public class MainAction {

    private static final Log LOG = Log.get(MainAction.class);

    /**
     * 测试业务
     */
    @Resource
    private DemoService demoService;

    /**
     * 方法：call
     * 描述：真正远程调用的方法
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2021年01月18日 10:41 上午
     */
    public void call() throws InterruptedException {
        // 用于模拟服务器正常启动后，人工调用远程服务代码
        Thread.sleep(10 * 1000);
        LOG.warn("[准备进行业务测试]");
        LOG.warn("[rpc测试] ");
        int sum = demoService.sum(5, 8);
        LOG.warn("[rpc测试结果] {}", sum);
        LOG.warn("[字符串测试] ");
        String print = demoService.print();
        LOG.warn("[字符串测试结果] {}", print);
        LOG.warn("[对象测试] ");
        User userInfo = demoService.getUserInfo();
        LOG.warn("[对象测试结果] {}", userInfo);
        LOG.warn("[异常测试]");
        try {
            double division = demoService.division(3, 0);
            LOG.warn("[异常测试结果] {}", division);
        } catch (Exception e) {
            LOG.error("[服务器异常] {}", e.getMessage());
        }
    }
}
