package com.zzb.sensitive.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * 类名称：SensitiveAutoConfiguration
 * 类描述：基本配置类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:18 上午
 * 修改备注：TODO
 */
@Configuration
@ComponentScans({
    @ComponentScan("com.zzb.sensitive")
})
public class SecurityAutoConfiguration {
}
