package com.zzb.core.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.yaml.snakeyaml.Yaml;
import sun.net.ResourceManager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * yml文件工具类
 */
public class YmlUtils {
    private static String APPLICATION_FILE = "application.yml";
    private static Map<Object, Object> result = new HashMap<>();

    /**
     * 根据文件名获取yml的文件内容
     *
     * @return
     */
    public static Map<Object, Object> getYmlByFileName(String... file) {
        result = new HashMap<>();
        if (ObjectUtil.isEmpty(file)) {
            file = new String[1];
            file[0] = APPLICATION_FILE;
        }
        ClassPathResource resource = new ClassPathResource(file[0]);
        Yaml props = new Yaml();
        Object obj = props.loadAs(resource.getStream(), Map.class);
        Map<String, Object> param = (Map<String, Object>) obj;
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();

            if (val instanceof Map) {
                forEachYaml(key, (Map<String, Object>) val);
            } else {
                result.put(key, val.toString());
            }
        }
        return result;
    }

    /**
     * 根据key获取值
     *
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        Map<Object, Object> map = getYmlByFileName(null);
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    /**
     * 遍历yml文件，获取map集合
     *
     * @param key_str
     * @param obj
     * @return
     */
    public static Map<Object, Object> forEachYaml(String key_str, Map<String, Object> obj) {
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            String str_new = "";
            if (StrUtil.isNotEmpty(key_str)) {
                str_new = key_str + "." + key;
            } else {
                str_new = key;
            }
            if (val instanceof Map) {
                forEachYaml(str_new, (Map<String, Object>) val);
            } else {
                result.put(str_new, val.toString());
            }
        }
        return result;
    }

    /**
     * 获取bootstrap.yml的name
     *
     * @return
     */
    public static String getApplicationName() {
        return getYmlByFileName(APPLICATION_FILE).get("spring.application.name").toString();
    }
}
