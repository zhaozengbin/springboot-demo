package com.zzb.socket.netty.client.rpc.util;

import cn.hutool.log.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：PackageClassUtils
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:54 上午
 * 修改备注：TODO
 */
public class PackageClassUtils {

    private final static Log LOG = Log.get(PackageClassUtils.class);

    /**
     * 方法：getAllFile
     * 描述：获取一个目录下的所有文件
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param s         :
     * @param file      :
     * @param classStrs :
     * @return : void
     * @date: 2021年01月18日 10:54 上午
     */
    private static void getAllFile(String s, File file, List<String> classStrs) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File file1 : files) {
                    getAllFile(s, file1, classStrs);
                }
        } else {
            String path = file.getPath();
            String cleanPath = path.replaceAll("/", ".");
            String fileName = cleanPath.substring(cleanPath.indexOf(s), cleanPath.length());
            LOG.info("[加载完成] 类文件：{}", fileName);
            classStrs.add(fileName);
        }
    }

    /**
     * 方法：getClassReferenceList
     * 描述：添加全限定类名到集合
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param classStrs : 集合
     * @param file      :
     * @param s         :
     * @return : java.util.List<java.lang.String> 类名集合
     * @date: 2021年01月18日 10:55 上午
     */
    private static List<String> getClassReferenceList(List<String> classStrs, File file, String s) {
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length != 0) {
            for (File file2 : listFiles) {
                if (file2.isFile()) {
                    String name = file2.getName();
                    String fileName = s + "." + name.substring(0, name.lastIndexOf('.'));
                    LOG.info("[加载完成] 类文件：{}", fileName);
                    classStrs.add(fileName);
                }
            }
        }
        return classStrs;
    }

    /**
     * 方法：resolver
     * 描述：解析包参数
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param basePackage : 包名
     * @return : java.util.List<java.lang.String> 包名字符串集合
     * @date: 2021年01月18日 10:55 上午
     */
    public static List<String> resolver(String basePackage) {
        // 以";"分割开多个包名
        String[] splitFHs = basePackage.split(";");
        List<String> classStrs = new ArrayList<>();
        // s: com.yyx.util.*
        for (String s : splitFHs) {
            LOG.info("[加载类目录] {}", s);
            // 路径中是否存在".*" com.yyx.util.*
            boolean contains = s.contains(".*");
            if (contains) {
                // 截断星号  com.yyx.util
                String filePathStr = s.substring(0, s.lastIndexOf(".*"));
                // 组装路径 com/yyx/util
                String filePath = filePathStr.replaceAll("\\.", "/");
                // 获取路径 xxx/classes/com/yyx/util
                File file = new File(PackageClassUtils.class.getResource("/").getPath() + "/" + filePath);
                // 获取目录下获取文件
                getAllFile(filePathStr, file, classStrs);
            } else {
                String filePath = s.replaceAll("\\.", "/");
                File file = new File(PackageClassUtils.class.getResource("/").getPath() + "/" + filePath);
                classStrs = getClassReferenceList(classStrs, file, s);
            }
        }
        return classStrs;
    }
}
