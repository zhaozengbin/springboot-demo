package com.zzb.redis.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisClusterConfiguration {
    @Autowired
    private ConfigRedisCluster configRedisCluster;

    @Bean(name = "clusterRedisConnectionFactory")
    public RedisConnectionFactory connectionFactory() {

        String nodes = configRedisCluster.getClusterNodes();
        List<String> nodeList = new ArrayList<>();
        String[] split = nodes.split(",");
        for (String node : split) {
            nodeList.add(node);
        }

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(configRedisCluster.getMaxTotal());
        config.setMaxIdle(configRedisCluster.getMaxIdle());
        config.setMinIdle(configRedisCluster.getMinIdle());
        config.setMaxWaitMillis(configRedisCluster.getMaxWait());

        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(config)
                .commandTimeout(Duration.ofMillis(configRedisCluster.getTimeout()))
                .build();

        org.springframework.data.redis.connection.RedisClusterConfiguration clusterConfig = new org.springframework.data.redis.connection.RedisClusterConfiguration(nodeList);
        clusterConfig.setMaxRedirects(3);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfig, clientConfig);
//        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;

    }

    @Bean(name = "clusterRedisTemplate")
    public RedisTemplate<String, String> redisTemplate() {

        // 配置redisTemplate
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory());

        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.QuoteFieldNames);
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        fastJsonRedisSerializer.setFastJsonConfig(config);

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        //GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        return redisTemplate;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {

        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.QuoteFieldNames);
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        fastJsonRedisSerializer.setFastJsonConfig(config);

        //GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(fastJsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //redisCacheConfigurationMap.put("city:shard", this.getRedisCacheConfigurationWithTtl(43200));
        //redisCacheConfigurationMap.put("city:route:conf", this.getRedisCacheConfigurationWithTtl(43200));
        //redisCacheConfigurationMap.put("city:node", this.getRedisCacheConfigurationWithTtl(43200));

        return redisCacheConfigurationMap;
    }
}
