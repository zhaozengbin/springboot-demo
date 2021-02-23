package com.zzb.cache.caffeine.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 类名称：Cache2KProperties
 * 类描述：caffeine配置
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:32 上午
 * 修改备注：TODO
 */
@Data
@Configuration
public class CaffeineProperties {

    @Value("${cache.caffeine.name}")
    private String cacheName;

    @Value("${cache.caffeine.initialCapacity}")
    private Integer initialCapacity;

    @Value("${cache.caffeine.maximumSize}")
    private Long maximumSize;

    @Value("${cache.caffeine.expireAfterWrite}")
    private Long expireAfterWrite;

}
