package com.zzb.monitor.core.config.listener;

import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.cache2k.Cache;
import org.cache2k.CacheEntry;
import org.cache2k.event.CacheEntryCreatedListener;
import org.cache2k.event.CacheEntryExpiredListener;
import org.cache2k.event.CacheEntryUpdatedListener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaffeineExceptionCacheListener implements CacheWriter {
    private final static Log LOG = Log.get(CaffeineExceptionCacheListener.class);

    @Override
    public void write(@NonNull Object o, @NonNull Object o2) {
        LOG.info(" write " + JSON.toJSONString(o) + " ---> " + JSON.toJSONString(o2));
    }

    @Override
    public void delete(@NonNull Object o, @Nullable Object o2, @NonNull RemovalCause removalCause) {
        LOG.info(" delete " + JSON.toJSONString(o) + " ---> " + JSON.toJSONString(o2) + " ---> " + JSON.toJSONString(removalCause));
    }
}
