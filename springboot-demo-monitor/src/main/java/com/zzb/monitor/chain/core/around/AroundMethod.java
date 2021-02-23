package com.zzb.monitor.chain.core.around;

import cn.hutool.log.Log;
import com.zzb.monitor.chain.core.point.BuriedPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：AroundMethod
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:13 下午
 * 修改备注：TODO
 */
public class AroundMethod {

    private final static Log LOG = Log.get(AroundMethod.class);

    public static void playBeforeMethod(ProceedingJoinPoint pjp, int identify) {
        LOG.debug("AroundMethodBefore " + pjp.getSignature().getDeclaringTypeName() + " " + pjp.getSignature().getName());
        BuriedPoint.before(pjp, identify);
    }

    public static void playAfterMethod(ProceedingJoinPoint pjp, int identify, Object obj) {
        LOG.debug("AroundMethodAfter " + pjp.getSignature().getDeclaringTypeName() + " " + pjp.getSignature().getName());
        BuriedPoint.after(identify, obj != null ? obj.getClass().getName() : null);
    }

}