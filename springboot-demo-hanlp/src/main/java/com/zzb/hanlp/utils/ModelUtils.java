package com.zzb.hanlp.utils;

import cn.hutool.core.collection.CollUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.document.CorpusLoader;
import com.hankcs.hanlp.corpus.document.Document;
import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelUtils {
    public final static String NLP_FILE_DIR_CORPUS = "corpus";

    public final static String NLP_FILE_DIR_MODEL = "model";

    public final static String NLP_FILE_TEMPLATE = "%s_%s.txt";

    public final static String[] NLP_CORPUS_NATURE_YES = new String[]{"nature"};

    public final static String NLP_MODEL_NAME_CWS = "cws";

    public final static String NLP_MODEL_NAME_WORD2VEC = "w2v";

    public static WordVectorModel tempWordVectorModel;

    /**
     * 方法：createCorpus
     * 描述：处理语料 生语料 -> 熟语料
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param type       :
     * @param rawCorpora :
     * @param size       :
     * @return : java.lang.String
     * @date: 2020年11月04日 1:53 下午
     */
    public static File createCorpus(String type, String rawCorpora, int size) throws Exception {
        File rawCorporaFolder = new File(rawCorpora);
        // 开始制作熟语料
        if (rawCorporaFolder.exists() && rawCorporaFolder.isDirectory()) {
            File familiarCorpora = new File(rawCorporaFolder.getParent() +
                File.separator + NLP_FILE_DIR_CORPUS +
                File.separator + (String.format(NLP_FILE_TEMPLATE, rawCorporaFolder.getName(), type)));
            if (familiarCorpora.exists()) {
                familiarCorpora.delete();
            } else if (!familiarCorpora.getParentFile().exists()) {
                familiarCorpora.mkdirs();
            }
            try {
                ModelUtils.convertCorpus(rawCorporaFolder.getPath(), familiarCorpora, 0, size, Arrays.asList(NLP_CORPUS_NATURE_YES).contains(type));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return familiarCorpora;
        } else {
            throw new Exception("文件不存在");
        }
    }

    public final static String[] NLP_CORPUS_NATURE_NO = new String[]{""};

    /**
     * 方法：convertCorpus
     * 描述：生语料转换为熟语料
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param segment     :
     * @param inputFolder :
     * @param outputFile  :
     * @param begin       :
     * @param end         :
     * @return : void
     * @date: 2020年11月04日 11:21 上午
     */
    private static void convertCorpus(String inputFolder, File outputFile, final int begin, final int end, final boolean isNature) throws IOException {
        final BufferedWriter[] bw = {null};
        CorpusLoader.walk(inputFolder, new CorpusLoader.Handler() {
                int doc = 0;

                @Override
                public void handle(Document document) {
                    ++doc;
                    if (doc < begin || doc > end) {
                        return;
                    }
                    try {
                        if (outputFile.exists()) {
                            outputFile.delete();
                        }
                        List<Sentence> sentenceList = document.sentenceList;
                        if (sentenceList.size() == 0) {
                            return;
                        }
                        for (Sentence sentence : sentenceList) {
                            if (sentence.size() == 0) {
                                continue;
                            }
                            List<String> wordList = new ArrayList<>();
                            NotionalTokenizer.segment(sentence.toString()).forEach(term -> {
                                if (isNature) {
                                    wordList.add(term.toString());
                                } else {
                                    wordList.add(term.word);
                                }
                            });
                            if (bw[0] == null) {
                                bw[0] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
                            }
                            bw[0].write(CollUtil.join(wordList, "  "));
                            bw[0].newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
        bw[0].close();
    }
}
