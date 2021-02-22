package com.zzb.monitor.core.config;

import com.zzb.monitor.core.config.listener.Cache2kExceptionCacheListener;
import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class Cache2kExceptionConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Cache2kExceptionCacheListener cache2kExceptionCacheListener;

    @Bean("exceptionaChe2kCacheManager")
    @Primary
    public SpringCache2kCacheManager cache2kCacheManager() {
        SpringCache2kCacheManager springCache2kCacheManager = new SpringCache2kCacheManager("springtesting-" + applicationContext.getId());
        springCache2kCacheManager.addCaches(b ->
                b.name("cache2kException")
                        .eternal(true)
                        .entryCapacity(100)
                        .expireAfterWrite(10, TimeUnit.SECONDS)
                        .addListener(cache2kExceptionCacheListener));
        return springCache2kCacheManager;
    }

    @Bean("exceptionache2kCache")
    public Cache exceptionache2kCache() {
        SpringCache2kCacheManager springCache2kCacheManager = cache2kCacheManager();
        return springCache2kCacheManager.getCache("cache2kException");
    }

}
