package com.zzb.hanlp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hankcs.hanlp.HanLP;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类名称：DemoHanlpInfoAcquisitionController
 * 类描述：Hanlp信息提取样例
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/28 4:18 下午
 * 修改备注：TODO
 */
@Api(value = "信息提取样例", tags = "信息提取样例")
@RestController
public class DemoHanlpInfoAcquisitionController {


    @ApiOperationSupport(order = 1)
    @ApiOperation(
        value = "关键词提取",
        notes = "关键词提取(TextRankKeyword), 用户可以直接调用TextRankKeyword.getKeywordList(document, size)。<br />" +
            "主要功能是从一段文本中根据TextRank算法来计算文本中分词后各组词的权重。<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/textrank-algorithm-to-extract-the-keywords-java-implementation.html\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
        @ApiImplicitParam(name = "text", value = "关键词数量", required = true, dataType = "Integer", defaultValue = "1")
    })
    @PostMapping("/keywordAcquisition")
    @ResponseBody
    public BaseSwaggerVo<List<String>> keywordAcquisition(String text, int size) {
        List<String> keywordList = HanLP.extractKeyword(text, size);
        return BaseSwaggerVo.success(keywordList);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(
        value = "段落提取",
        notes = "段落提取(TextRankSentence), 内部采用TextRankSentence实现，用户可以直接调用TextRankSentence.getTopSentenceList(document, size)。<br />" +
            "所谓自动摘要，就是从文章中自动抽取关键句。何谓关键句？人类的理解是能够概括文章中心的句子，机器的理解只能模拟人类的理解，即拟定一个权重的评分标准，给每个句子打分，之后给出排名靠前的几个句子。<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/textrank-algorithm-java-implementation-of-automatic-abstract.html\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
        @ApiImplicitParam(name = "text", value = "关键词数量", required = true, dataType = "Integer", defaultValue = "1")
    })
    @PostMapping("/sentenceAcquisition")
    @ResponseBody
    public BaseSwaggerVo<List<String>> sentenceAcquisition(String text, int size) {
        List<String> keywordList = HanLP.extractSummary(text, size);
        return BaseSwaggerVo.success(keywordList);
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(
        value = "短语提取(新词提取)",
        notes = "短语提取(MutualInformationEntropyPhraseExtractor), 内部采用MutualInformationEntropyPhraseExtractor实现，用户可以直接调用MutualInformationEntropyPhraseExtractor.extractPhrase(text, size)。<br />" +
            "在中文语言处理领域，一项重要的任务就是提取中文短语，也即固定多字词表达串的识别。短语提取经常用于搜索引擎的自动推荐，新词识别等领域。<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/extraction-and-identification-of-mutual-information-about-the-phrase-based-on-information-entropy.html\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
        @ApiImplicitParam(name = "text", value = "关键词数量", required = true, dataType = "Integer", defaultValue = "1")
    })
    @PostMapping("/phraseAcquisition")
    @ResponseBody
    public BaseSwaggerVo<List<String>> phraseAcquisition(String text, int size) {
        List<String> keywordList = HanLP.extractPhrase(text, size);
        return BaseSwaggerVo.success(keywordList);
    }
}
