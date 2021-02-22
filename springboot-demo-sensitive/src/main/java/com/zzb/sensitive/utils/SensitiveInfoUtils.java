package com.zzb.sensitive.utils;

import cn.hutool.core.util.ObjectUtil;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import org.apache.commons.lang3.StringUtils;


/**
 * 类名称：SensitiveInfoUtils
 * 类描述：脱敏基本工具类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:24 上午
 * 修改备注：TODO
 */
public class SensitiveInfoUtils {

    public final static Configuration CONF = Configuration.builder().options(Option.AS_PATH_LIST, Option.DEFAULT_PATH_LEAF_TO_NULL).build();

    /**
     * 方法：chineseName
     * 描述：[中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param fullName :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:25 上午
     */
    public static String chineseName(final String fullName) {
        if (ObjectUtil.isEmpty(fullName)) {
            return "";
        }
        final String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * 方法：chineseName
     * 描述： [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param familyName :
     * @param givenName  :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:25 上午
     */
    public static String chineseName(final String familyName, final String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        return chineseName(familyName + givenName);
    }

    /**
     * 方法：idCardNum
     * 描述：[身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param id :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:25 上午
     */
    public static String idCardNum(final String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        return StringUtils.left(id, 3).concat(StringUtils
            .removeStart(StringUtils.leftPad(StringUtils.right(id, 3), StringUtils.length(id), "*"),
                "***"));
    }

    /**
     * 方法：idCardNum
     * 描述：【身份证号】前三位 和后三位
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param idCardNum :
     * @param front     :
     * @param end       :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:26 上午
     */
    public static String idCardNum(String idCardNum, int front, int end) {
        //身份证不能为空
        if (StringUtils.isEmpty(idCardNum)) {
            return "";
        }
        //需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return "";
        }
        //需要截取的不能小于0
        if (front < 0 || end < 0) {
            return "";
        }
        //计算*的数量
        int asteriskCount = idCardNum.length() - (front + end);
        StringBuffer asteriskStr = new StringBuffer();
        for (int i = 0; i < asteriskCount; i++) {
            asteriskStr.append("*");
        }
        String regex = "(\\w{" + front + "})(\\w+)(\\w{" + end + "})";
        return idCardNum.replaceAll(regex, "$1" + asteriskStr + "$3");
    }

    /**
     * 方法：fixedPhone
     * 描述：【固定电话 前四位，后两位
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param num :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:26 上午
     */
    public static String fixedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 4).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 2), StringUtils.length(num), "*"), "****"));
    }

    /**
     * 方法：mobilePhone2
     * 描述：[手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param num :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:26 上午
     */
    public static String mobilePhone2(final String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 2).concat(StringUtils
            .removeStart(StringUtils.leftPad(StringUtils.right(num, 2), StringUtils.length(num), "*"),
                "***"));

    }

    /**
     * 方法：mobilePhone
     * 描述：【手机号码】前三位，后两位，其他隐藏，比如135******10
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param num :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:26 上午
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 2), StringUtils.length(num), "*"), "***"));
    }

    /**
     * 方法：address
     * 描述：[地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param address       :
     * @param sensitiveSize :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:27 上午
     */
    public static String address(final String address, final int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        if (address.length() <= sensitiveSize) {
            return address;
        }
        final int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * 方法：email
     * 描述： [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param email :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:27 上午
     */
    public static String email(final String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        final int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*")
                .concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * 方法：bankCard
     * 描述：[银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param cardNum :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:27 上午
     */
    public static String bankCard(final String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        if (cardNum.length() < 11) {
            return idCardNum(cardNum, 3, 2);
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(
            StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"),
            "******"));
    }

    /**
     * 方法：shopsCode
     * 描述：[公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param code :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:27 上午
     */
    public static String shopsCode(final String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

    /**
     * 方法：password
     * 描述：【密码】密码的全部字符都用*代替，比如：******
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param password :
     * @return : java.lang.String
     * @date: 2020年12月15日 11:27 上午
     */
    public static String password(String password) {
        if (StringUtils.isBlank(password)) {
            return "";
        }
        String pwd = StringUtils.left(password, 0);
        return StringUtils.rightPad(pwd, StringUtils.length(password), "*");
    }

}
