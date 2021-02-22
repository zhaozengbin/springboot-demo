package com.zzb.hanlp.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import com.zzb.hanlp.init.InitConfig;
import com.zzb.hanlp.utils.ModelUtils;
import com.zzb.swagger.controller.vo.BaseSwaggerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zzb.swagger.controller.vo.BaseSwaggerVo.success;

/**
 * 类名称：DemoHanlpWord2VecController
 * 类描述：Hanlp词向量控制器
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/28 1:42 下午
 * 修改备注：TODO
 */
@Api(value = "词向量样例", tags = "词向量样例")
@RestController
public class DemoHanlpWord2VecController {

    @ApiOperationSupport(order = 1)
    @ApiOperation(
        value = "一个有意思的例子",
        notes = "一个有意思的例子。通过词语的加减来简单的了解下词向量，基准词 - 指标词 + 新指标词 = 新词<br />" +
            "例:" +
            "机场 - 飞机 + 火车 = 高铁站 <br />" +
            "Windows - microsoft + google = android <br />" +
            "老婆 - 老公 + 丈夫 = 妻子 <br />" +
            "北京 - 中国 + 法国 = 巴黎 <br />" +
            "天安门 - 北京 + 巴黎 = 艾菲尔铁塔 <br />" +
            "说明:<br />" +
            " |- 词向量:假设词向量模型是一个水池子，那把每个词看做是池子里的小鱼，设想属性越相似的小鱼挨着就越近，那么每条小鱼所在池子的位置就是这条鱼的向量值。<br />" +
            "当然，咱们平时面对的空间大多是三维的，拿上面的例子来看就是长、宽、高。也就是X、Y、Z三个就能定义出一个唯一的点，那每条小鱼的词向量维度就是三维的。<br />" +
            "但是在计算机语言中，每个词的向量都是很多维度的，比如词的词性、词的所代表的领域、词所代表的领域的地域等等。例如：刘德华、张学友、郭富城和刘欢、那英的词向量位置<br />" +
            "都会很相近，因为他们都是明星、歌手。但是前三个词的向量要比后两个词的向量要更接近，因为前三个都是香港明星，但后两个是内地明星。后两个词再进一步比较刘欢要比那英的词向量位置要要更贴近前三个词，具体为啥？应该很明白了吧<br />" +
            "具体算法介绍详见<a href=\"https://www.yuque.com/docs/share/16f87611-f511-4c89-a927-e9bc9a481c9c?# 《搞懂NLP中的词向量，看这一篇就足够》\">文档</a>",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "baseText", value = "基准词", required = true, dataType = "String"),
        @ApiImplicitParam(name = "indexText", value = "基因词", required = true, dataType = "String"),
        @ApiImplicitParam(name = "nIndexText", value = "新基因词", required = true, dataType = "String"),
        @ApiImplicitParam(name = "size", value = "结果数量", defaultValue = "1", dataType = "Integer")
    })
    @PostMapping("/analogy")
    @ResponseBody
    public BaseSwaggerVo<List<Map.Entry<String, Float>>> analogy(String baseText, String indexText, String nIndexText, int size) {
        List<Map.Entry<String, Float>> result = InitConfig.wordVectorModelInstance().analogy(baseText, indexText, nIndexText, size);
        return success(result);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(
        value = "获取相似词",
        notes = "词语相似度(WordVectorModel.nearest(text,size)<br />" +
            "获取相似度较高的词语",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "text", value = "词语", required = true, dataType = "String"),
        @ApiImplicitParam(name = "size", value = "高相似度词语数量", required = true, dataType = "Integer"),
    })
    @PostMapping("/nearest")
    @ResponseBody
    public BaseSwaggerVo<List<Map.Entry<String, Float>>> nearest(String text, int size) {
        List<Map.Entry<String, Float>> result = InitConfig.wordVectorModelInstance().nearest(text, size);
        return success(result);
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(
        value = "词语相似度",
        notes = "词语相似度(WordVectorModel.similarity(what,with))<br />" +
            "获取两个词语的相似度",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "what", value = "词语A", required = true, dataType = "String"),
        @ApiImplicitParam(name = "with", value = "词语B", required = true, dataType = "String"),
        @ApiImplicitParam(name = "tempModelType", value = "是否使用临时训练的模型（选择true必须提前到训练模型demo里面训练模型否则还是使用默认模型）",
            dataType = "Boolean", paramType = "query", allowableValues = "true,false", allowMultiple = true)
    })
    @PostMapping("/word/similarity")
    @ResponseBody
    public BaseSwaggerVo<Float> wordSimilarity(String what, String with, boolean tempModelType) {
        float score = -1;
        if (tempModelType && ObjectUtil.isNotNull(ModelUtils.tempWordVectorModel)) {
            score = ModelUtils.tempWordVectorModel.similarity(what, with);
        } else {
            score = InitConfig.wordVectorModelInstance().similarity(what, with);
        }
        return success(String.format("共【%s】个词的维度", InitConfig.wordVectorModelInstance().size()), score);
    }

    @ApiOperationSupport(order = 4)
    @ApiOperation(
        value = "句子相似度",
        notes = "句子相似度(DocVectorModel.similarity(what,with))<br />" +
            "获取两个词语的相似度",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "what", value = "词语A", required = true, dataType = "String"),
        @ApiImplicitParam(name = "with", value = "词语B", required = true, dataType = "String"),
        @ApiImplicitParam(name = "tempModelType", value = "是否使用临时训练的模型（选择true必须提前到训练模型demo里面训练模型否则还是使用默认模型）",
            dataType = "Boolean", paramType = "query", allowableValues = "true,false", allowMultiple = true)
    })
    @PostMapping("/doc/similarity")
    @ResponseBody
    public BaseSwaggerVo<Float> docSimilarity(String what, String with, boolean tempModelType) {
        float score = -1;
        if (tempModelType && ObjectUtil.isNotNull(ModelUtils.tempWordVectorModel)) {
            score = InitConfig.docVectorModelInstance(ModelUtils.tempWordVectorModel).similarity(what, with);
        } else {
            score = InitConfig.docVectorModelInstance().similarity(what, with);
        }
        return success(String.format("共【%s】个词的维度", InitConfig.wordVectorModelInstance().size()), score);
    }


    @ApiOperationSupport(order = 5)
    @ApiOperation(
        value = "训练模型",
        notes = "训练向量，真实训练需要大量问的熟语料，此demo目的让大家看到语料文件和训练的模型是什么样的",
        httpMethod = "POST")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "rawCorporaFolder", value = "输入文件夹，输入生语料(原始文本格式,需要自行处理特殊字符、html字符等影响训练的字符)", required = true, dataType = "String"),
        @ApiImplicitParam(name = "size", value = "读取几个原始语料文件", required = true, dataType = "Integer")
    })
    @PostMapping("/modelTrain")
    @ResponseBody
    public BaseSwaggerVo<Map<String, Object>> modelTrain(String rawCorporaFolder, int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            File familiarCorporaFile = ModelUtils.createCorpus(ModelUtils.NLP_MODEL_NAME_WORD2VEC, rawCorporaFolder, size);
            if (FileUtil.isNotEmpty(familiarCorporaFile)) {
                try {
                    String familiarCorporaText = FileUtils.readFileToString(familiarCorporaFile, "utf-8");
                    result.put("熟语料样例(50条)", Arrays.asList(familiarCorporaText.split("\n")).subList(0, 50));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File modelFile = modelTrain(familiarCorporaFile.getPath());
            String modelText = FileUtils.readFileToString(modelFile, "utf-8");
            result.put("模型样例(50条)", Arrays.asList(modelText.split("\n")).subList(0, 50));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success(result);

    }


    /**
     * 方法：modelTrain
     * 描述：训练模型
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param familiarCorpora :
     * @return : com.zzb.swagger.controller.vo.BaseSwaggerVo<java.lang.String>
     * @date: 2020年11月04日 2:15 下午
     */
    private File modelTrain(String familiarCorpora) throws Exception {
        File familiarCorporaFile = new File(familiarCorpora);
        if (familiarCorporaFile.exists()) {
            File modelFile = new File(familiarCorporaFile.getParentFile().getParent() +
                File.separator + ModelUtils.NLP_FILE_DIR_MODEL +
                File.separator + familiarCorporaFile.getName());
            if (modelFile.exists()) {
                modelFile.delete();
            } else if (!modelFile.getParentFile().exists()) {
                modelFile.mkdirs();
            }
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            ModelUtils.tempWordVectorModel = trainerBuilder.train(familiarCorpora, modelFile.getPath());
            return modelFile;
        } else {
            throw new Exception("文件不存在");
        }
    }
}
