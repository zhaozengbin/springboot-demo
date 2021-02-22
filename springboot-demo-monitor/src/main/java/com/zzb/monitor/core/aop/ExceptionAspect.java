package com.zzb.monitor.core.aop;

import cn.hutool.core.collection.CollUtil;
import com.zzb.core.utils.SessionUtils;
import com.zzb.monitor.chain.db.entity.MethodNode;
import com.zzb.monitor.chain.db.service.MethodNodeService;
import com.zzb.monitor.core.entity.ExceptionInfoEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
public class ExceptionAspect {

    @Autowired
    @Qualifier("exceptionCaffeineCache")
    private Cache exceptionCaffeineCache;

    @Autowired
    @Qualifier("exceptionache2kCache")
    private Cache exceptionache2kCache;

    @Autowired
    private MethodNodeService methodNodeService;


    @Pointcut(value = "execution(* com.zzb.*.controller..*(..)) " +
            "&& !@annotation(org.aspectj.lang.annotation.Aspect)")
    private void pointcut() {
    }

    /**
     * 方法：handleThrowing
     * 描述：处理异常
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param e : 异常对象
     * @return : void
     * @date: 2021年02月04日 2:35 下午
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void handleThrowing(JoinPoint jp, Exception e) {
        String exceptionName = e.getClass().getName();
        String className = jp.getSignature().getDeclaringTypeName();
        String methodName = jp.getSignature().getName();
        Object[] params = jp.getArgs();
        List<StackTraceElement> list = stackTrace(e, "com.zzb");
        List<MethodNode> methodNodeList = methodNodeService.findAllByFullName(className, methodName);
        ExceptionInfoEntity exceptionInfoEntity = new ExceptionInfoEntity(
                SessionUtils.getSessionId(),
                exceptionName.substring(exceptionName.lastIndexOf(".") + 1),
                list,
                System.currentTimeMillis(),
                className,
                methodName,
                params,
                methodNodeList);
        cache2KCache(exceptionName, exceptionInfoEntity);
//        caffeineCache(exceptionName, exceptionInfoEntity);
    }

    private void caffeineCache(String exceptionName, ExceptionInfoEntity exceptionInfoEntity) {
        cache(exceptionCaffeineCache, exceptionName, exceptionInfoEntity);
    }

    private void cache2KCache(String exceptionName, ExceptionInfoEntity exceptionInfoEntity) {
        cache(exceptionache2kCache, exceptionName, exceptionInfoEntity);
    }


    private void cache(Cache cache, String exceptionName, ExceptionInfoEntity exceptionInfoEntity) {
        Set<ExceptionInfoEntity> exceptionInfoEntitySet = cache.get(exceptionName, HashSet.class);

        if (CollUtil.isEmpty(exceptionInfoEntitySet)) {
            exceptionInfoEntitySet = new HashSet<>();
        } else if (exceptionInfoEntitySet.contains(exceptionInfoEntity)) {
            return;
        }
        exceptionInfoEntitySet.add(exceptionInfoEntity);
        cache.put(exceptionName, exceptionInfoEntitySet);
    }

    private List<StackTraceElement> stackTrace(Throwable throwable, String filterTrace) {
        List<StackTraceElement> list = new LinkedList<StackTraceElement>();
        addStackTrace(list, throwable, filterTrace);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            addStackTrace(list, cause, filterTrace);
            cause = cause.getCause();
        }
        return list;
    }

    private void addStackTrace(List<StackTraceElement> list, Throwable throwable, String filterTrace) {
        list.addAll(0,
                Arrays.stream(throwable.getStackTrace())
                        .filter(x -> filterTrace == null ? true : x.getClassName().startsWith(filterTrace))
                        .filter(x -> !x.getFileName().equals("<generated>")).collect(Collectors.toList()));
    }
}
