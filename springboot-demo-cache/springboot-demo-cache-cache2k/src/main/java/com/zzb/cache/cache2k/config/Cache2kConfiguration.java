package com.zzb.cache.cache2k.config;

import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class Cache2kConfiguration {

    @Bean("cache2kCacheManager")
    public SpringCache2kCacheManager cache2kCacheManager() {
        return new SpringCache2kCacheManager();
    }

    @Bean("cache2kCacher")
    public Cache cache2kCacher() {
        return cache2kCacheManager().getCache("cache2k");
    }


}
