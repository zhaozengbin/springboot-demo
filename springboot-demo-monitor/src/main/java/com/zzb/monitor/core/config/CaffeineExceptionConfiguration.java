package com.zzb.monitor.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.zzb.monitor.core.config.listener.CaffeineExceptionCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineExceptionConfiguration {

    @Autowired
    private CaffeineExceptionCacheListener caffeineExceptionCacheListener;

    /**
     * 方法：caffeineCacheManager
     * 描述：集成方式一
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : org.springframework.cache.CacheManager
     * @date: 2021年02月05日 11:43 上午
     */
    @Bean("exceptionCaffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("exceptionCaffeineCache");
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .writer(caffeineExceptionCacheListener)
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(2)
                // 缓存的最大条数
                .maximumSize(2));
        return caffeineCacheManager;
    }

    /**
     * 方法：caffeineCache
     * 描述：集成方式二
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : org.springframework.cache.CacheManager
     * @date: 2021年02月05日 11:43 上午
     */
    @Bean("exceptionCaffeineCache")
    public Cache exceptionCaffeineCache() {
        return caffeineCacheManager().getCache("exceptionCaffeineCache");
    }
}
