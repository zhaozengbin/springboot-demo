package com.zzb.sensitive;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Test;


public class SecureTests {

    private static final String CONTENT = "test中文";

    @Test
    public void testAES() {
        //随机生成密钥
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        //加密
        byte[] encrypt = aes.encrypt(CONTENT);
        //解密
        byte[] decrypt = aes.decrypt(encrypt);
        //加密为16进制表示
        String encryptHex = aes.encryptHex(CONTENT);
        Console.log("加密后 => {}", encryptHex);
        //解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        Console.log("解密后 => {}", decryptStr);
    }


    @Test
    public void testDESede() {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
        //加密
        byte[] encrypt = des.encrypt(CONTENT);
        //解密
        byte[] decrypt = des.decrypt(encrypt);
        //加密为16进制字符串（Hex表示）
        String encryptHex = des.encryptHex(CONTENT);
        Console.log("加密后 => {}", encryptHex);
        //解密为字符串
        String decryptStr = des.decryptStr(encryptHex);
        Console.log("解密后 => {}", decryptStr);
    }

    @Test
    public void testSM4() {
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4");
        String encryptHex = sm4.encryptHex(CONTENT);
        Console.log("加密后 => {}", encryptHex);
        String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);//test中文
        Console.log("解密后 => {}", decryptStr);
    }
}
