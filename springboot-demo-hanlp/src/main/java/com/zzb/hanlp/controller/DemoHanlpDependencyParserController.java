package com.zzb.hanlp.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.zzb.core.aop.ann.CallTimeAnn;
import com.zzb.hanlp.controller.vo.DependencyParserVo;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：DemoHanlpDependencyParserController
 * 类描述：Hanlp句法依存控制器
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/28 1:42 下午
 * 修改备注：TODO
 */
@Api(value = "句法依存样例", tags = "句法依存样例")
@RestController
public class DemoHanlpDependencyParserController {

    @ApiOperationSupport(order = 1)
    @ApiOperation(
        value = "句法依存分析",
        notes = "句法依存分析(NeuralNetworkDependencyParser)。<br />" +
            "说明:<br />" +
            " |- 内部采用NeuralNetworkDependencyParser实现，用户可以直接调用NeuralNetworkDependencyParser.compute(sentence)。<br />" +
            " |- 也可以调用基于ArcEager转移系统的柱搜索依存句法分析器KBeamArcEagerDependencyParser。<br />" +
            " |- 具体算法介绍详见<a href=\"http://www.hankcs.com/nlp/parsing/neural-network-based-dependency-parser.html\">文档</a>。<br />",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "文本", required = true, dataType = "String"),
        @ApiImplicitParam(name = "text", value = "从某个节点一路遍历到根", required = true, dataType = "Integer"),
    })
    @PostMapping("/analysis")
    @ResponseBody
    @CallTimeAnn
    public BaseSwaggerVo<DependencyParserVo> analysis(String text, Integer nodeIndex) {
        CoNLLSentence sentence = HanLP.parseDependency(text);
        List<String> list = new ArrayList<>();
        // 可以方便地遍历它
        for (CoNLLWord word : sentence) {
            list.add(String.format("%s --(%s)--> %s", word.LEMMA, word.DEPREL, word.HEAD.LEMMA));
        }
        return BaseSwaggerVo.success(DependencyParserVo.getInstance(list, sentence, nodeIndex));
    }

}
