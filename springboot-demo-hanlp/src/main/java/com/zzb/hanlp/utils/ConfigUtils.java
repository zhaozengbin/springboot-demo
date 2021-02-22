package com.zzb.hanlp.utils;

import java.util.Properties;

public class ConfigUtils {

    public static String hanlpRootPath;

    public static String hanlpCustomizeVecModelFile;

    public static String hanlpCustomizeVecModelMiniFile;

    public static String hanlpCWSModelFile;

    private static String cwsModelFile = "data/model/perceptron/pku199801/cws.bin";

    static {
        Properties p = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ConfigUtils.class.getClassLoader();
        }
        try {
            p.load(loader.getResourceAsStream("hanlp.properties"));
            hanlpRootPath = p.getProperty("root", "").replaceAll("\\\\", "/");
            if (hanlpRootPath.length() > 0 && !hanlpRootPath.endsWith("/")) {
                hanlpRootPath += "/";
            }
            hanlpCustomizeVecModelFile = hanlpRootPath + p.getProperty("hanlp.customize.vec.model.file");
            hanlpCustomizeVecModelFile = hanlpCustomizeVecModelFile.replaceAll("//", "/");

            hanlpCustomizeVecModelMiniFile = hanlpRootPath + p.getProperty("hanlp.customize.vec.model.mini.file");
            hanlpCustomizeVecModelMiniFile = hanlpCustomizeVecModelMiniFile.replaceAll("//", "/");

            hanlpCWSModelFile = hanlpRootPath + cwsModelFile;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
