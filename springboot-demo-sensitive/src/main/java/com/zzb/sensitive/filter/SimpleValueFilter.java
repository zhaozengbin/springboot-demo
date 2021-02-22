package com.zzb.sensitive.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.zzb.sensitive.aop.DesensitizationAspect;
import com.zzb.sensitive.aop.ann.DesensitizationParamAnn;
import com.zzb.sensitive.aop.ann.DesensitizationParamsAnn;
import com.zzb.sensitive.enmu.EHandleType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * fastJson序列化时进行脱敏示例
 */
@Slf4j
@Deprecated
public class SimpleValueFilter implements ValueFilter {
    /**
     * 方法：SimpleValueFilter
     * 描述：fastJson字符串匹配
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @date: 2020年12月15日 11:57 上午
     * @param desensitizationController :
     * @return : null
     */
    private final Map<String, DesensitizationParamAnn> map = new HashMap<>();

    public SimpleValueFilter(DesensitizationParamsAnn desensitizationController) {
        for (DesensitizationParamAnn desensitization : desensitizationController.value()) {
            if (desensitization == null || desensitization.mode() != EHandleType.DEFAULT) {
                continue;
            }
            String[] key = desensitization.fields();
            for (String k : key) {
                map.put(k, desensitization);
            }
        }
    }

    @Override
    public Object process(Object object, String name, Object value) {
        if (!(value instanceof String)) {
            return value;
        }
        String valueStr = (String) value;
        DesensitizationParamAnn sensitiveInfo = map.get(name);
        return DesensitizationAspect.handlerDesensitization(sensitiveInfo, valueStr);
    }
}