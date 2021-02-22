package com.zzb.hanlp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.model.perceptron.PerceptronLexicalAnalyzer;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Other.DoubleArrayTrieSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.zzb.core.controller.BaseDemoController;
import com.zzb.hanlp.controller.vo.SegmentConfigVo;
import com.zzb.hanlp.controller.vo.TermVo;
import com.zzb.hanlp.utils.ConfigUtils;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 类名称：DemoHanlpSegmentController
 * 类描述：Hanlp分词Demo
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/27 2:12 下午
 * 修改备注：TODO
 */
@Api(value = "Hanlp分词样例", tags = "Hanlp分词样例")
@RestController
public class DemoHanlpSegmentController extends BaseDemoController {

    /**
     * 方法：basicSeg
     * 描述："标准分词器(StandardTokenizer)", "详见<a href="http://www.hankcs.com/nlp/segment/the-word-graph-is-generated.html">文档</a>"
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param text :
     * @return : java.util.List<com.hankcs.hanlp.seg.common.Term>
     * @date: 2020年10月27日 2:14 下午
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation(
        value = "标准分词器",
        notes = "标准分词器(StandardTokenizer)。<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/segment/the-word-graph-is-generated.html\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
    })
    @PostMapping("/basicSeg")
    @ResponseBody
    public BaseSwaggerVo<List<TermVo>> basicSeg(SegmentConfigVo segmentConfigVo, String text) {
        Segment segment = setSegmentConfig(new ViterbiSegment(), segmentConfigVo);
        return segment(segment, text);
    }

    /**
     * 方法：shortSeg
     * 描述："最短路径分词器(DijkstraSegment)", "一般场景下最短路分词的精度已经足够，而且速度比N最短路分词器快几倍"
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param segmentConfigVo :
     * @param text            :
     * @return : com.zzb.hanlp.controller.vo.TermVo
     * @date: 2020年10月27日 3:14 下午
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation(
        value = "最短路径分词器",
        notes = "最短路径分词器(DijkstraSegment),一般场景下最短路分词的精度已经足够，而且速度比N最短路分词器快几倍。<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/segment/n-shortest-path-to-the-java-implementation-and-application-segmentation.html\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
    })
    @PostMapping("/shortSeg")
    @ResponseBody
    private BaseSwaggerVo<List<TermVo>> shortSeg(SegmentConfigVo segmentConfigVo, String text) {
        Segment segment = setSegmentConfig(new DijkstraSegment(), segmentConfigVo);
        return segment(segment, text);
    }


    /**
     * 方法：nShortSeg
     * 描述："N最短路分词器(NShortSegment)", "N最短路分词器NShortSegment比最短路分词器慢，但是效果稍微好一些，对命名实体识别能力更强。"
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param segmentConfigVo :
     * @param text            :
     * @return : com.zzb.hanlp.controller.vo.TermVo
     * @date: 2020年10月27日 3:14 下午
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation(
        value = "N最短路分词器",
        notes = "N最短路分词器(NShortSegment),N最短路分词器NShortSegment比最短路分词器慢，但是效果稍微好一些，对命名实体识别能力更强。<br /> " +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/segment/n-shortest-path-to-the-java-implementation-and-application-segmentation.html\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
    })
    @PostMapping("/nShortSeg")
    @ResponseBody
    private BaseSwaggerVo<List<TermVo>> nShortSeg(SegmentConfigVo segmentConfigVo, String text) {
        Segment segment = setSegmentConfig(new NShortSegment(), segmentConfigVo);
        return segment(segment, text);
    }

    /**
     * 方法：perceptronLexicalAnalyzerSeg
     * 描述："基于感知机序列标注的词法分析器(PerceptronLexicalAnalyzer)，可选多个模型。<br />" +
     * "- large训练自一亿字的大型综合语料库，是已知范围内全世界最大的中文分词语料库。<br />" +
     * "- pku199801训练自个人修订版1998人民日报语料1月份，仅有183万字。<br />" +
     * " 语料库规模决定实际效果，面向生产环境的语料库应当在千万字量级。欢迎用户在自己的语料上训练新模型以适应新领域、识别新的命名实体。\n" +
     * " 无论在何种语料上训练，都完全支持简繁全半角和大小写。<br />" +
     * " 可自主训练模型详见<a href=\"https://github.com/hankcs/HanLP/wiki/%E7%BB%93%E6%9E%84%E5%8C%96%E6%84%9F%E7%9F%A5%E6%9C%BA%E6%A0%87%E6%B3%A8%E6%A1%86%E6%9E%B6\">文档</a>"
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param segmentConfigVo :
     * @param text            :
     * @return : com.zzb.hanlp.controller.vo.TermVo
     * @date: 2020年10月27日 3:14 下午
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation(
        value = "感知机序列标注的词法分析器",
        notes = "基于感知机序列标注的词法分析器(PerceptronLexicalAnalyzer)，可选多个模型。<br />" +
            "- large训练自一亿字的大型综合语料库，是已知范围内全世界最大的中文分词语料库。<br />" +
            "- pku199801训练自个人修订版1998人民日报语料1月份，仅有183万字。<br />" +
            " 语料库规模决定实际效果，面向生产环境的语料库应当在千万字量级。欢迎用户在自己的语料上训练新模型以适应新领域、识别新的命名实体。\n" +
            " 无论在何种语料上训练，都完全支持简繁全半角和大小写。<br />" +
            " 可自主训练模型详见<a href=\"https://github.com/hankcs/HanLP/wiki/%E7%BB%93%E6%9E%84%E5%8C%96%E6%84%9F%E7%9F%A5%E6%9C%BA%E6%A0%87%E6%B3%A8%E6%A1%86%E6%9E%B6\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
    })
    @PostMapping("/perceptronLexicalAnalyzerSeg")
    @ResponseBody
    private BaseSwaggerVo<List<TermVo>> perceptronLexicalAnalyzerSeg(SegmentConfigVo segmentConfigVo, String text) {
        try {
            PerceptronLexicalAnalyzer analyzer = new PerceptronLexicalAnalyzer(ConfigUtils.hanlpCWSModelFile,
                HanLP.Config.PerceptronPOSModelPath,
                HanLP.Config.PerceptronNERModelPath);
            Segment segment = setSegmentConfig(analyzer, segmentConfigVo);
            return segment(segment, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(
        value = "CRF分词分析器",
        notes = "极速分词(SpeedTokenizer)，基于AhoCorasickDoubleArrayTrie实现的词典分词，适用于“高吞吐量”“精度一般”的场合。<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/program/algorithm/aho-corasick-double-array-trie.html\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
    })
    @PostMapping("/speedTokenizerSeg")
    @ResponseBody
    private BaseSwaggerVo<List<TermVo>> speedTokenizerSeg(SegmentConfigVo segmentConfigVo, String text) {
        DoubleArrayTrieSegment analyzer = new DoubleArrayTrieSegment();
        Segment segment = setSegmentConfig(analyzer, segmentConfigVo);
        return segment(segment, text);
    }

    @ApiOperationSupport(order = 6)
    @ApiOperation(
        value = "极速词典分词",
        notes = "CRF对新词有很好的识别能力，但是开销较大。<br />" +
            "具体算法介绍详见<a href=\"https://github.com/hankcs/HanLP/wiki/CRF%E8%AF%8D%E6%B3%95%E5%88%86%E6%9E%90\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
    })
    @PostMapping("/crfLexicalAnalyzerSeg")
    @ResponseBody
    private BaseSwaggerVo<List<TermVo>> crfLexicalAnalyzerSeg(SegmentConfigVo segmentConfigVo, String text) {
        try {
            CRFLexicalAnalyzer analyzer = new CRFLexicalAnalyzer();
            Segment segment = setSegmentConfig(analyzer, segmentConfigVo);
            return segment(segment, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方法：segment
     * 描述：分词
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param segment :
     * @param text    :
     * @return : com.zzb.hanlp.controller.vo.TermVo
     * @date: 2020年10月27日 4:05 下午
     */
    private BaseSwaggerVo<List<TermVo>> segment(Segment segment, String text) {
        return BaseSwaggerVo.success(TermVo.term2Vo(segment.seg(text)));
    }

