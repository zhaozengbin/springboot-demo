package com.zzb.cache.cache2k.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import org.cache2k.Cache;
import org.cache2k.CacheEntry;
import org.cache2k.event.*;
import org.springframework.stereotype.Component;

@Component
public class Cache2kListener<K, V> implements CacheEntryExpiredListener<K, V>, CacheEntryCreatedListener<K, V>, CacheEntryUpdatedListener<K, V>, CacheEntryRemovedListener<K, V>, CacheEntryEvictedListener<K, V> {
    private static final Log LOG = Log.get(Cache2kListener.class);
    private static final String LOG_TEMPLATE = "[%s] -> %s";

    @Override
    public void onEntryExpired(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) throws Exception {
        this.listenerExpired(cache, cacheEntry);
    }

    @Override
    public void onEntryEvicted(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) throws Exception {
        listenerEvicted(cache, cacheEntry);
    }

    @Override
    public void onEntryRemoved(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) throws Exception {
        listenerRemoved(cache, cacheEntry);
    }

    @Override
    public void onEntryCreated(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) throws Exception {
        this.listenerCreated(cache, cacheEntry);
    }

    @Override
    public void onEntryUpdated(Cache<K, V> cache, CacheEntry<K, V> cacheEntry, CacheEntry<K, V> cacheEntry1) throws Exception {
        this.listenerUpdated(cache, cacheEntry, cacheEntry1);
    }

    protected void listenerExpired(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) {
        LOG.info(String.format(LOG_TEMPLATE, "listenerExpired", JSON.toJSONString(CollUtil.newArrayList(cache, cacheEntry))));
    }

    protected void listenerEvicted(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) {
        LOG.info(String.format(LOG_TEMPLATE, "listenerEvicted", JSON.toJSONString(CollUtil.newArrayList(cache, cacheEntry))));
    }

    protected void listenerRemoved(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) {
        LOG.info(String.format(LOG_TEMPLATE, "listenerEvicted", JSON.toJSONString(CollUtil.newArrayList(cache, cacheEntry))));
    }

    protected void listenerCreated(Cache<K, V> cache, CacheEntry<K, V> cacheEntry) {
        LOG.info(String.format(LOG_TEMPLATE, "listenerEvicted", JSON.toJSONString(CollUtil.newArrayList(cache, cacheEntry))));

    }

    protected void listenerUpdated(Cache<K, V> cache, CacheEntry<K, V> cacheEntry, CacheEntry<K, V> cacheEntry1) {
        LOG.info(String.format(LOG_TEMPLATE, "listenerEvicted", JSON.toJSONString(CollUtil.newArrayList(cache, cacheEntry, cacheEntry1))));
    }
}
