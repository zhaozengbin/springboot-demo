package com.zzb.cache.caffeine.service;

import com.zzb.cache.caffeine.entity.UserInfoEntity;

public interface UserInfoEntityService {

    /**
     * 增加用户信息
     *
     * @param userInfoEntity 用户信息
     */
    void addUserInfoEntity(UserInfoEntity userInfoEntity);

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserInfoEntity getByName(Integer id);

    /**
     * 修改用户信息
     *
     * @param userInfoEntity 用户信息
     * @return 用户信息
     */
    UserInfoEntity updateUserInfoEntity(UserInfoEntity userInfoEntity);

    /**
     * 删除用户信息
     *
     * @param id 用户ID
     */
    void deleteById(Integer id);
}
