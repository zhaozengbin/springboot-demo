package com.zzb.sensitive.aop;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.zzb.sensitive.aop.ann.DesensitizationParamAnn;
import com.zzb.sensitive.aop.ann.DesensitizationParamsAnn;
import com.zzb.sensitive.enmu.EHandleType;
import com.zzb.sensitive.enmu.ESensitiveType;
import com.zzb.sensitive.exception.FastJsonCustomException;
import com.zzb.sensitive.utils.SecurityPropertiesUtils;
import com.zzb.sensitive.utils.SensitiveInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.zzb.sensitive.utils.SensitiveInfoUtils.CONF;

/**
 * 类名称：DesensitizationAspect
 * 类描述：脱敏AOP
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:54 上午
 * 修改备注：TODO
 */
@Slf4j
@Component
@Aspect
public class DesensitizationAspect {
    private static final Log LOG = LogFactory.get(DesensitizationAspect.class);

    @AfterReturning(value = "@annotation(com.zzb.sensitive.aop.ann.DesensitizationParamsAnn)", returning = "returnValue")
    public Object before(JoinPoint joinPoint, Object returnValue) throws Throwable {
        if (!SecurityPropertiesUtils.getInstance().isEnableFastFilter()) {
            return returnValue;
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DesensitizationParamsAnn desensitizationController = AnnotationUtils.getAnnotation(method, DesensitizationParamsAnn.class);
        if (desensitizationController == null) {
            return returnValue;
        }
        List<DesensitizationParamAnn> ds = getDes(desensitizationController);
        try {
            return filterValue(ds, returnValue);
        } catch (Exception e) {
            LOG.error("JSONPathError", e);
            throw new FastJsonCustomException(returnValue);
        }
    }

    /**
     * 方法：getDes
     * 描述：获取所有注解
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param desensitizationController :
     * @return : java.util.List<com.zzb.sensitive.aop.ann.DesensitizationParamAnn>
     * @date: 2020年12月15日 11:55 上午
     */
    private List<DesensitizationParamAnn> getDes(DesensitizationParamsAnn desensitizationController) {
        if (desensitizationController == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(desensitizationController.value()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 方法：getRexExpDes
     * 描述：获取正则相关的注解
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param desensitizationController :
     * @return : java.util.List<com.zzb.sensitive.aop.ann.DesensitizationParamAnn>
     * @date: 2020年12月15日 11:55 上午
     */
    @Deprecated
    private List<DesensitizationParamAnn> getRexExpDes(DesensitizationParamsAnn desensitizationController) {
        if (desensitizationController == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(desensitizationController.value()).map(m -> {
            if (m.mode() == EHandleType.RGE_EXP) {
                return m;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 方法：filterRegExpValue
     * 描述： fastJson正则过滤匹配的值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ds          :
     * @param returnValue :
     * @return : void
     * @date: 2020年12月15日 11:55 上午
     */
    @Deprecated
    private void filterRegExpValue(List<DesensitizationParamAnn> ds, Object returnValue) {
        if (ds == null || ds.isEmpty()) {
            return;
        }
        for (DesensitizationParamAnn item : ds) {
            if (item.mode() != EHandleType.RGE_EXP) {
                continue;
            }
            for (String reg : item.fields()) {
                if (reg == null) {
                    continue;
                }
                String exp = reg;
                if (reg.contains("[%d]")) {
                    exp = reg.replace("[%d]", ".");
                }
                if (!JSONPath.contains(returnValue, exp)) {
                    continue;
                }
                Object os = JSONPath.eval(returnValue, exp);
                if (os instanceof ArrayList) {
                    ArrayList<Object> ns = (ArrayList<Object>) os;
                    if (ns.isEmpty()) {
                        continue;
                    }
                    for (int i = 0; i < ns.size(); i++) {
                        Object tmp = ns.get(i);
                        if (!(tmp instanceof String)) {
                            continue;
                        }
                        String key = String.format(reg, i);
                        String value = (String) tmp;
                        if (JSONPath.contains(returnValue, key)) {
                            JSONPath.set(returnValue, key, handlerDesensitization(item, value));
                        }
                    }
                    continue;
                }
                //非字符串类型,不处理
                if (!(os instanceof String)) {
                    continue;
                }
                String valueStr = (String) os;
                valueStr = handlerDesensitization(item, valueStr);
                JSONPath.set(returnValue, exp, valueStr);
            }
        }
    }

    /**
     * 方法：filterValue
     * 描述：JsonPath和FastJson过滤字段
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param ds :
     * @param t  :
     * @return : T
     * @date: 2020年12月15日 11:55 上午
     */
    private <T> T filterValue(List<DesensitizationParamAnn> ds, T t) {
        if (ds == null || ds.isEmpty()) {
            return t;
        }
        for (DesensitizationParamAnn item : ds) {
            String[] fs = item.fields();
            if (fs.length == 0) {
                continue;
            }
            if (item.mode() == EHandleType.DEFAULT) {
                fs = Arrays.stream(fs).map(m -> String.format("$..%s", m)).toArray(String[]::new);
            }
            for (String key : fs) {
                List<String> path;
                try {
                    path = JsonPath.using(CONF).parse(JSON.toJSONString(t)).read(key);
                } catch (PathNotFoundException err) {
                    log.warn("PathNotFoundExceptionError,{}", key);
                    continue;
                }
                if (path == null || path.isEmpty()) {
                    continue;
                }
                path.forEach(p -> {
                    if (JSONPath.contains(t, p)) {
                        Object value = JSONPath.eval(t, p);
                        //如果是NULL匹配,并且是封装类型,设置为空
                        if (item.type() == ESensitiveType.NULL) {
                            JSONPath.set(t, p, null);
                        } else if (value instanceof String) {
                            JSONPath.set(t, p, handlerDesensitization(item, (String) value));
                        }
                    }
                });
            }
        }
        return t;
    }

    /**
     * 方法：handlerDesensitization
     * 描述：处理单个脱敏
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param sensitiveInfo :
     * @param valueStr      :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:56 上午
     */
    public static String handlerDesensitization(DesensitizationParamAnn sensitiveInfo, String valueStr) {
        if (sensitiveInfo == null) {
            return valueStr;
        }
        try {
            //是NULL类型,直接返回null
            if (sensitiveInfo.type() == ESensitiveType.NULL) {
                return null;
            }
            //有正则优先使用正则
            if (StringUtils.isNotBlank(sensitiveInfo.regExp())) {
                return valueStr.replaceAll(sensitiveInfo.regExp(), sensitiveInfo.regStr());
            }
            switch (sensitiveInfo.type()) {
                case CHINESE_NAME: {
                    return SensitiveInfoUtils.chineseName(valueStr);
                }
                case ID_CARD:
                case MOBILE_PHONE: {
                    return SensitiveInfoUtils.idCardNum(valueStr, sensitiveInfo.idFront(), sensitiveInfo.idBack());
                }
                case FIXED_PHONE: {
                    return SensitiveInfoUtils.fixedPhone(valueStr);
                }
                case PASSWORD: {
                    return SensitiveInfoUtils.password(valueStr);
                }
                case ADDRESS: {
                    return SensitiveInfoUtils.address(valueStr, sensitiveInfo.addSize());
                }
                case EMAIL: {
                    return SensitiveInfoUtils.email(valueStr);
                }
                case BANK_CARD: {
                    return SensitiveInfoUtils.bankCard(valueStr);
                }
                case SHOPS_CODE: {
                    return SensitiveInfoUtils.shopsCode(valueStr);
                }
                default: {
                    return valueStr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueStr;
    }
}