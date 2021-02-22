package com.zzb.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/redis-cluster.properties", ignoreResourceNotFound = true)
@ConfigurationProperties(prefix = "spring.redis.cache")
public class ConfigRedisCluster {

    private int expireSeconds;

    private String clusterNodes;

    private int commandTimeout;

    /**
     * redis链接最大空闲数
     */
    private Integer maxIdle;

    /**
     * redis最大建立连接等待时间
     */
    private Integer maxWait;

    /**
     * 客户端超时时间单位是毫秒
     */
    private Integer timeout;

    /**
     * 最大连接数, 默认8个
     */
    private Integer maxTotal;

    /**
     * redis最小空闲连接数, 默认0
     */
    private Integer minIdle;

    /**
     * redis在获取连接的时候检查有效性, 默认false
     */
    private Boolean testOnBorrow;

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public int getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
}
