package com.zzb.monitor.core.config.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zzb.core.utils.PropertiesUtils;
import com.zzb.core.utils.YmlUtils;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 类名称：MonitorProperties
 * 类描述：监控配置类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/20 2:57 下午
 * 修改备注：TODO
 */
@Data
@Component
public class MonitorProperties {
    public static ConcurrentHashMap<Object, Object> exceptionConfig = null;

    private static final String EXCEPTION_KEY = "monitor.exception";

    private static final String DEFAULT_KEY = "root";

    private static final String EXCEPTION_THRESHOLD_KEY = "threshold";

    private static final String EXCEPTION_DINGDING_TOKEN_KEY = "dingding.token";

    private static final String EXCEPTION_DINGDING_SIGN_KEY = "dingding.sign";

    private static final String EXCEPTION_DINGDING_AT_KEY = "dingding.at";

    private static final String EXCEPTION_DINGDING_AT_ALL_KEY = "dingding.atAll";

    private static Properties properties = null;

    static {
        if (ObjectUtil.isEmpty(properties)) {
            properties = PropertiesUtils.getProperties("monitor.properties");
            Set<Map.Entry<Object, Object>> entrySet = null;
            if (ObjectUtil.isNotEmpty(properties)) {
                entrySet = properties.entrySet();
            } else {
                entrySet = YmlUtils.getYmlByFileName("monitor.yml").entrySet();
            }
            if (ObjectUtil.isNotEmpty(entrySet)) {
                Map<Object, Object> configMap = entrySet.stream()
                        .filter(value -> String.valueOf(value.getKey()).startsWith(EXCEPTION_KEY))
                        .collect(Collectors.toMap(Map.Entry<Object, Object>::getKey, Map.Entry<Object, Object>::getValue));
                exceptionConfig = new ConcurrentHashMap<>(configMap);
            }
        }
    }

    public static int getExceptionThreshold(String exception) {
        return getPropertiesValue(exception, EXCEPTION_THRESHOLD_KEY) == null ? Integer.parseInt(getPropertiesValue(DEFAULT_KEY, EXCEPTION_THRESHOLD_KEY)) : Integer.parseInt(getPropertiesValue(exception, EXCEPTION_THRESHOLD_KEY));
    }

    public static String getDingdingToken(String exception) {
        return getPropertiesValue(exception, EXCEPTION_DINGDING_TOKEN_KEY) == null ? getPropertiesValue(DEFAULT_KEY, EXCEPTION_DINGDING_TOKEN_KEY) : getPropertiesValue(exception, EXCEPTION_DINGDING_TOKEN_KEY);
    }

    public static String getDingdingSign(String exception) {
        return getPropertiesValue(exception, EXCEPTION_DINGDING_SIGN_KEY) == null ? getPropertiesValue(DEFAULT_KEY, EXCEPTION_DINGDING_SIGN_KEY) : getPropertiesValue(exception, EXCEPTION_DINGDING_SIGN_KEY);
    }

    public static boolean getDingdingAtAll(String exception) {
        return getPropertiesValue(exception, EXCEPTION_DINGDING_AT_ALL_KEY) == null ? Boolean.getBoolean(getPropertiesValue(DEFAULT_KEY, EXCEPTION_DINGDING_AT_ALL_KEY)) : Boolean.getBoolean(getPropertiesValue(exception, EXCEPTION_DINGDING_AT_ALL_KEY));
    }

    public static List<String> getDingdingAt(String exception) {
        List<String> atList = null;
        String dingdingAt = getPropertiesValue(exception, EXCEPTION_DINGDING_AT_KEY) == null ? getPropertiesValue(DEFAULT_KEY, EXCEPTION_DINGDING_AT_KEY) : getPropertiesValue(exception, EXCEPTION_DINGDING_AT_KEY);
        if (StrUtil.isNotEmpty(dingdingAt)) {
            atList = new ArrayList<>();
            if (dingdingAt.contains(",")) {
                atList.addAll(CollUtil.newArrayList(dingdingAt.split(",")));
            } else {
                atList.add(dingdingAt);
            }
            return atList;
        }
        return atList;
    }

    private static String getPropertiesValue(String exception, String configKey) {
        String key = EXCEPTION_KEY + "." + exception + "." + configKey;
        if (exceptionConfig.containsKey(key)) {
            return String.valueOf(exceptionConfig.get(key));
        }
        return null;
    }

}
