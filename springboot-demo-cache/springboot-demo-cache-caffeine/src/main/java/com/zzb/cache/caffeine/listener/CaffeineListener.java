package com.zzb.cache.caffeine.listener;

import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

@Component
public abstract class CaffeineListener<K, V> implements CacheWriter<K, V> {
    @Override
    public void write(@NonNull K k, @NonNull V v) {
        listenerWrite(k, v);
    }

    @Override
    public void delete(@NonNull K k, @Nullable V v, @NonNull RemovalCause removalCause) {
        this.listenerDelete(k, v, removalCause);
    }

    protected abstract void listenerWrite(K k, V v);

    protected abstract void listenerDelete(K k, V v, RemovalCause removalCause);

}
