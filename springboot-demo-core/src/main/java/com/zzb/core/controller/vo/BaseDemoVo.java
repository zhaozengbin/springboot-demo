package com.zzb.core.controller.vo;

import com.alibaba.fastjson.JSON;

/**
 * 类名称：BaseDemoVo
 * 类描述：基础vo
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/13 10:15 上午
 * 修改备注：TODO
 */
public class BaseDemoVo {

    /**
     * 状态码
     */
    private int code;
    /**
     * 信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    public BaseDemoVo() {
    }

    public BaseDemoVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseDemoVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BaseDemoVo success(String msg, Object data) {
        return new BaseDemoVo(200, msg, data);
    }

    public static BaseDemoVo fail(int code, String msg) {
        return new BaseDemoVo(code, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 方法：toObject
     * 描述：转换成子类
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param clazz :
     * @return : T
     * @date: 2020年10月13日 2:52 下午
     */
    public <T extends BaseDemoVo> T toObject(Class<T> clazz) {
        return JSON.toJavaObject(JSON.parseObject(toString()), clazz);
    }
}
