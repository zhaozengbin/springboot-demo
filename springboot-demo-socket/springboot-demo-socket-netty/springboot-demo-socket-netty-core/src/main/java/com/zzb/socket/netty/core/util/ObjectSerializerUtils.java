package com.zzb.socket.netty.core.util;

import cn.hutool.log.Log;

import java.io.*;

/**
 * 类名称：ObjectSerializerUtils
 * 类描述：对象序列化工具
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:30 下午
 * 修改备注：TODO
 */
public class ObjectSerializerUtils {

    private static final Log LOGGER = Log.get(ObjectSerializerUtils.class);

    /**
     * 方法：deSerilizer
     * 描述：反序列化
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param data :
     * @return : java.lang.Object
     * @date: 2021年01月15日 6:31 下午
     */
    public static Object deSerilizer(byte[] data) {

        if (data != null && data.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (Exception e) {
                LOGGER.info("[异常信息] {}", e.getMessage());
                e.printStackTrace();
            }
            return null;
        } else {
            LOGGER.info("[反序列化] 入参为空");
            return null;
        }
    }

    /**
     * 方法：serilizer
     * 描述：序列化对象
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param obj :
     * @return : byte[]
     * @date: 2021年01月15日 6:31 下午
     */
    public static byte[] serilizer(Object obj) {
        if (obj != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(obj);
                oos.flush();
                oos.close();
                return bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
}
