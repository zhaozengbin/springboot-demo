package com.zzb.sensitive.enmu;

/**
 * 类名称：EHandleType
 * 类描述： fastJson处理类型
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:15 上午
 * 修改备注：TODO
 */
public enum EHandleType {
    /**
     * 默认,匹配key,不区分层级
     */
    DEFAULT,
    /**
     * 正则匹配,JsonPath正则匹配
     */
    RGE_EXP
}
