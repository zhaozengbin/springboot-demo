package com.zzb.hanlp.controller;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONArray;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hankcs.hanlp.suggest.Suggester;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 类名称：DemoHanlpRecommendController
 * 类描述：Hanlp推荐控制器
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/28 1:42 下午
 * 修改备注：TODO
 */
@Api(value = "Hanlp文本推荐样例", tags = "Hanlp文本推荐样例")
@RestController
public class DemoHanlpRecommendController {
    private final static String TEXAMPLE_CANDIDATE_TEXT =
        "[\"威廉王子发表演说 呼吁保护野生动物\"," +
            "\"《时代》年度人物最终入围名单出炉 普京马云入选\"," +
            "\"“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\"," +
            "\"日本保密法将正式生效 日媒指其损害国民知情权\"," +
            "\"英报告说空气污染带来“公共健康危机”\"]";


    @ApiOperationSupport(order = 1)
    @ApiOperation(
        value = "文本推荐",
        notes = "文本推荐(Suggester)。<br />" +
            "说明:<br />" +
            " |- 从一系列句子中挑出与输入句子最相似的那一个。包括：语义推荐、拼音推荐、字词推荐。<br />" +
            " |- 在搜索引擎的输入框中，用户输入一个词，搜索引擎会联想出最合适的搜索词，HanLP实现了类似的功能。<br />" +
            " |- 可以动态调节每种识别器的权重。<br />" +
            " |- 默认候选文本(仅供测试使用)。<br />" + TEXAMPLE_CANDIDATE_TEXT,
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "candidateText", value = "候选文本，可用过数组形式传递多条", dataType = "Array"),
        @ApiImplicitParam(name = "text", value = "依据文本,如:危机公关,mayun", required = true, dataType = "String"),
        @ApiImplicitParam(name = "size", value = "匹配数量", required = true, defaultValue = "1", dataType = "Integer")
    })
    @PostMapping("/recommend")
    @ResponseBody
    public BaseSwaggerVo<List<String>> suggest(String[] candidateText, String text, int size) {
        Suggester suggester = new Suggester();
        // 加入默认候选文本
        Arrays.asList(JSONArray.parseArray(TEXAMPLE_CANDIDATE_TEXT).toArray(new String[0])).forEach(suggester::addSentence);

        if (ArrayUtil.isNotEmpty(candidateText)) {
            // 加入候选文本
            Arrays.asList(candidateText).forEach(suggester::addSentence);
        }

        // 推荐文本
        List<String> list = suggester.suggest(text, size);
        return BaseSwaggerVo.success(list);
    }

}
