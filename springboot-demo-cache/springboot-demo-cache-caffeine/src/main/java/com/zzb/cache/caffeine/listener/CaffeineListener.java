package com.zzb.cache.caffeine.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CaffeineListener<K, V> implements CacheWriter<K, V> {
    private static final Log LOG = Log.get(CaffeineListener.class);
    private static final String LOG_TEMPLATE = "[%s] -> %s";


    @Override
    public void write(@NonNull K k, @NonNull V v) {
        listenerWrite(k, v);
    }

    @Override
    public void delete(@NonNull K k, @Nullable V v, @NonNull RemovalCause removalCause) {
        this.listenerDelete(k, v, removalCause);
    }

    protected void listenerWrite(K k, V v) {
        LOG.info(String.format(LOG_TEMPLATE, "listenerExpired", JSON.toJSONString(CollUtil.newArrayList(k, v))));
    }


    protected void listenerDelete(K k, V v, RemovalCause removalCause) {
        LOG.info(String.format(LOG_TEMPLATE, "listenerExpired", JSON.toJSONString(CollUtil.newArrayList(k, v, removalCause))));
    }

}
