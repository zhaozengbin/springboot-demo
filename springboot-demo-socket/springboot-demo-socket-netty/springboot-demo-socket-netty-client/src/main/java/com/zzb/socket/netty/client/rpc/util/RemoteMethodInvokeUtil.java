package com.zzb.socket.netty.client.rpc.util;

import com.zzb.socket.netty.core.MethodInvokeMeta;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类名称：RemoteMethodInvokeUtil
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:55 上午
 * 修改备注：TODO
 */
public class RemoteMethodInvokeUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public Object processMethod(MethodInvokeMeta methodInvokeMeta) throws InvocationTargetException, IllegalAccessException {
        Class interfaceClass = methodInvokeMeta.getInterfaceClass();
        Object bean = applicationContext.getBean(interfaceClass);
        Method[] declaredMethods = interfaceClass.getDeclaredMethods();
        Method method = null;
        for (Method declaredMethod : declaredMethods) {
            if (methodInvokeMeta.getMethodName().equals(declaredMethod.getName())) {
                method = declaredMethod;
            }
        }
        Object invoke = method.invoke(bean, methodInvokeMeta.getArgs());
        return invoke;
    }

    @Override
    public void setApplicationContext(ApplicationContext app) throws BeansException {
        applicationContext = app;
    }
}
