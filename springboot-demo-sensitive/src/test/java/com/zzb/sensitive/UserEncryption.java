package com.zzb.sensitive;

import com.zzb.sensitive.aop.ann.*;
import com.zzb.sensitive.aop.ann.encryption.EncryptionDecodeFieldAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionEncodeFieldAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionEntityAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionFieldAnn;
import com.zzb.sensitive.enmu.EEncryptionType;
import com.zzb.sensitive.enmu.ESensitiveType;
import lombok.Data;

@Data
@EncryptionEntityAnn
public class UserEncryption {

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

    // 622260000027736298837
    @SensitiveInfoAnn(value = ESensitiveType.BANK_CARD)
    @EncryptionFieldAnn(isNeedEencryption = true, name = "bankCard", type = String.class, encodeFieldAnn = {
        @EncryptionEncodeFieldAnn(value = EEncryptionType.AES, name = "bankCardAES"),
        @EncryptionEncodeFieldAnn(value = EEncryptionType.DES_EDE, name = "bankCardDes"),
        @EncryptionEncodeFieldAnn(value = EEncryptionType.SM4, name = "bankCardSM4")
    })
    String bankCard = "622260000027736298837";

    // AES 0e7117ac86a61717ec32dedee99ded569f86f180ca342d5f4c190dfb73fdcf47
    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.AES))
    String bankCardAES = "";

    // DES 7bc1bda5d3ccb323ce2da7d65eac846526fae65692cb0218
    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.DES_EDE))
    String bankCardDes = "";

    // SM4 227ca8e357eb4c44291dc83aee7d3ffc55ceffc0213974669a8cb728125f171e
    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.SM4))
    String bankCardSM4 = "";

    @SensitiveInfoAnn(value = ESensitiveType.NULL)
    char id = 'c';
}
