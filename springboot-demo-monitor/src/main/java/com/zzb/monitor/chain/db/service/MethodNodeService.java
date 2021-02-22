package com.zzb.monitor.chain.db.service;


import com.zzb.monitor.chain.db.entity.MethodNode;

import java.util.List;

/**
 * 类名称：MethodNodeService
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:19 下午
 * 修改备注：TODO
 */
public interface MethodNodeService {


    boolean saveNotRedo(MethodNode node);

    List<MethodNode> findAll();

    MethodNode findAllById(Long id);

    List<MethodNode> findAllByGuid(String guid);

    boolean removeByGuid(String guid);

    List<MethodNode> findAllByMethodName(String methodName);

    List<MethodNode> findAllByFullName(String fullName);

    List<MethodNode> findAllByFullName(String className, String methodName);
}
