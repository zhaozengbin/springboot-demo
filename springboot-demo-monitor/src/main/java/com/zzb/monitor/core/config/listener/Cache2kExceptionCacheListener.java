package com.zzb.monitor.core.config.listener;

import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.zzb.core.utils.DingdingUtils;
import com.zzb.core.utils.SessionUtils;
import com.zzb.monitor.chain.db.service.MethodNodeService;
import com.zzb.monitor.core.config.properties.MonitorProperties;
import com.zzb.monitor.core.entity.ExceptionInfoEntity;
import com.zzb.monitor.report.text.markdown.MarkdownUtils;
import org.cache2k.Cache;
import org.cache2k.CacheEntry;
import org.cache2k.event.CacheEntryCreatedListener;
import org.cache2k.event.CacheEntryExpiredListener;
import org.cache2k.event.CacheEntryUpdatedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class Cache2kExceptionCacheListener implements CacheEntryExpiredListener, CacheEntryCreatedListener, CacheEntryUpdatedListener {
    private final static Log LOG = Log.get(CaffeineExceptionCacheListener.class);


    @Override
    public void onEntryExpired(Cache cache, CacheEntry cacheEntry) throws Exception {
        LOG.info("onEntryExpired cache -> :" + JSON.toJSONString(cache) + "cacheEntry -> :" + JSON.toJSONString(cacheEntry));
    }

    @Override
    public void onEntryCreated(Cache cache, CacheEntry cacheEntry) throws Exception {
        Set<ExceptionInfoEntity> exceptionInfoEntitySet = (Set) cacheEntry.getValue();
        ExceptionInfoEntity exceptionInfoEntity = exceptionInfoEntitySet.stream().findFirst().get();

        if (exceptionInfoEntity.getStackTraceElementList().size() >= MonitorProperties.getExceptionThreshold(exceptionInfoEntity.getExceptionName())) {
            String exName = exceptionInfoEntity.getExceptionName();
            if (cache.keys().size() >= MonitorProperties.getExceptionThreshold(exName)) {
                //开始发送警告
                String msg = MarkdownUtils.exceptionMarkdown(exName, exceptionInfoEntitySet);
                DingdingUtils.send("a456b3ae219e760b021509bbdec37ed62c56d5d33ff6345c71d1fc26f0ec3f29",
                        "SECa26d9f5128da05852c4ba432ecf4e0d2dbdbc5ff5037ac88b2f042889cf2c04f",
                        msg,
                        false,
                        "13466663949");
                LOG.info(msg);
            }
        }
    }

    @Override
    public void onEntryUpdated(Cache cache, CacheEntry cacheEntry, CacheEntry cacheEntry1) throws Exception {
        Set<ExceptionInfoEntity> exceptionInfoEntitySet = (Set) cacheEntry.getValue();
        ExceptionInfoEntity exceptionInfoEntity = exceptionInfoEntitySet.stream().findFirst().get();

        if (exceptionInfoEntity.getStackTraceElementList().size() >= MonitorProperties.getExceptionThreshold(exceptionInfoEntity.getExceptionName())) {
            String exName = exceptionInfoEntity.getExceptionName();
            //开始发送警告
            String msg = MarkdownUtils.exceptionMarkdown(exName, exceptionInfoEntitySet);
            DingdingUtils.send("a456b3ae219e760b021509bbdec37ed62c56d5d33ff6345c71d1fc26f0ec3f29",
                    "SECa26d9f5128da05852c4ba432ecf4e0d2dbdbc5ff5037ac88b2f042889cf2c04f",
                    msg, false,
                    "13466663949");
            LOG.info(msg);
        }
    }
}
