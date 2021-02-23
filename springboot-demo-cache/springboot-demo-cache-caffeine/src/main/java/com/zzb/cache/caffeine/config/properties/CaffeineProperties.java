package com.zzb.cache.caffeine.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
@Component
public class CaffeineProperties {

    @Value("caffeine.cache.name")
    private String cacheName;

    @Value("caffeine.cache.initialCapacity")
    private int initialCapacity;

    @Value("caffeine.cache.maximumSize")
    private int maximumSize;

    @Value("caffeine.cache.expireAfterWrite")
    private int expireAfterWrite;

}
