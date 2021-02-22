package com.zzb.sensitive.aop;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zzb.sensitive.aop.ann.encryption.*;
import com.zzb.sensitive.enmu.EEncryptionType;
import com.zzb.sensitive.enmu.EMethodType;
import com.zzb.sensitive.utils.EncryptionInfoUtils;
import com.zzb.sensitive.utils.SecurityPropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * 类名称：EncryptionAspect
 * 类描述：加密aop
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 12:11 下午
 * 修改备注：TODO
 */
@Order(1)
@Slf4j
@Aspect
@Component
public class EncryptionAspect {
    private static final Log LOG = LogFactory.get();
    private boolean isPrintLog = false;

    @Autowired
    private EncryptionInfoUtils encryptionInfoUtils;

    /**
     * 方法：methodCachePointcut
     * 描述：用于定位寻找注解
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年12月15日 12:45 下午
     */
    @Pointcut("@annotation(com.zzb.sensitive.aop.ann.encryption.EncryptionMethodAnn)")
    public void methodCachePointcut() {

    }


    @Before(value = "methodCachePointcut()")
    public void before(JoinPoint joinPoint) {
        Method method = getMethodInfo(joinPoint);
        EncryptionMethodAnn encryptionMethodAnn = method.getAnnotation(EncryptionMethodAnn.class);
        isPrintLog = encryptionMethodAnn.isPrintLog();
        printLog("方法:{} ==> 启动信息加密机制", method.getName());
        if (ObjectUtil.isNotEmpty(method)) {
            if (EMethodType.CREATE.equals(encryptionMethodAnn.type()) ||
                EMethodType.UPDATE.equals(encryptionMethodAnn.type()) ||
                EMethodType.DELETE.equals(encryptionMethodAnn.type())) {
                handleParam(joinPoint, method, (encryptionMethodAnn.isDecode() && true));
            } else {
                handleParam(joinPoint, method, (encryptionMethodAnn.isDecode() && false));
            }
        }
    }

    @AfterReturning(value = "methodCachePointcut()", returning = "rvt")
    public void afterReturning(JoinPoint joinPoint, Object rvt) {
        Method method = getMethodInfo(joinPoint);
        EncryptionMethodAnn encryptionMethodAnn = method.getAnnotation(EncryptionMethodAnn.class);
        isPrintLog = encryptionMethodAnn.isPrintLog();
        printLog("方法:{} ==> 启动信息加密机制", method.getName());

        if (ObjectUtil.isNotEmpty(method)) {
            handleReturn(rvt, method, (encryptionMethodAnn.isDecode() && false));
        }
    }

    /**
     * 方法：getMethodInfo
     * 描述：获取方法信息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param joinPoint :
     * @return : void
     * @date: 2020年12月19日 11:28 上午
     */
    private Method getMethodInfo(JoinPoint joinPoint) {
        if (!SecurityPropertiesUtils.getInstance().isEnableEncryptionFilter()) {
            return null;
        }

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames == null || parameterNames.length == 0) {
            return null;
        }


