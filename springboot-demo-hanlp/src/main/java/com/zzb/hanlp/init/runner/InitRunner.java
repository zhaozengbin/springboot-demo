package com.zzb.hanlp.init.runner;

import com.hankcs.hanlp.HanLP;
import com.zzb.hanlp.init.thread.InitConfigThread;
import com.zzb.hanlp.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

@Component
/**
 * 类名称：InitRunner
 * 类描述：初始化加载类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2019-08-01 15:53
 * 修改备注：TODO
 *
 */
public class InitRunner implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(InitRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        HanLP.Config.enableDebug(true);

        LOG.info("加载词向量模型开始,词向量文件:" + ConfigUtils.hanlpCustomizeVecModelFile + "预加载词向量文件:" + ConfigUtils.hanlpCustomizeVecModelMiniFile);
        File modelMiniFile = new File(ConfigUtils.hanlpCustomizeVecModelMiniFile);
        File modelFile = new File(ConfigUtils.hanlpCustomizeVecModelFile);

        if (StringUtils.isEmpty(ConfigUtils.hanlpCustomizeVecModelFile) || !modelFile.exists()) {
            LOG.error(String.format("读取词向量模型失败:hanlpCustomizeVecModelFilePath[%s],hanlpCustomizeVecModelFileFlag[%b]", ConfigUtils.hanlpCustomizeVecModelFile, modelFile.exists()));
            return;
        }

        if (StringUtils.isEmpty(ConfigUtils.hanlpCustomizeVecModelMiniFile) || !modelMiniFile.exists() || ConfigUtils.hanlpCustomizeVecModelFile.equals(ConfigUtils.hanlpCustomizeVecModelMiniFile)) {
            LOG.error(String.format("读取词向量Mini模型失败或与完整模型一致,所以不会读取预加载模型:hanlpCustomizeVecModelMiniFilePath[%s],hanlpCustomizeVecModelMiniFileFlag[%b],直接加载完整模型", ConfigUtils.hanlpCustomizeVecModelMiniFile, modelMiniFile.exists()));
            ConfigUtils.hanlpCustomizeVecModelMiniFile = null;
        }

        try {
            InitConfigThread loadModel = new InitConfigThread(ConfigUtils.hanlpCustomizeVecModelFile, ConfigUtils.hanlpCustomizeVecModelMiniFile);
            Thread loadModelThread = new Thread(loadModel);
            loadModelThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            InitConfigThread.loadModelFlag = -2;
        }
    }
}