package com.zzb.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zzb.cache.caffeine.config.properties.CaffeineProperties;
import com.zzb.cache.caffeine.listener.CaffeineListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineConfiguration {

    @Autowired
    private CaffeineProperties caffeineProperties;

    @Autowired
    private CaffeineListener caffeineListener;

    /**
     * 方法：caffeineCacheManager
     * 描述：集成方式一
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : org.springframework.cache.CacheManager
     * @date: 2021年02月05日 11:43 上午
     */
    @Bean("caffeineSpringCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(caffeineProperties.getCacheName());
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .writer(caffeineListener)
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(caffeineProperties.getExpireAfterWrite(), TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(caffeineProperties.getInitialCapacity())
                // 缓存的最大条数
                .maximumSize(caffeineProperties.getMaximumSize()));
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
    @Bean("caffeineSpringCache")
    public org.springframework.cache.Cache caffeineSpringCache() {
        return caffeineCacheManager().getCache(caffeineProperties.getCacheName());
    }

    /**
     * 方法：caffeineCache
     * 描述：集成方式二
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : org.springframework.cache.CacheManager
     * @date: 2021年02月05日 11:43 上午
     */
    @Bean("caffeineCache")
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    /**
     * 方法：cacheLoader
     * 描述：集成方式三 - 扩展配置参数 必须要指定这个Bean，refreshAfterWrite=5s这个配置属性才生效
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : com.github.benmanes.caffeine.cache.CacheLoader<java.lang.Object,java.lang.Object>
     * @date: 2021年02月05日 1:48 下午
     */
    @Bean
    @Primary
    public CacheLoader<Object, Object> cacheLoader() {
        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                return null;
            }

            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                return oldValue;
            }
        };

        return cacheLoader;
    }
}
