package com.zzb.sensitive.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zzb.sensitive.aop.ann.HyposensitizationParamAnn;
import com.zzb.sensitive.aop.ann.HyposensitizationParamsAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionMethodAnn;
import com.zzb.sensitive.enmu.EMethodType;
import com.zzb.sensitive.entity.SensitiveEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensitiveService {

    @HyposensitizationParamsAnn({
            @HyposensitizationParamAnn(type = "card", fields = "address")
    })
    public SensitiveEntity testAop(SensitiveEntity sensitiveEntity) {
        System.out.println(JSON.toJSONString(sensitiveEntity));
        return sensitiveEntity;
    }

    @EncryptionMethodAnn(type = EMethodType.READ, isPrintLog = true, isDecode = false)
    public SensitiveEntity testEncryption(SensitiveEntity sensitiveEntity) {
        return sensitiveEntity;
    }


    @EncryptionMethodAnn(type = EMethodType.READ, isPrintLog = true, isDecode = false)
    public List<SensitiveEntity> testEncryptionList(SensitiveEntity sensitiveEntity) {
        return CollUtil.newArrayList(sensitiveEntity);
    }
}
