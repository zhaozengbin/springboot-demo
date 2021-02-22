package com.zzb.socket.netty.client.rpc.util;


import com.zzb.socket.netty.core.MethodInvokeMeta;

import java.lang.reflect.Method;

/**
 * 类名称：WrapMethodUtils
 * 类描述：封装接口调用的工具
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:56 上午
 * 修改备注：TODO
 */
public class WrapMethodUtils {

    /**
     * 方法：readMethod
     * 描述：封装 method 的元数据信息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param interfaceClass : 接口类
     * @param method         : 方法
     * @param args           : 参数列表
     * @return : com.zzb.socket.netty.core.MethodInvokeMeta 封装的对象
     * @date: 2021年01月18日 10:56 上午
     */
    public static MethodInvokeMeta readMethod(Class interfaceClass, Method method, Object[] args) {
        MethodInvokeMeta methodInvokeMeta = new MethodInvokeMeta();
        methodInvokeMeta.setInterfaceClass(interfaceClass);
        methodInvokeMeta.setArgs(args);
        methodInvokeMeta.setMethodName(method.getName());
        methodInvokeMeta.setReturnType(method.getReturnType());
        Class<?>[] parameterTypes = method.getParameterTypes();
        methodInvokeMeta.setParameterTypes(parameterTypes);
        return methodInvokeMeta;
    }
}
