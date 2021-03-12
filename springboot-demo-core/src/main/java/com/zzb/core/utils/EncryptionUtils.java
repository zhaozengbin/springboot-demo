package com.zzb.core.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class EncryptionUtils {
    public static final String encryption = "5fdb2b224cea4939";

    private static SymmetricCrypto SM4;

    /**
     * 方法：getSM4
     * 描述：获取SM4实例
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : cn.hutool.crypto.symmetric.SymmetricCrypto
     * @date: 2020年12月18日 10:15 上午
     */
    private static SymmetricCrypto getSM4() {
        if (ObjectUtil.isEmpty(SM4)) {
            byte[] desKey = encryption.getBytes();
            SM4 = new SymmetricCrypto("SM4", desKey);
        }
        return SM4;
    }

    /**
     * 方法：SM4Encrypt
     * 描述：SM4加密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param decryptStr :
     * @return : java.lang.String
     * @date: 2020年12月17日 11:36 上午
     */
    public static String SM4Encrypt(String decryptStr) {
        String encryptStr = getSM4().encryptHex(decryptStr);
        return encryptStr;
    }

    /**
     * 方法：SM4Decrypt
     * 描述：SM4解密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param encryptStr :
     * @return : java.lang.String
     * @date: 2020年12月17日 11:36 上午
     */
    public static String SM4Decrypt(String encryptStr) {
        String decryptStr = getSM4().decryptStr(encryptStr, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }
}
