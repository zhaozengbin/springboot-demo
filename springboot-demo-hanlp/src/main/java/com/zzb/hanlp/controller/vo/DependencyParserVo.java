package com.zzb.hanlp.controller.vo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：SegmentConfigVo
 * 类描述：句法依存分析VO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/10/27 2:48 下午
 * 修改备注：TODO
 */
@ApiModel(value = "句法依存分析实体", description = "句法依存分析实体")
public class DependencyParserVo {

    @ApiModelProperty(notes = "句法依存分析结果列表", value = "句法依存分析结果列表")
    private List<String> list;


    @ApiModelProperty(notes = "句法依存分析结果树详细列表，可以作为nodeIndex的参考", value = "句法依存分析结果树详细列表，可以作为nodeIndex的参考")
    private List<String> treeList;

    @ApiModelProperty(notes = "句法依存分析结果树", value = "句法依存分析结果树")
    private String tree;

    public DependencyParserVo(List<String> list, List<String> treeList) {
        this.list = list;
        this.treeList = treeList;
    }

    public DependencyParserVo(List<String> list, List<String> treeList, String tree) {
        this.list = list;
        this.treeList = treeList;
        this.tree = tree;
    }

    public static DependencyParserVo getInstance(List<String> list, CoNLLSentence sentence, Integer nodeIndex) {
        List<String> treeList = new ArrayList<>();
        List<String> tree = new ArrayList<>();
        CoNLLWord[] wordArray = sentence.getWordArray();

        for (int i = 0; i < wordArray.length; i++) {
            treeList.add("[" + (i + 1) + "]" + wordArray[i].HEAD);
        }
        if (ObjectUtil.isNotEmpty(nodeIndex)) {
            // 还可以直接遍历子树，从某棵子树的某个节点一路遍历到虚根
            CoNLLWord head = wordArray[nodeIndex - 1];
            while ((head = head.HEAD) != null) {
                if (head == CoNLLWord.ROOT) {
                    tree.add(head.LEMMA);
                } else {
                    tree.add(String.format("%s --(%s)--> ", head.LEMMA, head.DEPREL));
                }
            }
            return new DependencyParserVo(list, treeList, CollUtil.join(tree, ""));
        }
        return new DependencyParserVo(list, treeList);
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public List<String> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<String> treeList) {
        this.treeList = treeList;
    }
}
