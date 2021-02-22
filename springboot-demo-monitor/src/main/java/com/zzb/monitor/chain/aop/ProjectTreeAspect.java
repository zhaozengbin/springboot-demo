package com.zzb.monitor.chain.aop;

import cn.hutool.core.util.StrUtil;
import com.zzb.core.utils.SessionUtils;
import com.zzb.monitor.chain.core.around.AroundMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Aspect
public class ProjectTreeAspect {

    //声明切点
    @Pointcut("execution(* com.zzb..*.*(..))" +
            " && !execution(* com.zzb.monitor..*.*(..))" +
            " && !@annotation(org.springframework.context.annotation.Bean)" +
            " && !@annotation(org.aspectj.lang.annotation.Aspect)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        int identify = new SecureRandom().nextInt();
        String sessionId = SessionUtils.getSessionId();
        Object obj = null;
        if (StrUtil.isEmpty(sessionId)) {
            return pjp.proceed();
        } else {
            try {
                AroundMethod.playBeforeMethod(pjp, identify);
                obj = pjp.proceed();
            } catch (Throwable throwable) {
                throw throwable;
            } finally {
                AroundMethod.playAfterMethod(pjp, identify, obj);
            }
            return obj;
        }
    }

}
