package com.zzb.socket.netty.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 类名称：MethodInvokeMeta
 * 类描述：记录调用方法的元信息
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:31 下午
 * 修改备注：TODO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class MethodInvokeMeta implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 接口
     */
    private Class<?> interfaceClass;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数
     */
    private Object[] args;
    /**
     * 返回值类型
     */
    private Class<?> returnType;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }
}
