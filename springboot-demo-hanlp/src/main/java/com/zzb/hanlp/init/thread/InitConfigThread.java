package com.zzb.hanlp.init.thread;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.dictionary.stopword.Filter;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
import com.zzb.hanlp.init.InitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static com.hankcs.hanlp.corpus.tag.Nature.*;

/**
 * 类名称：LoadModelThread
 * 类描述：加载模型线程
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2019-08-06 14:36
 * 修改备注：TODO
 *
 * @Copyright：www.duia.com Inc. All rights reserved.
 */
public class InitConfigThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(InitConfigThread.class);

    // 词性过滤
    private static final List<Nature> NATURE_FILTER = Arrays.asList(e, o, u, ud, uj, uv, y, w);

    public static int loadModelFlag = 0;

    private String hanlpCustomizeVecModelFile;

    private String hanlpCustomizeVecModelFileMini;

    public InitConfigThread(String hanlpCustomizeVecModelFile, String hanlpCustomizeVecModelFileMini) {
        this.hanlpCustomizeVecModelFile = hanlpCustomizeVecModelFile;
        this.hanlpCustomizeVecModelFileMini = hanlpCustomizeVecModelFileMini;
    }

    @Override
    public void run() {
        InitConfig.crfLexicalAnalyzerInstance();
        NotionalTokenizer.SEGMENT = HanLP.newSegment();

        // 自定义分词停用词性
        CoreStopWordDictionary.FILTER = new Filter() {
            @Override
            public boolean shouldInclude(Term term) {
                if (NATURE_FILTER.contains(term.nature)) {
                    return false;
                }
                return !CoreStopWordDictionary.contains(term.word);
            }
        };

        if (hanlpCustomizeVecModelFileMini != null) {
            loadModelFile(true);
        }
        loadModelFile(false);
    }

    private void loadModelFile(boolean preLoading) {
        String vewModelFile = null;
        boolean isReload = false;
        if (preLoading) {
            // 预加载迷你词向量
            vewModelFile = hanlpCustomizeVecModelFileMini;
        } else {
            vewModelFile = hanlpCustomizeVecModelFile;
            isReload = true;
        }
        LOG.info(String.format("加载词向量模型[%s]开始,加载文件:[%s]", (preLoading ? "预加载" : "完整加载"), vewModelFile));
        long startTime = System.currentTimeMillis();
        InitConfig.wordVectorModelInstance(vewModelFile, isReload);
        InitConfig.docVectorModelInstance();
        long endTime = System.currentTimeMillis();
        LOG.info(String.format("加载词向量模型[%s]结束,共花费:%d秒", (preLoading ? "预加载" : "完整加载"), (endTime - startTime) / 1000));
        loadModelFlag = (preLoading ? 1 : 2);
    }
}