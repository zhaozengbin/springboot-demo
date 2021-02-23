package com.zzb.monitor.core.config.listener;

import cn.hutool.core.collection.CollUtil;
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
import java.util.stream.Collectors;

@Component
public class Cache2kExceptionCacheListener implements CacheEntryExpiredListener, CacheEntryCreatedListener, CacheEntryUpdatedListener {
    private final static Log LOG = Log.get(CaffeineExceptionCacheListener.class);


    @Override
    public void onEntryExpired(Cache cache, CacheEntry cacheEntry) throws Exception {
        LOG.info("onEntryExpired cache -> :" + JSON.toJSONString(cache) + "cacheEntry -> :" + JSON.toJSONString(cacheEntry));
    }

    @Override
    public void onEntryCreated(Cache cache, CacheEntry cacheEntry) throws Exception {
        sendMsg(cacheEntry);
    }

    @Override
    public void onEntryUpdated(Cache cache, CacheEntry cacheEntry, CacheEntry cacheEntry1) throws Exception {
        sendMsg(cacheEntry);
    }

    private void sendMsg(CacheEntry cacheEntry) {
        String exceptionName = (String) cacheEntry.getKey();
        List<ExceptionInfoEntity> exceptionInfoEntityList = (List) cacheEntry.getValue();
        int threshold = MonitorProperties.getExceptionThreshold(exceptionName);
        if (exceptionInfoEntityList.size() < threshold) {
            exceptionInfoEntityList = exceptionInfoEntityList.stream().filter(value -> (value.getExceptionCount() > threshold)).collect(Collectors.toList());
        }
        if (CollUtil.isNotEmpty(exceptionInfoEntityList)) {
            //开始发送警告
            String msg = MarkdownUtils.exceptionMarkdown(exceptionName, threshold, exceptionInfoEntityList);
            DingdingUtils.send(MonitorProperties.getDingdingToken(exceptionName),
                    MonitorProperties.getDingdingSign(exceptionName),
                    MonitorProperties.getDingdingAt(exceptionName),
                    MonitorProperties.getDingdingAtAll(exceptionName),
                    msg);
            LOG.info(msg);
        }
    }
}
