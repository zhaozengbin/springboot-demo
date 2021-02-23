package com.zzb.cache.cache2k.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 类名称：Cache2kProperties
 * 类描述：cache2k配置
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:32 上午
 * 修改备注：TODO
 */
@Data
@Configuration
public class Cache2kProperties {

    @Value("${cache.cache2k.name}")
    private String cacheName;

    /**
     * 是否持久化
     */
    @Value("${cache.cache2k.eternal}")
    private boolean eternal;
    /**
     * 最大条目
     */
    @Value("${cache.cache2k.entryCapacity}")
    private Long entryCapacity;

    /**
     * 生命周期
     */
    @Value("${cache.cache2k.expireAfterWrite}")
    private Long expireAfterWrite;

}
