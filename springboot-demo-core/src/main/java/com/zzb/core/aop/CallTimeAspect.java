package com.zzb.core.aop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CallTimeAspect {

    private static final Log LOGGER = LogFactory.getLog(CallTimeAspect.class);

    // 一分钟，即1000ms
    private static final long ONE_MINUTE = 1000;

    @Pointcut("@annotation(com.zzb.core.aop.ann.CallTimeAnn)")
    public void operationLog() {
    }

    /**
     * 方法：timeAround
     * 描述：统计方法执行耗时Around环绕通知
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param joinPoint :
     * @return : java.lang.Object
     * @date: 2020年12月02日 2:28 下午
     */
    @Around("operationLog()")
    public Object timeAround(ProceedingJoinPoint joinPoint) {
        // 定义返回对象、得到方法需要的参数
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        try {
            obj = joinPoint.proceed(args);
        } catch (Throwable e) {
            LOGGER.error("统计某方法执行耗时环绕通知出错", e);
        }

        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        // 打印耗时的信息
        this.printExecTime(methodName, startTime, endTime);

        return obj;
    }

    /**
     * 方法：printExecTime
     * 描述：打印方法执行耗时的信息，如果超过了一定的时间，才打印
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param methodName :
     * @param startTime  :
     * @param endTime    :
     * @return : void
     * @date: 2020年12月02日 2:28 下午
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
        if (diffTime > ONE_MINUTE) {
            LOGGER.warn("-----" + methodName + " 方法执行耗时：" + diffTime + " ms");
        }
    }
}
