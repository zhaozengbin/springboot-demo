package com.zzb.core.utils;

import cn.hutool.core.io.resource.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 * 类名称：PropertiesUtils
 * 类描述：PropertiesUtils 工具类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/20 3:05 下午
 * 修改备注：TODO
 */
public class PropertiesUtils {
    /**
     * 方法：getProperties
     * 描述：TODO
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param propertiesFileName : properties文件名
     * @return : java.util.Properties
     * @date: 2021年02月20日 3:05 下午
     */
    public static Properties getProperties(String propertiesFileName) {
        try {
            ClassPathResource resource = new ClassPathResource(propertiesFileName);
            Properties properties = new Properties();
            properties.load(resource.getStream());
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
