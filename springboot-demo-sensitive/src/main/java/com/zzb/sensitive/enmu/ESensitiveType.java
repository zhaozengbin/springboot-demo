package com.zzb.sensitive.enmu;


/**
 * 类名称：ESensitiveType
 * 类描述：脱敏类型枚举
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:37 上午
 * 修改备注：TODO
 */
public enum ESensitiveType {

    /**
     * 中文名
     */
    CHINESE_NAME,
    /**
     * 身份证号
     */
    ID_CARD,
    /**
     * 座机号
     */
    FIXED_PHONE,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 密码
     */
    PASSWORD,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 银行卡
     */
    BANK_CARD,
    /**
     * 公司开户银行联号
     */
    SHOPS_CODE,
    /**
     * 自定义正则过滤
     */
    RGE_EXP,

    /**
     * 空值
     */
    NULL
}
