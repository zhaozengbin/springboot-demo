package com.zzb.sensitive.aop;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.JsonPath;
import com.zzb.sensitive.aop.ann.HyposensitizationParamAnn;
import com.zzb.sensitive.aop.ann.HyposensitizationParamsAnn;
import com.zzb.sensitive.enmu.EHandleType;
import com.zzb.sensitive.undo.UndoObserved;
import com.zzb.sensitive.undo.UndoObservedUtils;
import com.zzb.sensitive.undo.UndoVO;
import com.zzb.sensitive.utils.SecurityPropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zzb.sensitive.utils.SensitiveInfoUtils.CONF;
import static java.util.stream.Collectors.toList;

/**
 * 类名称：HyposensitizationAspect
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 12:11 下午
 * 修改备注：TODO
 */
@Order(1)
@Slf4j
@Aspect
@Component
public class HyposensitizationAspect {


    /**
     * 方法：methodCachePointcut
     * 描述：用于定位寻找注解
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : void
     * @date: 2020年12月15日 12:45 下午
     */
    @Pointcut("@annotation(com.zzb.sensitive.aop.ann.HyposensitizationParamsAnn)")
    public void methodCachePointcut() {

    }

    @Around("methodCachePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //如果未定义填充监听,不执行相关操作
        if (!UndoObserved.isObserver()) {
            return joinPoint.proceed();
        }
        if (!SecurityPropertiesUtils.getInstance().isEnableUndoFilter()) {
            return joinPoint.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        HyposensitizationParamsAnn params = method.getAnnotation(HyposensitizationParamsAnn.class);
        if (params == null) {
            return joinPoint.proceed();
        }
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return joinPoint.proceed();
        }
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames == null || parameterNames.length == 0) {
            return joinPoint.proceed();
        }
        List<HyposensitizationParamAnn> ls = Arrays.stream(params.value()).collect(toList());
        for (HyposensitizationParamAnn item : ls) {
            if (item.argName().isEmpty()) {
                UndoVO vo = convertVO(item, args[0]);
                if (vo == null) {
                    continue;
                }
                UndoObservedUtils.getInstance().sendResult(vo);
                continue;
            }
            UndoVO vo = convertVO(item, queryArg(item.argName(), args, parameterNames));
            if (vo == null) {
                continue;
            }
            UndoObservedUtils.getInstance().sendResult(vo);
        }
        return joinPoint.proceed();
    }

    /**
     * 方法：queryArg
     * 描述：获取入参
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key   :
     * @param args  :
     * @param names :
     * @return : java.lang.Object
     * @date: 2020年12月15日 12:46 下午
     */
    private Object queryArg(String key, Object[] args, String[] names) {

        if (key == null || args == null || names == null || args.length != names.length) {
            return null;
        }
        for (int i = 0, len = names.length; i < len; i++) {
            if (key.equals(names[i])) {
                return args[i];
            }
        }
        return null;
    }


    /**
     * 方法：convertVO
     * 描述：UndoVO对象转换
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param param :
     * @param obj   :
     * @return : com.zzb.sensitive.undo.UndoVO
     * @date: 2020年12月15日 12:46 下午
     */
    private UndoVO convertVO(HyposensitizationParamAnn param, Object obj) {

        if (param.fields().length == 0) {
            return null;
        }
        if (obj == null) {
            return null;
        }
        UndoVO vo = new UndoVO();
        vo.setArgName(param.argName())
            .setType(param.type())
            .setFields(param.fields())
            .setMode(param.mode())
            .setObj(obj);
        if (param.fields().length == 1 && param.fields()[0].isEmpty()) {
            return vo;
        }
        String[] fs = param.fields();
        if (param.mode() == EHandleType.DEFAULT) {
            fs = Arrays.stream(fs).map(m -> String.format("$..%s", m)).toArray(String[]::new);
        }
        List<String> regs = new ArrayList<>();
        for (String key : fs) {
            List<String> path = JsonPath.using(CONF).parse(JSON.toJSONString(obj)).read(key);
            if (path == null || path.isEmpty()) {
                continue;
            }
            regs.addAll(path);
        }
        vo.setRegFields(regs);
        return vo;
    }
}
