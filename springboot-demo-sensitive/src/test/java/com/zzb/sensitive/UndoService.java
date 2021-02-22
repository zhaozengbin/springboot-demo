package com.zzb.sensitive;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.zzb.sensitive.aop.ann.encryption.EncryptionMethodAnn;
import com.zzb.sensitive.aop.ann.HyposensitizationParamAnn;
import com.zzb.sensitive.aop.ann.HyposensitizationParamsAnn;
import com.zzb.sensitive.enmu.EMethodType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UndoService {

    @HyposensitizationParamsAnn({
        @HyposensitizationParamAnn(type = "card", fields = "address")
    })
    public UserSensitive testAop(UserSensitive userSensitive) {
        System.out.println(JSON.toJSONString(userSensitive));
        return userSensitive;
    }

    @EncryptionMethodAnn(type = EMethodType.READ, isPrintLog = true, isDecode = false)
    public UserEncryption testEncryption(UserEncryption userEncryption) {
        return userEncryption;
    }


    @EncryptionMethodAnn(type = EMethodType.READ, isPrintLog = true, isDecode = false)
    public List<UserEncryption> testEncryptionList(UserEncryption userEncryption) {
        return CollUtil.newArrayList(userEncryption);
    }
}
