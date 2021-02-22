package com.zzb.monitor.chain.db.service.impl;

import com.zzb.monitor.chain.db.dao.MethodNodeDao;
import com.zzb.monitor.chain.db.entity.MethodNode;
import com.zzb.monitor.chain.db.service.MethodNodeService;
import com.zzb.monitor.chain.db.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类名称：MethodNodeServiceImpl
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:18 下午
 * 修改备注：TODO
 */
@Service
public class MethodNodeServiceImpl implements MethodNodeService {

    @Autowired
    private MethodNodeDao methodNodeDao;

    @Override
    public boolean saveNotRedo(MethodNode methodNode) {
        //根据方法签名查找是否已存在该方法
        List<MethodNode> methodList = methodNodeDao.findAllByMethodId(methodNode.getMethodId());
        //不存在直接保存,若存在调用链不相同也保存
        if (methodList == null || methodList.size() == 0 || !ServiceUtil.analyzeList(methodNode, methodList)) {
            return methodNodeDao.save(methodNode);
        }
        return false;
    }

    @Override
    public List<MethodNode> findAll() {
        return methodNodeDao.findAll();
    }

    @Override
    public MethodNode findAllById(Long id) {
        return methodNodeDao.findAllById(id);
    }

    @Override
    public List<MethodNode> findAllByGuid(String guid) {
        return methodNodeDao.findAllByGuid(guid);
    }

    @Override
    public boolean removeByGuid(String guid) {
        return methodNodeDao.removeByGuid(guid);
    }

    @Override
    public List<MethodNode> findAllByMethodName(String methodName) {
        return methodNodeDao.findAllByMethodName(methodName);
    }

    @Override
    public List<MethodNode> findAllByFullName(String fullName) {
        return methodNodeDao.findAllByFullName(fullName);
    }

    @Override
    public List<MethodNode> findAllByFullName(String className, String methodName) {
        return methodNodeDao.findAllByFullName(className, methodName);
    }
}
