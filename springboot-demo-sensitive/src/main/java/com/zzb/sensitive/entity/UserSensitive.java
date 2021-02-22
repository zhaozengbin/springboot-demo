package com.zzb.sensitive.entity;

import com.zzb.sensitive.aop.ann.SensitiveInfoAnn;
import com.zzb.sensitive.enmu.ESensitiveType;
import lombok.Data;

@Data
public class UserSensitive {

    @SensitiveInfoAnn(value = ESensitiveType.CHINESE_NAME)
    String name = "张三";

    @SensitiveInfoAnn(value = ESensitiveType.ID_CARD)
    String idCard = "430524202012120832";

    @SensitiveInfoAnn(regExp = "(?<=\\w{3})\\w(?=\\w{4})")
    String idCard2 = "430524202012120832";

    @SensitiveInfoAnn(value = ESensitiveType.MOBILE_PHONE)
    String phone = "1234567890";

    @SensitiveInfoAnn(value = ESensitiveType.FIXED_PHONE)
    String ext = "0739-8888888";

    @SensitiveInfoAnn(value = ESensitiveType.ADDRESS)
    String address = "湖南省长沙市高新区岳麓大道芯城科技园";

    @SensitiveInfoAnn(value = ESensitiveType.NULL)
    String address2 = "湖南省";

    @SensitiveInfoAnn(value = ESensitiveType.BANK_CARD)
    String bankCard = "622260000027736298837";

    @SensitiveInfoAnn(value = ESensitiveType.NULL)
    char id = 'c';
}
