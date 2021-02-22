package com.zzb.sensitive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zzb.sensitive.aop.ann.DesensitizationParamAnn;
import com.zzb.sensitive.aop.ann.DesensitizationParamsAnn;
import com.zzb.sensitive.aop.ann.HyposensitizationParamAnn;
import com.zzb.sensitive.aop.ann.HyposensitizationParamsAnn;
import com.zzb.sensitive.enmu.EHandleType;
import com.zzb.sensitive.enmu.ESensitiveType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class IndexController {
    @Resource
    UndoService undoService;

    /**
     * 基于fastJson的数据脱敏
     */
    @DesensitizationParamsAnn({
        @DesensitizationParamAnn(type = ESensitiveType.NULL, fields = {"id", "address"}),
        @DesensitizationParamAnn(type = ESensitiveType.MOBILE_PHONE, fields = {"phone", "idCard"}),
        @DesensitizationParamAnn(type = ESensitiveType.BANK_CARD, fields = "$..bankCard", mode = EHandleType.RGE_EXP),
        @DesensitizationParamAnn(regExp = "(?<=\\w{2})\\w(?=\\w{1})", fields = "$[0].idCard2", mode = EHandleType.RGE_EXP)
    })
    @GetMapping("fast")
    public List<UserDesensitization> sensitive() {
        return Arrays.asList(new UserDesensitization(), new UserDesensitization());
    }


    /**
     * 数据回填,不给argName默认取第一个参数
     *
     * @param pt1
     * @param pt2
     */
    @HyposensitizationParamsAnn({
        @HyposensitizationParamAnn(type = "card", fields = "bankCard"),
        @HyposensitizationParamAnn(argName = "a", type = "string"),
        @HyposensitizationParamAnn(argName = "pt0", type = "obj"),
        @HyposensitizationParamAnn(argName = "pt1", type = "phone", fields = {"idCard", "phone"}),
        @HyposensitizationParamAnn(argName = "pt2", type = "reg", fields = {"$..id"}, mode = EHandleType.RGE_EXP)
    })
    @GetMapping("undo")
    public String Hyposensitization(UserDesensitization pt1, UserSensitive pt2
        , String a, SingleObj pt0) {
        return JSON.toJSONString(Arrays.asList(pt1, pt2, a, pt0));
    }


    @GetMapping("encryption")
    public String encryption() {
        UserEncryption userEncryption = new UserEncryption();
        String result = JSON.toJSONString(undoService.testEncryption(userEncryption));
        return result;
    }

    @GetMapping("encryption2")
    public String encryption2() {
        UserEncryption userEncryption = new UserEncryption();
        String result = JSONArray.toJSONString(undoService.testEncryptionList(userEncryption));
        return result;
    }

    /**
     * 基于jackson的数据脱敏
     *
     * @return
     */
    @GetMapping
    public List<UserSensitive> jackson() {
        return Collections.singletonList(new UserSensitive());
    }
}
