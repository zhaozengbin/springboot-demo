package com.zzb.sensitive.enmu;

/**
 * 类名称：EEncryptionType
 * 类描述：方法类型
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/17 10:33 上午
 * 修改备注：TODO
 */
public enum EMethodType {
    /**
     * 默认,匹配key,SM4
     */
    CREATE,
    /**
     * AES算法
     */
    READ,
    /**
     * DES_EDE算法
     */
    UPDATE,
    /**
     * DES_EDE算法
     */
    DELETE;
}
