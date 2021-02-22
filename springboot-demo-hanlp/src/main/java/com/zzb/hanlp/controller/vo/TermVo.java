package com.zzb.hanlp.controller.vo;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.seg.common.Term;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "分词结果实体", description = "分词结果实体")
public class TermVo {
    /**
     * 词语
     */
    @ApiModelProperty(notes = "词语", value = "词语")
    public String word;

    /**
     * 词性
     */
    @ApiModelProperty(notes = "词性", value = "词性")
    public String nature;

    /**
     * 在文本中的起始位置（需开启分词器的offset选项）
     */
    @ApiModelProperty(notes = "在文本中的起始位置", value = "在文本中的起始位置")
    public int offset;

    public TermVo(String word, String nature, int offset) {
        this.word = word;
        this.nature = nature;
        this.offset = offset;
    }

    public static List<TermVo> term2Vo(List<Term> terms) {
        List<TermVo> termVoList = new ArrayList<>();
        if (ObjectUtil.isNotNull(terms)) {
            terms.forEach(term -> {
                termVoList.add(new TermVo(term.word, (ObjectUtil.isNotNull(term.nature) ? term.nature.toString() : ""), term.offset));
            });
        }
        return termVoList;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