    /**
     * 方法：setSegmentConfig
     * 描述：设置分词配置
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param t               :
     * @param segmentConfigVo :
     * @return : T
     * @date: 2020年10月27日 3:06 下午
     */
    private <T extends Segment> T setSegmentConfig(T t, SegmentConfigVo segmentConfigVo) {
        t.enableOrganizationRecognize(segmentConfigVo.isEnableOrganizationRecognize());
        t.enablePlaceRecognize(segmentConfigVo.isEnablePlaceRecognize());
        t.enableCustomDictionary(segmentConfigVo.isEnableCustomDictionary());
        t.enableCustomDictionaryForcing(segmentConfigVo.isEnableCustomDictionaryForcing());
        t.enableAllNamedEntityRecognize(segmentConfigVo.isEnableAllNamedEntityRecognize());
        t.enableJapaneseNameRecognize(segmentConfigVo.isEnableJapaneseNameRecognize());
        t.enableNameRecognize(segmentConfigVo.isEnableNameRecognize());
        t.enableOffset(segmentConfigVo.isEnableOffset());
        t.enableTranslatedNameRecognize(segmentConfigVo.isEnableTranslatedNameRecognize());
        t.enableNumberQuantifierRecognize(segmentConfigVo.isEnableNumberQuantifierRecognize());
        t.enablePartOfSpeechTagging(segmentConfigVo.isEnablePartOfSpeechTagging());
        t.enableIndexMode(segmentConfigVo.isEnableIndexMode());
        t.enableIndexMode(segmentConfigVo.getEnableIndexModeNums());
        return t;
    }

    public static void main(String[] args) {
        System.out.println(new DemoHanlpSegmentController().nShortSeg(new SegmentConfigVo(), "王国维和服务员"));
    }
}
