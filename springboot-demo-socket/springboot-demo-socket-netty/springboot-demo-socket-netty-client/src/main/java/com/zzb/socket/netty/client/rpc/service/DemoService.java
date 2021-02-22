package com.zzb.socket.netty.client.rpc.service;


import com.zzb.socket.netty.core.User;
import com.zzb.socket.netty.core.exception.ErrorParamsException;

/**
 * 类名称：DemoService
 * 类描述：测试Service
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:49 上午
 * 修改备注：TODO
 */
public interface DemoService {


    /**
     * 方法：division
     * 描述：除法运算
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param numberA : 第一个数
     * @param numberB : 第二个数
     * @return : double 结果
     * @date: 2021年01月18日 10:49 上午
     */


    double division(int numberA, int numberB) throws ErrorParamsException;

    /**
     * 方法：getUserInfo
     * 描述：获取用户信息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : com.zzb.socket.netty.core.User
     * @date: 2021年01月18日 10:50 上午
     */
    User getUserInfo();

    /**
     * 方法：print
     * 描述：打印方法
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : java.lang.String 一个字符串
     * @date: 2021年01月18日 10:50 上午
     */
    String print();

    /**
     * 方法：sum
     * 描述：求和方法
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param numberA : 第一个数
     * @param numberB : 第二个数
     * @return : int 两数之和
     * @date: 2021年01月18日 10:51 上午
     */
    int sum(int numberA, int numberB);
}