        return methodSignature.getMethod();
    }

    /**
     * 方法：handleReturn
     * 描述：处理请求信息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param joinPoint :
     * @param method    :
     * @param isDecode  :
     * @return : void
     * @date: 2020年12月19日 11:28 上午
     */
    private void handleParam(JoinPoint joinPoint, Method method, boolean isDecode) {
        if (ObjectUtil.isEmpty(method)) {
            return;
        }
        CollUtil.newArrayList(joinPoint.getArgs()).stream().forEach(arg -> {
            handleEntity(arg, method, isDecode);
        });
    }

    /**
     * 方法：handleReturn
     * 描述：处理返回信息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param object   :
     * @param method   :
     * @param isDecode :
     * @return : void
     * @date: 2020年12月19日 11:28 上午
     */
    private void handleReturn(Object object, Method method, boolean isDecode) {

        if (ObjectUtil.isEmpty(method)) {
            return;
        }
        if (ObjectUtil.isNotEmpty(method)) {
            if (object instanceof Collection) {
                ((Collection<?>) object).forEach(value -> {
                    handleEntity(value, method, isDecode);
                });
            } else if (object.getClass().isArray()) {
                CollUtil.newArrayList(object).forEach(value -> {
                    handleEntity(value, method, isDecode);
                });
            } else {
                handleEntity(object, method, isDecode);
            }
        }
    }

    /**
     * 方法：handleEntity
     * 描述：实体加解码
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param object   :
     * @param method   :
     * @param isDecode :
     * @return : void
     * @date: 2020年12月19日 11:28 上午
     */
    private void handleEntity(Object object, Method method, boolean isDecode) {
        Class<?> clazz = ClassUtil.getClass(object);
        if (clazz.isAnnotationPresent(EncryptionEntityAnn.class)) {
            printLog("方法:{} 参数:{} ==> 符合加密规则", method.getName(), clazz.getTypeName());
            DynaBean dynaBean = DynaBean.create(object);
            Field[] fields = ClassUtil.getDeclaredFields(clazz);
            CollUtil.newArrayList(fields).forEach(field -> {
                if (field.isAnnotationPresent(EncryptionFieldAnn.class)) {
                    EncryptionFieldAnn encryptionFieldAnn = field.getAnnotation(EncryptionFieldAnn.class);
                    if (encryptionFieldAnn.isNeedEencryption()) {
                        encode(encryptionFieldAnn, dynaBean, method.getName(), clazz.getTypeName(), field);
                    } else if (!encryptionFieldAnn.isNeedEencryption()) {
                        if (isDecode) {
                            decode(encryptionFieldAnn, dynaBean, method.getName(), clazz.getTypeName(), field);
                        } else {
                            dynaBean.set(encryptionFieldAnn.name(), null);
                        }
                    }
                }
            });
        }
    }

    /**
     * 方法：decode
     * 描述：字段解密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param encryptionFieldAnn :
     * @param dynaBean           :
     * @param methodName         :
     * @param paramName          :
     * @param field              :
     * @return : void
     * @date: 2020年12月18日 10:47 上午
     */
    private void decode(EncryptionFieldAnn encryptionFieldAnn, DynaBean dynaBean, String methodName, String paramName, Field field) {
        EncryptionDecodeFieldAnn encryptionDecodeFieldAnn = encryptionFieldAnn.decodeFieldAnn();
        EEncryptionType eEncryptionType = encryptionDecodeFieldAnn.value();
        String decodeFieldName = encryptionFieldAnn.name();
        String fieldName = getFieldName(field.getName());
        if (StrUtil.isNotEmpty(decodeFieldName) && !decodeFieldName.equals(fieldName)) {
            Object decodeFieldValue = dynaBean.get(decodeFieldName);
            Object currentFieldValue = dynaBean.get(fieldName);
            if (ObjectUtil.isNotEmpty(currentFieldValue)) {
                Object result = encryptionInfoUtils.decode(eEncryptionType, String.valueOf(currentFieldValue), field.getType());
                if (ObjectUtil.isEmpty(decodeFieldValue)) {
                    if (dynaBean.containsProp(decodeFieldName)) {
                        dynaBean.set(decodeFieldName, result);
                        printLog("方法:{} 参数:{} 属性:{} ==> 为需要解密数据 ==> (解密前:{} -> 解密后:{})", methodName, paramName, decodeFieldName, currentFieldValue, result);

                    } else {
                        throw new UtilException("没有找到属性:{},指定的{}属性", fieldName, decodeFieldName);
                    }
                } else if (!decodeFieldValue.equals(result)) {
                    throw new UtilException("{}解密与原始数据不一致可能加密出现问题 ==> 加密数据 -> {},原始数据 -> {},加密算法 -> {}", fieldName, currentFieldValue, result, eEncryptionType.name());
                }
            }
        }
    }

    /**
     * 方法：decode
     * 描述：字段加密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param encryptionFieldAnn :
     * @param dynaBean           :
     * @param methodName         :
     * @param paramName          :
     * @param field              :
     * @return : void
     * @date: 2020年12月18日 10:47 上午
     */
    private void encode(EncryptionFieldAnn encryptionFieldAnn, DynaBean dynaBean, String methodName, String paramName, Field field) {
        List<EncryptionEncodeFieldAnn> encryptionEncodeFieldAnnList = CollUtil.newArrayList(encryptionFieldAnn.encodeFieldAnn());
        String decodeFieldName = encryptionFieldAnn.name();
        String fieldName = getFieldName(field.getName());
        Object currentFieldValue = dynaBean.get(fieldName);
        if (ObjectUtil.isNotEmpty(currentFieldValue)) {
            if (CollUtil.isNotEmpty(encryptionEncodeFieldAnnList)) {
                encryptionEncodeFieldAnnList.stream().forEach(encryptionEncodeFieldAnn -> {
                    String encodeFieldName = encryptionEncodeFieldAnn.name();
                    EEncryptionType encodeEncryptionType = encryptionEncodeFieldAnn.value();
                    String result = encryptionInfoUtils.encode(encodeEncryptionType, String.valueOf(currentFieldValue));

                    if (StrUtil.isNotEmpty(encodeFieldName) && decodeFieldName.equals(fieldName)) {
                        if (dynaBean.containsProp(encodeFieldName)) {
                            Object encodeFieldValue = dynaBean.get(encodeFieldName);
                            if (ObjectUtil.isEmpty(encodeFieldValue)) {
                                dynaBean.set(encodeFieldName, result);
                                printLog("方法:{} 参数:{} 属性:{} ==> 为需要加密数据 ==> (加密前:{} -> 加密后:{})", methodName, paramName, encodeFieldName, currentFieldValue, result);
                            } else if (!encodeFieldValue.equals(result)) {
                                throw new UtilException("{}解密与原始数据不一致可能加密出现问题 ==> 加密数据 -> {},原始数据 -> {},加密算法 -> {}", fieldName, currentFieldValue, result, encodeEncryptionType.name());
                            }
                        } else {
                            throw new UtilException("没有找到属性:{},指定的{}属性", fieldName, encodeFieldName);
                        }

                    }
                });
            }
        }
    }

    private String getFieldName(String field) {
        return field.substring(field.lastIndexOf(".") + 1);
    }

    private void printLog(String format, Object... arguments) {
        if (isPrintLog) {
            LOG.debug(format, arguments);
        }
    }
}
