package com.zzb.sensitive.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 类名称：SensitiveProperties
 * 类描述：脱敏启用配置
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:32 上午
 * 修改备注：TODO
 */
@Data
@Component
public class SecurityProperties {

    /**
     * 是否启用fastJson脱敏
     */
    @Value("${sensitive.filter.fast.enable}")
    private boolean enableFastFilter;

    /**
     * 是否启用Jackson脱敏
     */
    @Value("${sensitive.filter.jack.enable}")
    private boolean enableJackFilter;

    /**
     * 是否启用数据填充
     */
    @Value("${sensitive.filter.undo.enable}")
    private boolean enableUndoFilter;

    /**
     * 是否启用加密
     */
    @Value("${sensitive.filter.encryption.enable}")
    private boolean enableEncryptionFilter;

    @Value("${sensitive.filter.encryption.encryption-key.aes}")
    private String encryptionAesKey;

    @Value("${sensitive.filter.encryption.encryption-key.des}")
    private String encryptionDesKey;

    @Value("${sensitive.filter.encryption.encryption-key.sm4}")
    private String encryptionSM4Key;
}
