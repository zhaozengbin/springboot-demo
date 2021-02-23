package com.zzb.cache.cache2k.listener;

import org.cache2k.Cache;
import org.cache2k.CacheEntry;
import org.cache2k.event.*;
import org.springframework.stereotype.Component;

@Component
public abstract class Cache2kListener<K, V> implements CacheEntryExpiredListener<K, V>, CacheEntryCreatedListener<K, V>, CacheEntryUpdatedListener<K, V>, CacheEntryRemovedListener<K, V>, CacheEntryEvictedListener<K, V> {

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

    protected abstract void listenerExpired(Cache<K, V> cache, CacheEntry<K, V> cacheEntry);

    protected abstract void listenerEvicted(Cache<K, V> cache, CacheEntry<K, V> cacheEntry);

    protected abstract void listenerRemoved(Cache<K, V> cache, CacheEntry<K, V> cacheEntry);

    protected abstract void listenerCreated(Cache<K, V> cache, CacheEntry<K, V> cacheEntry);

    protected abstract void listenerUpdated(Cache<K, V> cache, CacheEntry<K, V> cacheEntry, CacheEntry<K, V> cacheEntry1);
}
