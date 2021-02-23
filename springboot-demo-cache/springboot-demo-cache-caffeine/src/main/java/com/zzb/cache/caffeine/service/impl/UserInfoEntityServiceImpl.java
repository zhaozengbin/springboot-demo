package com.zzb.cache.caffeine.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zzb.cache.caffeine.entity.UserInfoEntity;
import com.zzb.cache.caffeine.service.UserInfoEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@CacheConfig(cacheNames = "caffeineCacheSpringManager")
public class UserInfoEntityServiceImpl implements UserInfoEntityService {

    /**
     * 模拟数据库存储数据
     */
    private HashMap<Integer, UserInfoEntity> userInfoEntityMap = new HashMap<>();

    @Override
    @CachePut(key = "#userInfoEntity.id")
    public void addUserInfoEntity(UserInfoEntity userInfoEntity) {
        log.info("create");
        userInfoEntityMap.put(userInfoEntity.getId(), userInfoEntity);
    }

    @Override
    @Cacheable(key = "#id")
    public UserInfoEntity getByName(Integer id) {
        log.info("get");
        return userInfoEntityMap.get(id);
    }

    @Override
    @CachePut(key = "#userInfoEntity.id")
    public UserInfoEntity updateUserInfoEntity(UserInfoEntity userInfoEntity) {
        log.info("update");
        if (!userInfoEntityMap.containsKey(userInfoEntity.getId())) {
            return null;
        }
        // 取旧的值
        UserInfoEntity oldUserInfoEntity = userInfoEntityMap.get(userInfoEntity.getId());
        // 替换内容
        if (oldUserInfoEntity.getAge() > 0) {
            oldUserInfoEntity.setAge(userInfoEntity.getAge());
        }
        if (!StrUtil.isEmpty(oldUserInfoEntity.getName())) {
            oldUserInfoEntity.setName(userInfoEntity.getName());
        }
        if (!StrUtil.isEmpty(oldUserInfoEntity.getSex())) {
            oldUserInfoEntity.setSex(userInfoEntity.getSex());
        }
        // 将新的对象存储，更新旧对象信息
        userInfoEntityMap.put(oldUserInfoEntity.getId(), oldUserInfoEntity);
        // 返回新对象信息
        return oldUserInfoEntity;
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Integer id) {
        log.info("delete");
        userInfoEntityMap.remove(id);
    }

}
