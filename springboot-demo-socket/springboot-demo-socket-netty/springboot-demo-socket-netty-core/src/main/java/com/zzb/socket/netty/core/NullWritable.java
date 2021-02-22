package com.zzb.socket.netty.core;

import lombok.Data;

import java.io.Serializable;

/**
 * 类名称：NullWritable
 * 类描述：服务器可能返回空的处理
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:31 下午
 * 修改备注：TODO
 */
@Data
public class NullWritable implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 2123827169429254101L;
    /**
     * 单例
     */
    private static NullWritable instance = new NullWritable();

    /**
     * 私有构造器
     */
    private NullWritable() {
    }

    /**
     * 方法：nullWritable
     * 描述：返回代表Null的对象
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : com.zzb.socket.netty.core.NullWritable 当方法返回值为void时或返回值为null时返回此对象
     * @date: 2021年01月15日 6:32 下午
     */
    public static NullWritable nullWritable() {
        return instance;
    }
}
