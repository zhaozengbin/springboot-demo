package com.zzb.sensitive.entity;

import com.zzb.sensitive.aop.ann.SensitiveInfoAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionDecodeFieldAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionEncodeFieldAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionEntityAnn;
import com.zzb.sensitive.aop.ann.encryption.EncryptionFieldAnn;
import com.zzb.sensitive.enmu.EEncryptionType;
import com.zzb.sensitive.enmu.ESensitiveType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@EncryptionEntityAnn
public class SensitiveEntity {
    @SensitiveInfoAnn(value = ESensitiveType.CHINESE_NAME)
    String name;

    @SensitiveInfoAnn(value = ESensitiveType.ID_CARD)
    String idCard;

    @SensitiveInfoAnn(regExp = "(?<=\\w{3})\\w(?=\\w{4})")
    String idCard2;

    @SensitiveInfoAnn(value = ESensitiveType.MOBILE_PHONE)
    String phone;

    @SensitiveInfoAnn(value = ESensitiveType.FIXED_PHONE)
    String ext;

    @SensitiveInfoAnn(value = ESensitiveType.ADDRESS)
    String address;

    @SensitiveInfoAnn(value = ESensitiveType.NULL)
    String address2;

    // 622260000027736298837
    @SensitiveInfoAnn(value = ESensitiveType.BANK_CARD)
    @EncryptionFieldAnn(isNeedEencryption = true, name = "bankCard", type = String.class, encodeFieldAnn = {
        @EncryptionEncodeFieldAnn(value = EEncryptionType.AES, name = "bankCardAES"),
        @EncryptionEncodeFieldAnn(value = EEncryptionType.DES_EDE, name = "bankCardDES"),
        @EncryptionEncodeFieldAnn(value = EEncryptionType.SM4, name = "bankCardSM4")
    })
    String bankCard;

    // AES 0e7117ac86a61717ec32dedee99ded569f86f180ca342d5f4c190dfb73fdcf47
    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.AES))
    String bankCardAES;

    // DES 7bc1bda5d3ccb323ce2da7d65eac846526fae65692cb0218
    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.DES_EDE))
    String bankCardDES;

    // SM4 227ca8e357eb4c44291dc83aee7d3ffc55ceffc0213974669a8cb728125f171e
    @EncryptionFieldAnn(isNeedEencryption = false, name = "bankCard", type = String.class, decodeFieldAnn = @EncryptionDecodeFieldAnn(value = EEncryptionType.SM4))
    String bankCardSM4;

    @SensitiveInfoAnn(value = ESensitiveType.NULL)
    char id;

    public SensitiveEntity(String name, String idCard, String idCard2, String phone, String ext, String address, String address2, String bankCard) {
        this.name = name;
        this.idCard = idCard;
        this.idCard2 = idCard2;
        this.phone = phone;
        this.ext = ext;
        this.address = address;
        this.address2 = address2;
        this.bankCard = bankCard;
    }
}
