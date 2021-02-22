package com.zzb.monitor.chain.db.dao;

import com.zzb.monitor.chain.db.entity.MethodNode;

import java.util.List;

/**
 * 类名称：MethodNodeDao
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:17 下午
 * 修改备注：TODO
 */
public interface MethodNodeDao {

    MethodNode findAllById(Long id);

    List<MethodNode> findAllByGuid(String guid);

    boolean save(MethodNode methodNode);

    boolean removeByGuid(String guid);

    List<MethodNode> findAll();

    List<MethodNode> findAllByMethodId(String methonId);

    List<MethodNode> findAllByMethodName(String methodName);

    List<MethodNode> findAllByFullName(String fullName);

    List<MethodNode> findAllByFullName(String className, String methodName);
}
