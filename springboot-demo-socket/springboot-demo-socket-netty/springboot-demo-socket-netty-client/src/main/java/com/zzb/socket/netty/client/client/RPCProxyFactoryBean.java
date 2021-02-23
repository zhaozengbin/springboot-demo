package com.zzb.socket.netty.client.client;

import cn.hutool.log.Log;
import com.zzb.socket.netty.client.rpc.util.ChannelUtil;
import com.zzb.socket.netty.client.rpc.util.WrapMethodUtils;
import com.zzb.socket.netty.core.MethodInvokeMeta;
import com.zzb.socket.netty.core.exception.ErrorParamsException;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 类名称：RPCProxyFactoryBean
 * 类描述：JDK动态代理类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:46 上午
 * 修改备注：TODO
 */
public class RPCProxyFactoryBean extends AbstractFactoryBean<Object> implements InvocationHandler {

    private static final Log LOG = Log.get(RPCProxyFactoryBean.class);
    /**
     * 远程服务接口
     */
    private Class interfaceClass;

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    /**
     * 方法：createInstance
     * 描述：创建实例的方法
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : java.lang.Object 由工厂创建的实例
     * @date: 2021年01月18日 10:46 上午
     */
    @Override
    protected Object createInstance() {

        LOG.info("[代理工厂] 初始化代理Bean : {}", interfaceClass);
        // 返回代理类
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, this);
    }

    /**
     * 方法：invoke
     * 描述：动态调用方法的方法 该方法不会显示调用
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param proxy  : 被代理的实例
     * @param method : 调用的方法
     * @param args   : 参数列表
     * @return : java.lang.Object
     * @date: 2021年01月18日 10:47 上午
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws ErrorParamsException {

        LOG.info("{} -> [准备进行远程服务调用] ", this.getClass().getName());
        LOG.info("{} -> [封装调用信息] ", this.getClass().getName());
        final MethodInvokeMeta methodInvokeMeta = WrapMethodUtils.readMethod(interfaceClass, method, args);
        LOG.info("{} -> [远程服务调用封装完毕] 调用接口 -> {}\n调用方法 -> {}\n参数列表 -> {} \n 参数类型 -> {}" +
                        "\n 返回值类型 -> {}", this.getClass().getName(), methodInvokeMeta.getInterfaceClass(), methodInvokeMeta.getMethodName()
                , methodInvokeMeta.getArgs(), methodInvokeMeta.getParameterTypes(), methodInvokeMeta.getReturnType());
        // 构造一个时间戳
        String uuid = System.currentTimeMillis() + UUID.randomUUID().toString();
        // 真正开始使用netty进行通信的方法
        ChannelUtil.remoteCall(methodInvokeMeta, uuid);
        Object result;
        do {
            // 接收返回信息
            result = ChannelUtil.getResult(uuid);
        } while (result == null);
        // 服务器有可能返回异常信息，所以在这里可以进行异常处理
        if (result instanceof ErrorParamsException) {
            throw (ErrorParamsException) result;
        }
        return result;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

}