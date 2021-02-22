package com.zzb.hanlp.controller.vo;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称：PinYinConvertVo
 * 类描述：拼音转换Vo
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/28 11:50 上午
 * 修改备注：TODO
 */
@ApiModel(value = "拼音转换实体", description = "拼音转换实体")
public class PinYinConvertVo {

    @ApiModelProperty(notes = "拼音转换原文", value = "拼音转换原文")
    private String text;

    @ApiModelProperty(notes = "拼音转换结果,key：描述,value:结果", value = "拼音转换结果,key：描述,value:结果")
    private Map<String, List<Object>> pinyin;

    public PinYinConvertVo() {
    }

    public PinYinConvertVo(String text, Map<String, List<Object>> pinyin) {
        this.text = text;
        this.pinyin = pinyin;

    }

    public static PinYinConvertVo getInstance(String text, List<Pinyin> pinyinList) {
        Map<String, List<Object>> pinyinMap = new HashMap<>();
        List<Object> sourceText = new ArrayList<>();
        for (char c : text.toCharArray()) {
            sourceText.add(String.valueOf(c));
        }
        pinyinMap.put("原文分解", sourceText);

        List<Object> szyd = new ArrayList<>();
        for (Pinyin pinyin : pinyinList) {
            szyd.add(pinyin);
        }
        pinyinMap.put("拼音（数字音调）", szyd);

        List<Object> fzyd = new ArrayList<>();
        for (Pinyin pinyin : pinyinList) {
            fzyd.add(pinyin.getPinyinWithToneMark());
        }
        pinyinMap.put("拼音（符号音调）", fzyd);


        List<Object> wyd = new ArrayList<>();
        for (Pinyin pinyin : pinyinList) {
            wyd.add(pinyin.getPinyinWithoutTone());
        }
        pinyinMap.put("拼音（无音调）", wyd);

        List<Object> sd = new ArrayList<>();
        for (Pinyin pinyin : pinyinList) {
            sd.add(pinyin.getTone());
        }
        pinyinMap.put("声调", sd);

        List<Object> sm = new ArrayList<>();
        for (Pinyin pinyin : pinyinList) {
            sm.add(pinyin.getShengmu());
        }
        pinyinMap.put("声母", sm);

        List<Object> ym = new ArrayList<>();
        for (Pinyin pinyin : pinyinList) {
            ym.add(pinyin.getYunmu());
        }
        pinyinMap.put("韵母", ym);

        List<Object> srft = new ArrayList<>();
        for (Pinyin pinyin : pinyinList) {
            srft.add(pinyin.getHead());
        }
        pinyinMap.put("输入法头", srft);
        return new PinYinConvertVo(text, pinyinMap);
    }

    public JSONObject toJSON() {
        return JSON.parseObject(this.toString());
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("原文", this.text);
        this.pinyin.forEach((key, value) -> jsonObject.put(key, JSONArray.toJSON(value)));
        return jsonObject.toJSONString();
    }
}
