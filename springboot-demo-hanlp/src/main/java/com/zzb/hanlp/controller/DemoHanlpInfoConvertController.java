package com.zzb.hanlp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.zzb.hanlp.controller.vo.PinYinConvertVo;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类名称：DemoHanlpInfoConvertController
 * 类描述：Hanlp转化控制器
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/28 1:42 下午
 * 修改备注：TODO
 */
@Api(value = "文本转换样例", tags = "文本转换样例")
@RestController
public class DemoHanlpInfoConvertController {
    @ApiOperationSupport(order = 1)
    @ApiOperation(
        value = "拼音转换",
        notes = "拼音转换(PinyinDictionary)。<br />" +
            "说明:<br />" +
            " |- HanLP不仅支持基础的汉字转拼音，还支持声母、韵母、音调、音标和输入法首字母首声母功能。<br />" +
            " |- HanLP能够识别多音字，也能给繁体中文注拼音。<br />" +
            " |- 最重要的是，HanLP采用的模式匹配升级到AhoCorasickDoubleArrayTrie，性能大幅提升，能够提供毫秒级的响应速度！<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/java-chinese-characters-to-pinyin-and-simplified-conversion-realization.html#h2-17\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String")
    })
    @PostMapping("/pinyinConvert")
    @ResponseBody
    public BaseSwaggerVo<List<Pinyin>> pinyinConvert(String text) {
        List<Pinyin> pinyinList = HanLP.convertToPinyinList(text);
        return BaseSwaggerVo.success(PinYinConvertVo.getInstance(text, pinyinList).toJSON());
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(
        value = "简繁转换",
        notes = "简繁转换(SimplifiedChineseDictionary)。<br />" +
            "说明:<br />" +
            " |- HanLP能够识别简繁分歧词，比如打印机=印表機。许多简繁转换工具不能区分“以后”“皇后”中的两个“后”字,HanLP可以。<br />" +
            "具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/java-chinese-characters-to-pinyin-and-simplified-conversion-realization.html#h2-17\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
        @ApiImplicitParam(name = "type", value = "转换类型", required = true, dataType = "String", paramType = "query",
            allowableValues = "toSimplified,toTraditional", allowMultiple = true)
    })
    @PostMapping("/jfConvert")
    @ResponseBody
    public BaseSwaggerVo<String> jfConvert(String text, String type) {
        if ("toSimplified".equals(type)) {
            return BaseSwaggerVo.success(HanLP.convertToSimplifiedChinese(text));
        } else {
            return BaseSwaggerVo.success(HanLP.convertToTraditionalChinese(text));

        }
    }
}
