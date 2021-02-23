package com.zzb.cache.cache2k.config;

import cn.hutool.core.util.StrUtil;
import com.zzb.cache.cache2k.config.properties.Cache2kProperties;
import com.zzb.cache.cache2k.listener.Cache2kListener;
import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class Cache2kConfiguration {

    @Autowired
    private Cache2kProperties cache2kProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Cache2kListener cache2kListener;

    @Bean("cache2kCacheSpringManager")
    public SpringCache2kCacheManager cache2kCacheManager() {
        SpringCache2kCacheManager springCache2kCacheManager = new SpringCache2kCacheManager(
                StrUtil.isEmpty(cache2kProperties.getCacheManagerName()) ? "cache2k-" + applicationContext.getId() : cache2kProperties.getCacheManagerName());
        springCache2kCacheManager.addCaches(b ->
                b.name(cache2kProperties.getCacheName())
                        .eternal(cache2kProperties.isEternal())
                        .entryCapacity(cache2kProperties.getEntryCapacity())
                        .expireAfterWrite(cache2kProperties.getExpireAfterWrite(), TimeUnit.SECONDS)
                        .addListener(cache2kListener));
        return springCache2kCacheManager;
    }

    @Bean("cache2kSpringCacher")
    public Cache cache2kCacher() {
        return cache2kCacheManager().getCache(cache2kProperties.getCacheName());
    }

}
