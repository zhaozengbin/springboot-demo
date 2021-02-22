package com.zzb.monitor.chain.util;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.WeakHashMap;

/**
 * 类名称：ReadClasspathFile
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/2/18 5:21 下午
 * 修改备注：TODO
 */
public class ReadClasspathFile {


    private static WeakHashMap<String, byte[]> map = new WeakHashMap<>();

    public static byte[] read(String classPath) {
        //考虑到数据的一致性，这里没有使用map的containsKey()
        byte[] s = map.get(classPath);
        if (s != null) {
            return s;
        }
        //判空
        ClassPathResource resource = new ClassPathResource(classPath);
        if (!resource.exists()) {
            return null;
        }
        //读取
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(resource.getInputStream());
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(stream)) {
            byte[] bytes = new byte[1024];
            int n;
            while ((n = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //DCL双检查锁
        if (!map.containsKey(classPath)) {
            synchronized (ReadClasspathFile.class) {
                if (!map.containsKey(classPath)) {
                    map.put(classPath, stream.toByteArray());
                }
            }
        }
        return stream.toByteArray();
    }
}
