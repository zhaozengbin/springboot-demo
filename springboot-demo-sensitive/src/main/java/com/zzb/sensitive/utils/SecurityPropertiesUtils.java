package com.zzb.sensitive.utils;

import com.zzb.sensitive.config.properties.SecurityProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 类名称：SecurityPropertiesUtils
 * 类描述：脱敏配置读取类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:32 上午
 * 修改备注：TODO
 */
@Data
@Component
public class SecurityPropertiesUtils {

    private static SecurityProperties INSTANCE;

    public static SecurityProperties getInstance() {
        if (INSTANCE == null) {
            throw new IllegalArgumentException("SensitiveProperties is Undefined");
        }
        return INSTANCE;
    }

    @Autowired
    public void setSensitiveProperties(SecurityProperties sensitiveProperties) {
        INSTANCE = sensitiveProperties;
    }
}
