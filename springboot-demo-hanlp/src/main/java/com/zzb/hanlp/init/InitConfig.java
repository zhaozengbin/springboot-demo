package com.zzb.hanlp.init;

import cn.hutool.core.util.ObjectUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;

import java.io.IOException;

public class InitConfig {
    private static WordVectorModel wordVectorMiniModel;

    private static WordVectorModel wordVectorModel;

    private static DocVectorModel docVectorModel;

    private static CRFLexicalAnalyzer crfLexicalAnalyzer;

    /**
     * 方法：wordVectorModelInstance
     * 描述：获取词向量
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : com.hankcs.hanlp.mining.word2vec.WordVectorModel
     * @date: 2019年08月01日 18:29
     */
    public static WordVectorModel wordVectorModelInstance() {
        return ObjectUtil.isNotNull(wordVectorModel) ? wordVectorModel : wordVectorMiniModel;
    }

    /**
     * 方法：wordVectorModelInstance
     * 描述：词向量模型
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param modelFile :
     * @return : com.hankcs.hanlp.mining.word2vec.WordVectorModel
     * @date: 2019年08月01日 15:49
     */
    public static WordVectorModel wordVectorModelInstance(String modelFile, boolean isReload) {
        try {
            if (isReload && null == wordVectorModel) {
                wordVectorModel = new WordVectorModel(modelFile);
            }
            if (null == wordVectorModel && null == wordVectorMiniModel && !isReload) {
                wordVectorMiniModel = new WordVectorModel(modelFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ObjectUtil.isNotNull(wordVectorModel) ? wordVectorModel : wordVectorMiniModel;
    }


    /**
     * 方法：docVectorModelInstance
     * 描述：文档向量模型
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : com.hankcs.hanlp.mining.word2vec.DocVectorModel
     * @date: 2019年08月01日 15:50
     */
    public static DocVectorModel docVectorModelInstance() {
        if (null == docVectorModel) {
            try {
                docVectorModel = new DocVectorModel(wordVectorModelInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docVectorModel;
    }

    /**
     * 方法：docVectorModelInstance
     * 描述：文档向量模型
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param wordVectorModel
     * @return : com.hankcs.hanlp.mining.word2vec.DocVectorModel
     * @date: 2019年08月01日 15:50
     */
    public static DocVectorModel docVectorModelInstance(WordVectorModel wordVectorModel) {
        if (null == docVectorModel) {
            try {
                docVectorModel = new DocVectorModel(wordVectorModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docVectorModel;
    }

    public static CRFLexicalAnalyzer crfLexicalAnalyzerInstance() {
        try {
            if (crfLexicalAnalyzer == null) {
                crfLexicalAnalyzer = new CRFLexicalAnalyzer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crfLexicalAnalyzer;
    }
}
