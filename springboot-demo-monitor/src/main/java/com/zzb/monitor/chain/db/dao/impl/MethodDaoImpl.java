package com.zzb.monitor.chain.db.dao.impl;

import cn.hutool.core.util.StrUtil;
import com.zzb.core.utils.SessionUtils;
import com.zzb.monitor.chain.db.dao.MethodNodeDao;
import com.zzb.monitor.chain.db.entity.MethodNode;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类名称：MethodDaoImpl
 * 类描述：因为使用单例线程池，所以不必加锁。
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:17 下午
 * 修改备注：TODO
 */
@Repository
public class MethodDaoImpl implements MethodNodeDao {
    private final List<MethodNode> methodNodeList = new ArrayList<>();

    @Override
    public MethodNode findAllById(Long id) {
        List<MethodNode> nodes = methodNodeList.stream().filter(m -> m.getId().equals(id)).collect(Collectors.toList());
        return nodes.size() != 0 ? nodes.get(0) : null;
    }

    @Override
    public List<MethodNode> findAllByGuid(String guid) {
        List<MethodNode> nodes = methodNodeList.stream().filter(m ->
                (StrUtil.isNotEmpty(m.getGuid()) && StrUtil.isNotEmpty(guid) && m.getGuid().equals(guid)))
                .collect(Collectors.toList());
        return nodes;
    }

    @Override
    public boolean removeByGuid(String guid) {
        return methodNodeList.removeAll(findAllByGuid(guid));
    }

    @Override
    public boolean save(MethodNode methodNode) {
        methodNode.setId((long) methodNodeList.size());
        return methodNodeList.add(methodNode);
    }

    @Override
    public List<MethodNode> findAll() {
        return methodNodeList;
    }

    @Override
    public List<MethodNode> findAllByMethodId(String methonId) {
        return methodNodeList.stream().filter(m -> m.getMethodId().equals(methonId)).collect(Collectors.toList());
    }

    @Override
    public List<MethodNode> findAllByMethodName(String methodName) {
        return methodNodeList.stream().filter(m -> m.getMethodName().equals(methodName)).collect(Collectors.toList());
    }

    @Override
    public List<MethodNode> findAllByFullName(String fullName) {
        return methodNodeList.stream().filter(m -> m.getFullName().equals(fullName)).collect(Collectors.toList());
    }

    @Override
    public List<MethodNode> findAllByFullName(String className, String methodName) {
        return methodNodeList.stream().filter(m -> (m.getClassName().equals(className) && m.getMethodName().equals(methodName))).collect(Collectors.toList());
    }
}
