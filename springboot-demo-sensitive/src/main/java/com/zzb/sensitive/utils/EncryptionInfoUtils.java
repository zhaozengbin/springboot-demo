package com.zzb.sensitive.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.zzb.sensitive.config.properties.SecurityProperties;
import com.zzb.sensitive.enmu.EEncryptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 类名称：EncryptionInfoUtils
 * 类描述：加密基本工具类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:24 上午
 * 修改备注：TODO
 */
@Component
public class EncryptionInfoUtils {

    @Autowired
    private SecurityProperties securityProperties;

    private static SymmetricCrypto AES;

    private static SymmetricCrypto DESede;

    private static SymmetricCrypto SM4;

    /**
     * 方法：getAES
     * 描述：获取AES实例
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : cn.hutool.crypto.symmetric.SymmetricCrypto
     * @date: 2020年12月18日 10:15 上午
     */
    private SymmetricCrypto getAES() {
        if (ObjectUtil.isEmpty(AES)) {
            if (ObjectUtil.isEmpty(securityProperties.getEncryptionAesKey())) {
                throw new UtilException("秘钥获取不到");
            }
            byte[] aesKey = securityProperties.getEncryptionAesKey().getBytes();
            AES = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey);
        }
        return AES;
    }

    /**
     * 方法：getDESede
     * 描述：获取DES实例
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : cn.hutool.crypto.symmetric.SymmetricCrypto
     * @date: 2020年12月18日 10:15 上午
     */
    private SymmetricCrypto getDESede() {
        if (ObjectUtil.isEmpty(DESede)) {
            if (ObjectUtil.isEmpty(securityProperties.getEncryptionDesKey())) {
                throw new UtilException("秘钥获取不到");
            }
            byte[] desKey = securityProperties.getEncryptionDesKey().getBytes();
            DESede = new SymmetricCrypto(SymmetricAlgorithm.DESede, desKey);
        }
        return DESede;
    }

    /**
     * 方法：getSM4
     * 描述：获取SM4实例
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : cn.hutool.crypto.symmetric.SymmetricCrypto
     * @date: 2020年12月18日 10:15 上午
     */
    private SymmetricCrypto getSM4() {
        if (ObjectUtil.isEmpty(SM4)) {
            if (ObjectUtil.isEmpty(securityProperties.getEncryptionSM4Key())) {
                throw new UtilException("秘钥获取不到");
            }
            byte[] desKey = securityProperties.getEncryptionSM4Key().getBytes();
            SM4 = new SymmetricCrypto("SM4", desKey);
        }
        return SM4;
    }

    /**
     * 方法：encode
     * 描述：加密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param eEncryptionType :
     * @param str             :
     * @return : java.lang.String
     * @date: 2020年12月18日 10:16 上午
     */
    public String encode(EEncryptionType eEncryptionType, Object str) {
        if (ObjectUtil.isNotEmpty(str)) {
            String result = String.valueOf(str);
            switch (eEncryptionType) {
                case AES:
                    result = AESEncrypt(result);
                    break;
                case DES_EDE:
                    result = DESedeEncrypt(result);
                    break;
                case SM4:
                case DEFAULT:
                    result = SM4Encrypt(result);
                    break;
                default:
                    return null;
            }
            return result;
        }
        return null;
    }

    /**
     * 方法：decode
     * 描述：解密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param eEncryptionType :
     * @param str             :
     * @param clazz           :
     * @return : T
     * @date: 2020年12月18日 10:16 上午
     */
    public <T> T decode(EEncryptionType eEncryptionType, String str, Class<T> clazz) {
        String result = null;
        if (StrUtil.isNotEmpty(str)) {
            switch (eEncryptionType) {
                case AES:
                    result = AESDecrypt(str);
                    break;
                case DES_EDE:
                    result = DESedeDecrypt(str);
                    break;

                case SM4:
                case DEFAULT:
                    result = SM4Decrypt(str);
                    break;
                default:
                    return null;
            }
            return Convert.convert(clazz, result);
        }
        return null;
    }

    /**
     * 方法：AESEncrypt
     * 描述：AES加密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param decryptStr :
     * @return : java.lang.String
     * @date: 2020年12月17日 11:36 上午
     */
    public String AESEncrypt(String decryptStr) {
        //加密为16进制表示
        String encryptStr = getAES().encryptHex(decryptStr);
        return encryptStr;
    }

    /**
     * 方法：AESDecrypt
     * 描述：AES解密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param encryptStr :
     * @return : java.lang.String
     * @date: 2020年12月17日 11:36 上午
     */
    public String AESDecrypt(String encryptStr) {
        //解密为字符串
        String decryptStr = getAES().decryptStr(encryptStr, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }

    /**
     * 方法：DESedeEncrypt
     * 描述：DESede加密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param decryptStr :
     * @return : T
     * @date: 2020年12月17日 11:36 上午
     */
    public String DESedeEncrypt(String decryptStr) {
        String encryptStr = getDESede().encryptHex(decryptStr);
        return encryptStr;
    }

    /**
     * 方法：DESedeDecrypt
     * 描述：DESede解密
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param encryptStr :
     * @return : java.lang.String
     * @date: 2020年12月17日 11:36 上午
     */
    public String DESedeDecrypt(String encryptStr) {
        //解密为字符串
        String decryptStr = getDESede().decryptStr(encryptStr);
        return decryptStr;
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
    public String SM4Encrypt(String decryptStr) {
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
    public String SM4Decrypt(String encryptStr) {
        String decryptStr = getSM4().decryptStr(encryptStr, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }

}
